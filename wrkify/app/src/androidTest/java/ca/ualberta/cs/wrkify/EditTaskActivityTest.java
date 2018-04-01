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
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Tests for EditTaskActivity.
 */
@IntentTest
public class EditTaskActivityTest extends AbstractIntentTest<EditTaskActivity> {
    /**
     * Matches a widget in a CheckListItemView that is the Nth item
     * in the CheckListView.
     */
    private static Matcher<View> inNthEditView(int index) {
        return withParent(withParent(withParentIndex(index)));
    }

    private User user;
    private Task task;

    @Override
    protected Class<EditTaskActivity> getActivityClass() {
        return EditTaskActivity.class;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Nullable
    @Override
    protected User getInitialSessionUser() {
        return user;
    }

    @Override
    protected void createMockData(MockRemoteClient client) {
        user = client.create(User.class, "Requester", "requester@example.com", "2048397183");
        task = client.create(Task.class, "Initial task title", user, "Initial task description");
    }

    private void assertChecklistHidden() {
        onView(withId(R.id.editTaskButtonChecklistNew)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.editTaskButtonChecklistAdd)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.editTaskChecklist)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    private void assertChecklistShown() {
        onView(withId(R.id.editTaskButtonChecklistNew)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.editTaskButtonChecklistAdd)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.editTaskChecklist)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    private void launchActivityWithTask() {
        Intent intent = new Intent();
        intent.putExtra(EditTaskActivity.EXTRA_EXISTING_TASK, task);
        launchActivity(intent);
    }

    /**
     * Create a new task.
     * Should: show empty fields
     *         not show a delete option
     *         allow editing title, description
     *         return a task with the new values
     */
    @Test
    public void testNewTask() {
        launchActivity();

        onView(withId(R.id.menuItemDeleteTask)).check(doesNotExist());

        onView(withId(R.id.editTaskTitleField)).check(matches(withText("")));
        onView(withId(R.id.editTaskDescriptionField)).check(matches(withText("")));
        assertChecklistHidden();

        onView(withId(R.id.editTaskTitleField)).perform(typeText("New task title"));
        onView(withId(R.id.editTaskDescriptionField)).perform( typeText("New task description"));

        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityFinished();
        assertEquals(EditTaskActivity.RESULT_TASK_CREATED, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(EditTaskActivity.EXTRA_RETURNED_TASK);
        assertEquals("New task title", task.getTitle());
        assertEquals("New task description", task.getDescription());

        try {
            assertEquals(user, task.getRemoteRequester(getClient()));
            assertNull(task.getRemoteProvider(getClient()));
        } catch (IOException e) {
            fail();
        }

        assertEquals(0, task.getBidList().size());
        assertEquals(0, task.getCheckList().itemCount());
    }

    /**
     * Create a new task with a checklist.
     * Should: allow creating a checklist
     *         allow adding items to the checklist
     *         allow editing items in the checklist
     *         allow removing items from the checklist
     *         hide the checklist if it has no items
     *         save the checklist to the task
     */
    @Test
    public void testNewTaskWithChecklist() {
        launchActivity();
        assertChecklistHidden();

        onView(withId(R.id.editTaskButtonChecklistNew)).perform(click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(0)))
                .perform(scrollTo(), typeText("First checklist item"));

        onView(withId(R.id.editTaskButtonChecklistAdd)).perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(1)))
                .perform(scrollTo(), typeText("Second checklist item"));

