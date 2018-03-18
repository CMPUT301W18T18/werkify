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
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

/**
 * Runs EditTaskActivity
 */
public class EditTaskActivityTest {
    @Rule
    public ActivityTestRule<EditTaskActivity> activityTestRule = new ActivityTestRule<>(
            EditTaskActivity.class, false, false);

    User testUser = new User("user", "email@a.com", "1234567890");

    /**
     * Test creating a new task
     */
    @Test
    public void testEditTaskActivity() {
        Intent intent = new Intent();
        activityTestRule.launchActivity(intent);
    }

    /**
     * Test editing an existing task
     */
    @Test
    public void testEditExistingTaskActivity() {
        Intent intent = new Intent();
        Task task = new Task("Task being edited", testUser, "Description of task");
        intent.putExtra(EditTaskActivity.EXTRA_EXISTING_TASK, task);
        activityTestRule.launchActivity(intent);
    }
}
