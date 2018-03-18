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

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for ViewTaskActivity.
 * Launches ViewTaskActivity for a variety of task/session user configurations.
 */
public class ViewTaskActivityTest {
    @Rule
    public ActivityTestRule<ViewTaskActivity> activityTestRule = new ActivityTestRule<>(
            ViewTaskActivity.class, false, false);

    private static String aLongDescription =
            "A task can have a description, and that description can be fairly " +
            "long. It may or may not be likely for an actual description to be long enough " +
            "to cause the view to scroll, but it is theoretically allowed, probably. Adding " +
            "additional content like images and locations will probably make long tasks more " +
            "common, but even with just a sufficiently long description, it could overflow. " +
            "A task can have a description, and that description can be fairly " +
            "long. It may or may not be likely for an actual description to be long enough " +
            "to cause the view to scroll, but it is theoretically allowed, probably. Adding " +
            "additional content like images and locations will probably make long tasks more " +
            "common, but even with just a sufficiently long description, it could overflow. " +
            "A task can have a description, and that description can be fairly " +
            "long. It may or may not be likely for an actual description to be long enough " +
            "to cause the view to scroll, but it is theoretically allowed, probably. Adding " +
            "additional content like images and locations will probably make long tasks more " +
            "common, but even with just a sufficiently long description, it could overflow. " +
            "A task can have a description, and that description can be fairly " +
            "long. It may or may not be likely for an actual description to be long enough " +
            "to cause the view to scroll, but it is theoretically allowed, probably. Adding " +
            "additional content like images and locations will probably make long tasks more " +
            "common, but even with just a sufficiently long description, it could overflow. ";

    private static ConcreteUser exampleUser1 = new ConcreteUser(
            "Username1", "test@example.com", "5555550001");
    private static ConcreteUser exampleUser2 = new ConcreteUser(
            "Username2", "test2@example.com", "5555550002");
    private static ConcreteUser exampleUser3 = new ConcreteUser(
            "Username3", "test3@example.com", "5555550003");

    private void startViewTaskActivityWith(Task task, User sessionUser) {
        Intent intent = new Intent();
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);
        intent.putExtra(ViewTaskActivity.EXTRA_SESSION_USER, sessionUser);
        activityTestRule.launchActivity(intent);
    }

    /**
     * ViewTaskActivity test:
     * there are no bids on the task, and the session user is not
     * the requester.
     */
    @Test
    public void testViewTaskActivity() {
        ConcreteTask task = new ConcreteTask("Example task with no bids", exampleUser1, aLongDescription);
        startViewTaskActivityWith(task, exampleUser2);
    }

    /**
     * ViewTaskActivity test:
     * there are bids on the task, but the session user has not bidded.
     */
    @Test
    public void testViewBiddedTask() {
        ConcreteTask task = new ConcreteTask("Example task with bids", exampleUser1, aLongDescription);
        task.addBid(new Bid(20.00, exampleUser2));
        task.addBid(new Bid(30.00, exampleUser2));
        startViewTaskActivityWith(task, exampleUser3);
    }

    /**
     * ViewTaskActivity test:
     * the session user has bidded on the task, and does not have
     * the current highest bid.
     */
    @Test
    public void testViewSelfBiddedTask() {
        // TODO This doesn't currently work any differently from a normal bidded task
        ConcreteTask task = new ConcreteTask("Example task that session user has bidded on", exampleUser1, aLongDescription);
        task.addBid(new Bid(20.00, exampleUser2));
        task.addBid(new Bid(30.00, exampleUser3));
        startViewTaskActivityWith(task, exampleUser3);
    }

    /**
     * ViewTaskActivity test:
     * the task being viewed was requested by the session user.
     */
    @Test
    public void testViewOwnUnbiddedTask() {
        ConcreteTask task = new ConcreteTask("Example requested task", exampleUser1, aLongDescription);
        startViewTaskActivityWith(task, exampleUser1);
    }

    /**
     * ViewTaskActivity test:
     * the session user is the requester, and the task has bids.
     */
    @Test
    public void testViewOwnBiddedTask() {
        ConcreteTask task = new ConcreteTask("Example bidded requested task", exampleUser1, aLongDescription);
        task.addBid(new Bid(15.15, exampleUser2));
        task.addBid(new Bid(20.25, exampleUser3));
        startViewTaskActivityWith(task, exampleUser1);
    }

    /**
     * ViewTaskActivity test:
     * the task is assigned, but the session user is neither the
     * requester nor the assignee.
     */
    @Test
    public void testViewAssignedTask() {
        ConcreteTask task = new ConcreteTask("Example assigned task", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser3);
    }

    /**
     * ViewTaskActivity test:
     * the task is assigned to the session user
     */
    @Test
    public void testViewAssignedToSelfTask() {
        // TODO This doesn't currently work any differently from a task assigned to another user
        ConcreteTask task = new ConcreteTask("Example task assigned to self", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser2);
    }

    /**
     * ViewTaskActivity test:
     * the task is requested by the session user,
     * and is assigned
     */
    @Test
    public void testViewOwnAssignedTask() {
        ConcreteTask task = new ConcreteTask("Example task assigned to other", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser1);
    }

    /**
     * ViewTaskActivity test:
     * the task is closed
     */
    @Test
    public void testViewCompletedTask() {
        ConcreteTask task = new ConcreteTask("Closed task", exampleUser1, aLongDescription);
        task.setProvider(exampleUser2);
        startViewTaskActivityWith(task, exampleUser3);
    }
}