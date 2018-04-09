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

import android.support.v7.widget.CardView;
import android.view.View;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests for MainActivity.
 */
@IntentTest
public class MainActivityTest extends AbstractIntentTest<MainActivity> {
    private User requester;
    private User provider;
    private User otherBidder;
    private User otherUser;

    private Task unbiddedTask;
    private Task biddedTask;
    private Task acceptedTask;
    private Task closedTask;

    @Override
    protected Class<MainActivity> getActivityClass() {
        return MainActivity.class;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Override
    protected void createMockData(CachingClient<MockRemoteClient> client) {
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

        try {
            client.upload(unbiddedTask);
            client.upload(biddedTask);
            client.upload(acceptedTask);
            client.upload(closedTask);
        } catch (IOException e) {
            fail();
        }
    }

    private void launchAsRequester() {
        getSession().setUser(requester);
        launchActivity();
    }

    private void launchAsProvider() {
        getSession().setUser(provider);
        launchActivity();
    }

    private Matcher<View> tabWithText(String text) {
        return allOf(isDescendantOfA(withId(R.id.overviewTabBar)), withText(text));
    }

    private Matcher<View> taskView(int index) {
        return allOf(withParentIndex(index), withParent(allOf(anyOf(withId(R.id.taskListView), withId(R.id.taskSearchRecycler)), isDisplayed())));
    }

    /**
     * View My Tasks as a task provider.
     * Should: show assigned task in the first tab
     *         show bidded task in the second tab
     *         show assigned and closed task in the third tab
     *         view a task on click
     */
    @Test
    public void testProviderView() {
        launchAsProvider();
        onView(withId(R.id.navigation_tasks)).perform(click());
        onView(allOf(withId(R.id.overviewSelfView), instanceOf(UserView.class), isDisplayed()))
                .check(matches(hasDescendant(withText("TaskProvider"))));

        onView(tabWithText("Assigned")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(0)))));
        onView(tabWithText("Assigned")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(not(isDisplayed())));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("Assigned")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Accepted task")))).check(matches(isDisplayed()));

        onView(tabWithText("Bidded")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(1)))));
        onView(tabWithText("Bidded")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(not(isDisplayed())));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("$10.00")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Bidded task")))).check(matches(isDisplayed()));

        onView(tabWithText("Completed")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(2)))));
        onView(tabWithText("Completed")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(not(isDisplayed())));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("Done")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Closed task")))).check(matches(isDisplayed()));

        onView(taskView(0)).perform(click());
        intended(allOf(
                hasComponent(component(ViewTaskActivity.class)),
                hasExtra(ViewTaskActivity.EXTRA_TARGET_TASK, closedTask)));
    }

    /**
     * View My Posts as a task requester.
     * Should: show requested unbidded task and requested bidded task in the first tab
     *         show assigned task in the second tab
     *         show assigned and closed task in the third tab
     *         view a task on click
     */
    @Test
    public void testRequesterView() {
        launchAsRequester();
        onView(withId(R.id.navigation_posts)).perform(click());
        onView(allOf(withId(R.id.overviewSelfView), instanceOf(UserView.class), isDisplayed()))
                .check(matches(hasDescendant(withText("TaskRequester"))));

        onView(tabWithText("Requested")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(0)))));
        onView(tabWithText("Requested")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(isDisplayed()));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("Requested")),
                hasDescendant(withText("Unbidded task")))).check(matches(isDisplayed()));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("$10.00")),
                hasDescendant(withText("Bidded task")))).check(matches(isDisplayed()));

        onView(tabWithText("Assigned")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(1)))));
        onView(tabWithText("Assigned")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(not(isDisplayed())));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("Assigned")),
                hasDescendant(withText("TaskProvider")),
                hasDescendant(withText("Accepted task")))).check(matches(isDisplayed()));

        onView(tabWithText("Closed")).check(matches(allOf(isDisplayed(), withParent(withParentIndex(2)))));
        onView(tabWithText("Closed")).perform(click());
        onView(withId(R.id.overviewAddButton)).check(matches(not(isDisplayed())));

        onView(allOf(
                CoreMatchers.<View>instanceOf(CardView.class),
                hasDescendant(withText("Done")),
                hasDescendant(withText("TaskProvider")),
                hasDescendant(withText("Closed task")))).check(matches(isDisplayed()));

        onView(taskView(0)).perform(click());
        intended(allOf(
                hasComponent(component(ViewTaskActivity.class)),
                hasExtra(ViewTaskActivity.EXTRA_TARGET_TASK, closedTask)));
    }

    /**
     * View empty task lists.
     * Should: show "Nothing here"
     */
    @Test
    public void testEmptyViews() {
        getSession().setUser(otherUser);
        launchActivity();

        onView(withId(R.id.navigation_posts)).perform(click());

        onView(tabWithText("Requested")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));

        onView(tabWithText("Assigned")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));

        onView(tabWithText("Closed")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));

        onView(withId(R.id.navigation_tasks)).perform(click());

        onView(tabWithText("Assigned")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));

        onView(tabWithText("Bidded")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));

        onView(tabWithText("Completed")).perform(click());
        onView(isRoot()).check(matches(hasDescendant(allOf(withText("Nothing here"), isDisplayed()))));
    }

    /**
     * Go to posting a new task.
     * Should: show a New Task button in the requester view
     *         open a new task on click
     */
    @Test
    public void testStartNewTask() {
        launchAsRequester();
        onView(withId(R.id.navigation_posts)).perform(click());

        onView(withId(R.id.overviewAddButton)).check(matches(isDisplayed()));
        onView(withId(R.id.overviewAddButton)).perform(click());

        intended(hasComponent(component(EditTaskActivity.class)));
    }

    /**
     * Search for tasks.
     * Should: display search results in a list
     *         view the result on click
     */
    @Test
    public void testSearchView() {
        launchAsProvider();
        onView(withId(R.id.navigation_search)).perform(click());

        getInnerClient().mockNextKeywordSearch(unbiddedTask, biddedTask, acceptedTask, closedTask);

        onView(withId(R.id.searchBar)).perform(typeText("Search"), pressImeActionButton());
        closeSoftKeyboard();

        onView(taskView(0)).check(matches(allOf(
                hasDescendant(withText("Requested")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Unbidded task")))));
        onView(taskView(1)).check(matches(allOf(
                hasDescendant(withText("$10.00")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Bidded task")))));
        onView(taskView(2)).check(matches(allOf(
                hasDescendant(withText("Assigned")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Accepted task")))));
        onView(taskView(3)).check(matches(allOf(
                hasDescendant(withText("Done")),
                hasDescendant(withText("TaskRequester")),
                hasDescendant(withText("Closed task")))));

        onView(taskView(0)).perform(click());
        intended(allOf(
                hasComponent(component(ViewTaskActivity.class)),
                hasExtra(ViewTaskActivity.EXTRA_TARGET_TASK, unbiddedTask)));
    }

    /**
     * Go to own profile.
     * Should: open ViewProfileActivity
     */
    @Test
    public void testGoToProfile() {
        getSession().setUser(otherUser);
        launchActivity();

        onView(allOf(withId(R.id.overviewSelfView), instanceOf(UserView.class), isDisplayed())).perform(click());

        onView(withText("View profile")).check(matches(isDisplayed()));
        onView(withText("View profile")).perform(click());

        intended(allOf(
                hasComponent(component(ViewProfileActivity.class)),
                hasExtra(ViewProfileActivity.USER_EXTRA, otherUser)));
    }

    /**
     * Click Logout.
     * Should: log out
     */
    @Test
    public void testLogout() {
        getSession().setUser(otherUser);
        launchActivity();

        onView(allOf(withId(R.id.overviewSelfView), instanceOf(UserView.class), isDisplayed())).perform(click());

        onView(withText("Logout")).check(matches(isDisplayed()));
        onView(withText("Logout")).perform(click());

        assertActivityFinished();
        assertNull(getSession().getUser());
    }
}
