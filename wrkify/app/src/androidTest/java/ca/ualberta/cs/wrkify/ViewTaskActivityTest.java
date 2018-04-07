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
import android.view.View;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * Tests for ViewTaskActivity.
 */
@IntentTest
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

        unbiddedTask.getCheckList().addItem("Unchecked", false);
        unbiddedTask.getCheckList().addItem("Checked", true);
        biddedTask.getCheckList().addItem("Unchecked", false);
        biddedTask.getCheckList().addItem("Checked", true);
        acceptedTask.getCheckList().addItem("Unchecked", false);
        acceptedTask.getCheckList().addItem("Checked", true);
        closedTask.getCheckList().addItem("Unchecked", false);
        closedTask.getCheckList().addItem("Checked", true);

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
        onView(withId(R.id.taskViewBottomSheetContent)).check(doesNotExist());
    }

    private void assertBottomSheetExpanded() {
        onView(withId(R.id.taskViewBottomSheetContent)).check(matches(hasDescendant(isDisplayed())));
    }

    private Matcher<View> isChecklistToggle(int index) {
        return allOf(
                withId(R.id.checkitemToggle),
                isDescendantOfA(allOf(
                        CoreMatchers.<View>instanceOf(CheckListView.CheckListItemView.class),
                        withParentIndex(index)
                )));
    }

    private void assertChecklistStatic() {
        onView(isChecklistToggle(0)).check(matches(allOf(not(isClickable()), isNotChecked())));
        onView(isChecklistToggle(1)).check(matches(allOf(not(isClickable()), isChecked())));
    }

    private void assertChecklistInteractive() {
        onView(isChecklistToggle(0)).check(matches(allOf(isClickable(), isNotChecked())));
        onView(isChecklistToggle(1)).check(matches(allOf(isClickable(), isChecked())));
    }

    private void assertHasStatus(String status, String detail, String rightStatus, String rightDetail) {
        onView(withId(R.id.taskViewBottomSheetStatus)).check(matches(withText(containsString(status))));
        onView(withId(R.id.taskViewBottomSheetDetail)).check(matches(withText(containsString(detail))));
        onView(withId(R.id.taskViewBottomSheetRightStatus)).check(matches(withText(containsString(rightStatus))));
        onView(withId(R.id.taskViewBottomSheetRightDetail)).check(matches(withText(containsString(rightDetail))));
    }

    private void placeBid(String bid) {
        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetExpanded();

        onView(withId(R.id.taskViewBottomSheetBidField)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetBidField)).perform(typeText(bid));

        onView(withId(R.id.taskViewBottomSheetButtonBid)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetButtonBid)).perform(click());
        onView(withText("Bid")).perform(click());

        assertBottomSheetCollapsed();
    }

    /**
     * There are no bids on the task, and the session user is not the requester.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status OPEN
     *         show "No bids"
     *         allow placing a bid
     * Requires deactivating animations.
     */
    @Test
    public void testViewTaskActivity() {
        launchActivityWith(otherUser, unbiddedTask);

        checkTaskDetails(unbiddedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Open", "No bids", "", "");

        placeBid("12.00");

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
     * Place an invalid bid on an unbidded task.
     * Should: fail
     */
    @Test
    public void testInvalidBid() {
        launchActivityWith(otherUser, unbiddedTask);

        checkTaskDetails(unbiddedTask);
        assertNoEditButton();
        assertBottomSheetCollapsed();
        assertHasStatus("Open", "No bids", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetExpanded();

        onView(withId(R.id.taskViewBottomSheetBidField)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetBidField)).perform(typeText(""));

        onView(withId(R.id.taskViewBottomSheetButtonBid)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetButtonBid)).perform(click());
        onView(withText("Bid")).check(matches(not(isDisplayed())));

        closeSoftKeyboard();

        pressBack();
        assertBottomSheetCollapsed();

        pressBackUnconditionally();
        assertActivityFinished();

        try {
            Task editedTask = getClient().download(unbiddedTask.getId(), Task.class);

            assertEquals(TaskStatus.REQUESTED, editedTask.getStatus());
            assertEquals(0, editedTask.getBidList().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * There are bids on the task, but the session user has not bidded.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status OPEN
     *         show the number of bids so far
     *         show the current lowest bid
     *         allow placing a bid
     * Requires deactivating animations.
     */
    @Test
    public void testViewBiddedTask() {
        launchActivityWith(otherUser, biddedTask);

        checkTaskDetails(biddedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Open", "2 bids", "$10.00", "");

        placeBid("8.00");

        pressBackUnconditionally();
        assertActivityFinished();

        try {
            Task editedTask = getClient().download(biddedTask.getId(), Task.class);

            assertEquals(TaskStatus.BIDDED, editedTask.getStatus());
            assertEquals(3, editedTask.getBidList().size());
            assertEquals(new Price("8.00"), editedTask.getBidList().get(0).getValue());

            assertEquals(otherUser, editedTask.getBidList().get(0).getRemoteBidder(getClient()));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * The session user has bidded on the task, and does not have the current highest bid.
     * Should: show basic task details
     *         not display an edit button
     *         now allow editing the checklist
     *         show status BIDDED
     *         show the number of bids so far
     *         show the current lowest bid
     *         show your bid
     *         allow replacing your bid
     */
    @Test
    public void testViewSelfBiddedTask() {
        // TODO This isn't implemented, so not testing
        fail();
    }

    /**
     * The task being viewed was requested by the session user.
     * Should: show basic task details
     *         display a working edit button
     *         not allow editing the checklist
     *         have no bottom controls
     *         show status REQUESTED
     *         show "No bids"
     */
    @Test
    public void testViewOwnUnbiddedTask() {
        launchActivityWith(requester, unbiddedTask);

        checkTaskDetails(unbiddedTask);
        assertHasEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Requested", "No bids", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetCollapsed();

        onView(withId(R.id.taskViewButtonEdit)).perform(click());
        intended(allOf(
                hasComponent(component(EditTaskActivity.class)),
                hasExtra(EditTaskActivity.EXTRA_EXISTING_TASK, unbiddedTask)));
    }

    /**
     * The session user is the requester, and the task has bids.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status BIDDED
     *         go to show bids on bottom sheet click
     */
    @Test
    public void testViewOwnBiddedTask() {
        launchActivityWith(requester, biddedTask);

        checkTaskDetails(biddedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Bidded", "2 bids", "$10.00", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        intended(allOf(
                hasComponent(component(ViewBidsActivity.class)),
                hasExtra(ViewBidsActivity.EXTRA_VIEWBIDS_TASK, biddedTask)));
    }

    /**
     * The task is assigned, but the session user is neither the requester nor the assignee.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status ASSIGNED
     *         show the assignee
     *         have no bottom controls
     */
    @Test
    public void testViewAssignedTask() {
        launchActivityWith(otherUser, acceptedTask);

        checkTaskDetails(acceptedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Assigned", "TaskProvider", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetCollapsed();
    }

    /**
     * The task is assigned to the session user.
     * Should: show basic task details
     *         not display an edit button
     *         allow editing the checklist
     *         show status ASSIGNED
     *         show the assignee
     *         have no bottom controls
     * TODO should this show something in the bottom sheet differently from an unassociated user?
     */
    @Test
    public void testViewAssignedToSelfTask() {
        launchActivityWith(provider, acceptedTask);

        checkTaskDetails(acceptedTask);
        assertNoEditButton();
        assertChecklistInteractive();
        assertBottomSheetCollapsed();
        assertHasStatus("Assigned", "TaskProvider", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetCollapsed();

        onView(isChecklistToggle(0)).perform(click());
        onView(withText("Mark completed")).perform(click());
        onView(isChecklistToggle(0)).check(matches(isChecked()));

        onView(isChecklistToggle(1)).perform(click());
        onView(withText("Mark not completed")).perform(click());
        onView(isChecklistToggle(1)).check(matches(isNotChecked()));

        pressBackUnconditionally();

        try {
            Task editedTask = getClient().download(acceptedTask.getId(), Task.class);
            assertEquals(2, editedTask.getCheckList().itemCount());
            assertEquals(true, editedTask.getCheckList().getItem(0).getStatus());
            assertEquals(false, editedTask.getCheckList().getItem(1).getStatus());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * The task is requested by the session user, and is assigned.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status ASSIGNED
     *         show the assignee
     */
    @Test
    public void testViewOwnAssignedTask() {
        launchActivityWith(requester, acceptedTask);

        checkTaskDetails(acceptedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Assigned", "TaskProvider", "$10.00", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetExpanded();

        onView(withId(R.id.taskViewBottomSheetButtonDeassign)).check(matches(isDisplayed()));
        onView(withId(R.id.taskViewBottomSheetButtonMarkDone)).check(matches(isDisplayed()));
    }

    /**
     * Deassign a task.
     * Should: make the task REQUESTED
     *         remove current bids
     * Requires deactivating animations.
     */
    @Test
    public void testDeassignTask() {
        testViewOwnAssignedTask();

        onView(withId(R.id.taskViewBottomSheetButtonDeassign)).perform(click());
        onView(withText("Deassign")).perform(click());

        assertBottomSheetCollapsed();

        pressBackUnconditionally();

        try {
            Task editedTask = getClient().download(acceptedTask.getId(), Task.class);
            assertEquals(TaskStatus.REQUESTED, editedTask.getStatus());
            assertEquals(0, editedTask.getBidList().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Close a task.
     * Should: make the task CLOSED
     * Requires deactivating animations.
     */
    @Test
    public void testCloseTask() {
        testViewOwnAssignedTask();

        onView(withId(R.id.taskViewBottomSheetButtonMarkDone)).perform(click());
        onView(withText("Mark done")).perform(click());

        assertBottomSheetCollapsed();

        pressBackUnconditionally();

        try {
            Task editedTask = getClient().download(acceptedTask.getId(), Task.class);
            assertEquals(TaskStatus.DONE, editedTask.getStatus());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * The task is closed.
     * Should: show basic task details
     *         not display an edit button
     *         not allow editing the checklist
     *         show status CLOSED
     *         show the assignee
     */
    @Test
    public void testViewCompletedTask() {
        launchActivityWith(provider, closedTask);

        checkTaskDetails(closedTask);
        assertNoEditButton();
        assertChecklistStatic();
        assertBottomSheetCollapsed();
        assertHasStatus("Done", "TaskProvider", "", "");

        onView(withId(R.id.taskViewBottomSheetHeader)).perform(click());
        assertBottomSheetCollapsed();
    }
}