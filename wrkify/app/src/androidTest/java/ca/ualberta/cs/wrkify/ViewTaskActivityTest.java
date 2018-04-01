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

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.pressBackUnconditionally;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * Tests for ViewTaskActivity.
 */
public class ViewTaskActivityTest extends AbstractIntentTest<ViewTaskActivity> {
    private User requester;
    private User provider;
    private User otherBidder;
    private User otherUser;

    private Task unbiddedTask;
    private Task biddedTask;
    private Task acceptedTask;
    private Task closedTask;

    @Override
    protected Class<ViewTaskActivity> getActivityClass() {
        return ViewTaskActivity.class;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Override
    protected void createMockData(MockRemoteClient client) {
        requester = client.create(User.class, "TaskRequester", "task-requester@example.com", "0000000000");
        provider = client.create(User.class, "TaskProvider", "task-provider@example.com", "0000000000");
        otherBidder = client.create(User.class, "TaskBidder", "task-bidder@example.com", "0000000000");
        otherUser = client.create(User.class, "OtherUser", "other-user@example.com", "0000000000");

        unbiddedTask = client.create(Task.class, "Unbidded task", requester, "A task with no bids");
        biddedTask = client.create(Task.class, "Bidded task", requester, "A task with two bids");
        acceptedTask = client.create(Task.class, "Accepted task", requester, "An accepted task");
        closedTask = client.create(Task.class, "Closed task", requester, "A closed task");

        biddedTask.addBid(new Bid(new Price(10.00), provider));
        biddedTask.addBid(new Bid(new Price(12.00), otherBidder));
        acceptedTask.addBid(new Bid(new Price(10.00), provider));
        acceptedTask.addBid(new Bid(new Price(12.00), otherBidder));
        closedTask.addBid(new Bid(new Price(10.00), provider));
        closedTask.addBid(new Bid(new Price(12.00), otherBidder));

        acceptedTask.acceptBid(acceptedTask.getBidList().get(0));
        closedTask.acceptBid(acceptedTask.getBidList().get(0));

        closedTask.complete();
    }

    private void launchActivityWith(User sessionUser, Task task) {
        getSession().setUser(sessionUser);

        Intent intent = new Intent();
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);
        launchActivity(intent);
    }

    private void checkTaskDetails(Task task) {
        onView(withId(R.id.taskViewTitle)).check(matches(allOf(isDisplayed(), withText(task.getTitle()))));
        onView(withId(R.id.taskViewDescription)).check(matches(allOf(isDisplayed(), withText(task.getDescription()))));
    }

    private void assertNoEditButton() {
        onView(withId(R.id.taskViewButtonEdit)).check(matches(not(isDisplayed())));
    }

    private void assertHasEditButton() {
        onView(withId(R.id.taskViewButtonEdit)).check(matches(isDisplayed()));
    }

    private void assertBottomSheetCollapsed() {
        onView(withId(R.id.taskViewBottomSheetContent)).check(matches(not(hasDescendant(isDisplayed()))));
    }

    private void assertBottomSheetExpanded() {
        onView(withId(R.id.taskViewBottomSheetContent)).check(matches(hasDescendant(isDisplayed())));
    }

    private void assertHasStatus(String status, String detail, String rightStatus, String rightDetail) {
        onView(withId(R.id.taskViewBottomSheetStatus)).check(matches(withText(containsString(status))));
        onView(withId(R.id.taskViewBottomSheetDetail)).check(matches(withText(containsString(detail))));
        onView(withId(R.id.taskViewBottomSheetRightStatus)).check(matches(withText(containsString(rightStatus))));
        onView(withId(R.id.taskViewBottomSheetRightDetail)).check(matches(withText(containsString(rightDetail))));
    }

    /**
     * There are no bids on the task, and the session user is not the requester.
     * Should: show basic task details
     *         not display an edit button
     *         show status OPEN
     *         show "No bids"
     *         allow placing a bid
     */
    @Test
    public void testViewTaskActivity() {
        launchActivityWith(otherUser, unbiddedTask);

        checkTaskDetails(unbiddedTask);
        assertNoEditButton();
        assertBottomSheetCollapsed();
        assertHasStatus("Open", "No bids", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetExpanded();

        onView(withId(R.id.taskViewBottomSheetBidField)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetBidField)).perform(typeText("12.00"));

        onView(withId(R.id.taskViewBottomSheetButtonBid)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetButtonBid)).perform(click());
        onView(withText("Bid")).perform(click());

        closeSoftKeyboard();

        pressBack();
        assertBottomSheetCollapsed();

        pressBackUnconditionally();
        assertActivityFinished();

        try {
            Task editedTask = getClient().download(unbiddedTask.getId(), Task.class);

            assertEquals(TaskStatus.BIDDED, editedTask.getStatus());
            assertEquals(1, editedTask.getBidList().size());
            assertEquals(new Price("12.00"), editedTask.getBidList().get(0).getValue());

            assertEquals(otherUser, editedTask.getBidList().get(0).getRemoteBidder(getClient()));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * ViewTaskActivity test:
     * there are bids on the task, but the session user has not bidded.
     *//*
    @Test
    public void testViewBiddedTask() {
        Task task = new Task("Example task with bids", exampleUser1, aLongDescription);
        task.addBid(new Bid(new Price(20.00), exampleUser2));
        task.addBid(new Bid(new Price(30.00), exampleUser2));
        startViewTaskActivityWith(task, exampleUser3);
    } */

    /**
     * ViewTaskActivity test:
     * the session user has bidded on the task, and does not have
     * the current highest bid.
     *//*
    @Test
    public void testViewSelfBiddedTask() {
        // TODO This doesn't currently work any differently from a normal bidded task
        Task task = new Task("Example task of session user", exampleUser1, aLongDescription);
        task.addBid(new Bid(new Price(20.00), exampleUser2));
        task.addBid(new Bid(new Price(30.00), exampleUser3));
        startViewTaskActivityWith(task, exampleUser3);
    } */

    /**
     * ViewTaskActivity test:
     * the task being viewed was requested by the session user.
     *//*
    @Test
    public void testViewOwnUnbiddedTask() {
        Task task = new Task("Example requested task", exampleUser1, aLongDescription);
        startViewTaskActivityWith(task, exampleUser1);
    } */

    /**
     * ViewTaskActivity test:
     * the session user is the requester, and the task has bids.
     *//*
    @Test
    public void testViewOwnBiddedTask() {
        Task task = new Task("Example bidded requested task", exampleUser1, aLongDescription);
        task.addBid(new Bid(new Price(15.15), exampleUser2));
        task.addBid(new Bid(new Price(20.25), exampleUser3));
        startViewTaskActivityWith(task, exampleUser1);
    } */

    /**
     * ViewTaskActivity test:
     * the task is assigned, but the session user is neither the
     * requester nor the assignee.
     *//*
    @Test
    public void testViewAssignedTask() {
        Task task = new Task("Example assigned task", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser3);
    } */

    /**
     * ViewTaskActivity test:
     * the task is assigned to the session user
     *//*
    @Test
    public void testViewAssignedToSelfTask() {
        // TODO This doesn't currently work any differently from a task assigned to another user
        Task task = new Task("Example task assigned to self", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser2);
    } */

    /**
     * ViewTaskActivity test:
     * the task is requested by the session user,
     * and is assigned
     *//*
    @Test
    public void testViewOwnAssignedTask() {
        Task task = new Task("Example task assigned to other", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser1);
    } */

    /**
     * ViewTaskActivity test:
     * the task is closed
     *//*
    @Test
    public void testViewCompletedTask() {
        Task task = new Task("Closed task", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser3);
    } */
}