        onView(allOf(withId(R.id.checkitemEditButtonDelete), inNthEditView(0)))
                .perform(scrollTo(), click());
        assertChecklistShown();
        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(0)))
                .check(matches(withText("Second checklist item")));

        onView(allOf(withId(R.id.checkitemEditButtonDelete), inNthEditView(0)))
                .perform(scrollTo(), click());
        assertChecklistHidden();

        onView(withId(R.id.editTaskButtonChecklistNew)).perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(0)))
                .perform(scrollTo(), typeText("New first checklist item"));

        onView(withId(R.id.editTaskButtonChecklistAdd)).perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(1)))
                .perform(scrollTo(), typeText("New second checklist item"));

        onView(withId(R.id.editTaskButtonChecklistAdd)).perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(2)))
                .perform(scrollTo(), typeText("New third checklist item"));

        onView(allOf(withId(R.id.checkitemEditButtonDelete), inNthEditView(1)))
                .perform(scrollTo(), click());
        assertChecklistShown();

        onView(withId(R.id.editTaskTitleField)).perform(scrollTo(), typeText("Task with checklist"));
        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityFinished();
        assertEquals(EditTaskActivity.RESULT_TASK_CREATED, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(EditTaskActivity.EXTRA_RETURNED_TASK);
        assertEquals(2, task.getCheckList().itemCount());
        assertEquals("New first checklist item", task.getCheckList().getItem(0).getDescription());
        assertEquals(false, task.getCheckList().getItem(0).getStatus());
        assertEquals("New third checklist item", task.getCheckList().getItem(1).getDescription());
        assertEquals(false, task.getCheckList().getItem(1).getStatus());
    }

    /**
     * Edit an existing task.
     * Should: display the current task details
     *         allow editing the task details
     *         return the edited task
     */
    @Test
    public void testEditExistingTask() {
        launchActivityWithTask();

        onView(withId(R.id.editTaskTitleField)).check(matches(withText("Initial task title")));
        onView(withId(R.id.editTaskDescriptionField)).check(matches(withText("Initial task description")));
        assertChecklistHidden();

        onView(withId(R.id.editTaskTitleField)).perform(clearText(), typeText("Edited task title"));
        onView(withId(R.id.editTaskDescriptionField)).perform(clearText(), typeText("Edited task description"));

        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityFinished();
        assertEquals(EditTaskActivity.RESULT_OK, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(EditTaskActivity.EXTRA_RETURNED_TASK);
        assertEquals("Edited task title", task.getTitle());
        assertEquals("Edited task description", task.getDescription());
        assertEquals(0, task.getBidList().size());
        assertEquals(0, task.getCheckList().itemCount());
    }

    /**
     * Delete a task.
     * Should: show the delete option
     *         return RESULT_TASK_DELETED
     *
     * TODO This doesn't currently work since accessing the app bar menu doesn't seem to work normally.
     */
    @Test
    public void testDeleteTask() {
        launchActivityWithTask();

        openActionBarOverflowOrOptionsMenu(getActivity());
        onData(withId(R.id.menuItemDeleteTask)).check(matches(isDisplayed()));
        onData(withId(R.id.menuItemDeleteTask)).perform(click());

        assertActivityFinished();
        assertEquals(EditTaskActivity.RESULT_TASK_DELETED, getActivityResultCode());
    }

    /**
     * Create a task with no title.
     * Should: fail
     */
    @Test
    public void testNewTaskInvalidTitle() {
        launchActivityWithTask();

        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityNotFinished();
    }

    /**
     * Save an edited task with no title.
     * Should: fail
     */
    @Test
    public void testEditTaskInvalidTitle() {
        launchActivityWithTask();

        onView(withId(R.id.editTaskTitleField)).perform(clearText());
        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityNotFinished();
    }

    /**
     * Edit a task with a checklist.
     * Should: show the existing checklist
     *         allow adding an item
     *         allow deleting an item
     *         allow editing an item
     */
    @Test
    public void testEditTaskWithChecklist() {
        task.getCheckList().addItem("Initial first item", false);
        task.getCheckList().addItem("Initial second item", true);

        launchActivityWithTask();

        assertChecklistShown();
        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(0)))
                .check(matches(withText("Initial first item")));
        onView(allOf(withId(R.id.checkitemEditToggle), inNthEditView(0)))
                .check(matches(isNotChecked()));
        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(1)))
                .check(matches(withText("Initial second item")));
        onView(allOf(withId(R.id.checkitemEditToggle), inNthEditView(1)))
                .check(matches(isChecked()));

        closeSoftKeyboard();
        onView(withId(R.id.editTaskButtonChecklistAdd)).perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(2)))
                .perform(scrollTo(), typeText("New third item"));
        onView(allOf(withId(R.id.checkitemEditButtonDelete), inNthEditView(0)))
                .perform(scrollTo(), click());
        assertChecklistShown();

        onView(allOf(withId(R.id.checkitemEditDescription), inNthEditView(0)))
                .perform(scrollTo(), clearText(), typeText("Edited first item"));

        onView(withId(R.id.menuItemSaveTask)).perform(click());

        assertActivityFinished();
        assertEquals(EditTaskActivity.RESULT_OK, getActivityResultCode());

        Task task = (Task) getActivityResultExtra(EditTaskActivity.EXTRA_RETURNED_TASK);
        assertEquals(2, task.getCheckList().itemCount());
        assertEquals("Edited first item", task.getCheckList().getItem(0).getDescription());
        assertEquals(true, task.getCheckList().getItem(0).getStatus());
        assertEquals("New third item", task.getCheckList().getItem(1).getDescription());
        assertEquals(false, task.getCheckList().getItem(1).getStatus());
    }
}
