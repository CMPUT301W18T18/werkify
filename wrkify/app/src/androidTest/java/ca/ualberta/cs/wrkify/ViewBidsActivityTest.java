/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBackUnconditionally;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withParentIndex;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * Tests for ViewBidsActivity.
 */
@IntentTest
public class ViewBidsActivityTest extends AbstractIntentTest<ViewBidsActivity> {
    private Task task;
    private User user;
    private List<User> bidUsers = new ArrayList<>();

    @Override
    protected Class<ViewBidsActivity> getActivityClass() {
        return ViewBidsActivity.class;
    }

    @Nullable
    @Override
    protected User getInitialSessionUser() {
        return user;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Override
    protected void createMockData(MockRemoteClient client) {
        user = client.create(User.class, "User", "user@example.com", "1958203971");
        task = client.create(Task.class, "Task with bids", user, "Description");

        task.addBid(new Bid(new Price(13.00), makeBidUser()));
        task.addBid(new Bid(new Price(11.00), makeBidUser()));
        task.addBid(new Bid(new Price(12.00), makeBidUser()));
        task.addBid(new Bid(new Price(10.00), makeBidUser()));
    }

    private User makeBidUser() {
        String userIndex = String.valueOf(bidUsers.size());
        User bidUser = getClient().create(User.class, "User" + userIndex, "user" + userIndex + "@example.com", "0000000000");
        bidUsers.add(bidUser);
        return bidUser;
    }

    private void launchActivityWithTask() {
        Intent intent = new Intent();
        intent.putExtra(ViewBidsActivity.EXTRA_VIEWBIDS_TASK, task);
        launchActivity(intent);
    }

    private Matcher<View> bidView(int index) {
        return withParent(allOf(
                instanceOf(CardView.class),
                withParentIndex(index),
                withParent(withId(R.id.bidListRecyclerView))));
    }

    /**
     * View bid list.
     * Should: show all bids, sorted low to high
     *         show the current bid count
     *         allow selecting a bid
     *         allow viewing profile from selected bid
     */
    @Test
    public void testViewBidsActivity() {
        launchActivityWithTask();

        onView(withId(R.id.bidListActivity_taskTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.bidListActivity_taskTitle)).check(matches(withText("Task with bids")));

        onView(withId(R.id.bidListActivity_bidCount)).check(matches(isDisplayed()));
        onView(withId(R.id.bidListActivity_bidCount)).check(matches(withText(containsString("4"))));

        onView(bidView(0)).check(matches(withChild(withText("$10.00"))));
        onView(bidView(1)).check(matches(withChild(withText("$11.00"))));
        onView(bidView(2)).check(matches(withChild(withText("$12.00"))));
        onView(bidView(3)).check(matches(withChild(withText("$13.00"))));

        onView(bidView(0)).check(matches(withChild(withText("User3"))));
        onView(bidView(1)).check(matches(withChild(withText("User1"))));
        onView(bidView(2)).check(matches(withChild(withText("User2"))));
        onView(bidView(3)).check(matches(withChild(withText("User0"))));

        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_accept))).check(matches(not(isDisplayed())));
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_reject))).check(matches(not(isDisplayed())));
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_viewProfile))).check(matches(not(isDisplayed())));

        onView(bidView(1)).perform(click());

        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_accept))).check(matches(isDisplayed()));
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_reject))).check(matches(isDisplayed()));
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_viewProfile))).check(matches(isDisplayed()));

        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_viewProfile))).perform(click());
        intended(allOf(
                hasComponent(component(ViewProfileActivity.class)),
                hasExtra(ViewProfileActivity.USER_EXTRA, bidUsers.get(1))));
    }

    /**
     * Reject bids.
     * Should: remove the bid from the list
     *         update the counter
     *         remove the bid from the task on return
     */
    @Test
    public void testRejectBid() {
        launchActivityWithTask();

        onView(withId(R.id.bidListActivity_bidCount)).check(matches(withText(containsString("4"))));
        onView(bidView(0)).check(matches(withChild(withText("$10.00"))));
        onView(bidView(1)).check(matches(withChild(withText("$11.00"))));
        onView(bidView(2)).check(matches(withChild(withText("$12.00"))));
        onView(bidView(3)).check(matches(withChild(withText("$13.00"))));

        onView(bidView(1)).perform(click());
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_reject))).perform(click());

        onView(withId(R.id.bidListActivity_bidCount)).check(matches(withText(containsString("3"))));
        onView(bidView(0)).check(matches(withChild(withText("$10.00"))));
        onView(bidView(1)).check(matches(withChild(withText("$12.00"))));
        onView(bidView(2)).check(matches(withChild(withText("$13.00"))));

        onView(allOf(withParent(bidView(2)), withId(R.id.bidListItem_reject))).perform(click());

        onView(withId(R.id.bidListActivity_bidCount)).check(matches(withText(containsString("2"))));
        onView(bidView(0)).check(matches(withChild(withText("$10.00"))));
        onView(bidView(1)).check(matches(withChild(withText("$12.00"))));

        pressBackUnconditionally();

        assertActivityFinished();
        assertEquals(ViewBidsActivity.RESULT_OK, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(ViewBidsActivity.EXTRA_RETURNED_TASK);
        assertEquals(TaskStatus.BIDDED, task.getStatus());
        assertEquals(2, task.getBidList().size());
        assertEquals(new Price(10.00), task.getBidList().get(0).getValue());
        assertEquals(new Price(12.00), task.getBidList().get(1).getValue());
    }

    /**
     * Accept a bid.
     * Should: finish the activity
     *         set the accepted bid
     */
    @Test
    public void testAcceptBid() {
        launchActivityWithTask();

        onView(bidView(2)).perform(click());
        onView(allOf(withParent(bidView(2)), withId(R.id.bidListItem_accept))).perform(click());

        assertActivityFinished();
        assertEquals(ViewBidsActivity.RESULT_OK, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(ViewBidsActivity.EXTRA_RETURNED_TASK);
        assertEquals(TaskStatus.ASSIGNED, task.getStatus());
        assertEquals(new Price(12.00), task.getPrice());

        try {
            assertEquals(bidUsers.get(2), task.getRemoteProvider(getClient()));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Reject all the bids.
     * Should: set task status to REQUESTED
     */
    @Test
    public void testRejectAllBids() {
        launchActivityWithTask();

        // NOTE: This will actually fail if Animator animations are enabled!
        onView(bidView(3)).perform(click());
        onView(allOf(withParent(bidView(3)), withId(R.id.bidListItem_reject))).perform(click());
        onView(bidView(2)).perform(click());
        onView(allOf(withParent(bidView(2)), withId(R.id.bidListItem_reject))).perform(click());
        onView(bidView(1)).perform(click());
        onView(allOf(withParent(bidView(1)), withId(R.id.bidListItem_reject))).perform(click());
        onView(bidView(0)).perform(click());
        onView(allOf(withParent(bidView(0)), withId(R.id.bidListItem_reject))).perform(click());

        pressBackUnconditionally();

        assertActivityFinished();
        assertEquals(ViewBidsActivity.RESULT_OK, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(ViewBidsActivity.EXTRA_RETURNED_TASK);
        assertEquals(TaskStatus.REQUESTED, task.getStatus());
        assertEquals(0, task.getBidList().size());
    }
}
