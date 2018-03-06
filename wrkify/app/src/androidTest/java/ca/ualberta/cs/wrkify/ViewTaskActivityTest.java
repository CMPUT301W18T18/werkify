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

import static org.junit.Assert.*;

public class ViewTaskActivityTest {
    @Rule
    public ActivityTestRule<ViewTaskActivity> activityTestRule = new ActivityTestRule<>(
            ViewTaskActivity.class, false, false);



    @Test
    public void testViewTaskActivity() {
        Intent intent = new Intent();
        ConcreteTask task = new ConcreteTask();
        task.setProvider(new ConcreteUser("Username", "test@email", "1234567890"));
        task.setTitle("Test task title");
        task.setDescription("Test task description");
        task.setStatus(TaskStatus.REQUESTED);
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_USER, task);
        activityTestRule.launchActivity(intent);
    }
}