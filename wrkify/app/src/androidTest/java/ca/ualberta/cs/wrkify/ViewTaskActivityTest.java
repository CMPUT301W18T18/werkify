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
        task.setRequester(new ConcreteUser("Username", "test@email", "1234567890"));
        task.setTitle("Example task");
        task.setDescription("A task can have a description, and that description can be fairly" +
                "long. It may or may not be likely for an actual description to be long enough" +
                "to cause the view to scroll, but it is theoretically allowed, probably. Adding" +
                "additional content like images and locations will probably make long tasks more" +
                "common, but even with just a sufficiently long description, it could overflow." +
                "A task can have a description, and that description can be fairly" +
                "long. It may or may not be likely for an actual description to be long enough" +
                "to cause the view to scroll, but it is theoretically allowed, probably. Adding" +
                "additional content like images and locations will probably make long tasks more" +
                "common, but even with just a sufficiently long description, it could overflow." +
                "A task can have a description, and that description can be fairly" +
                "long. It may or may not be likely for an actual description to be long enough" +
                "to cause the view to scroll, but it is theoretically allowed, probably. Adding" +
                "additional content like images and locations will probably make long tasks more" +
                "common, but even with just a sufficiently long description, it could overflow.");
        task.setStatus(TaskStatus.REQUESTED);
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);
        activityTestRule.launchActivity(intent);
    }
}