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


import org.junit.Test;

import static junit.framework.Assert.fail;

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

    private void launchAsRequester() {
        getSession().setUser(requester);
        launchActivity();
    }

    private void launchAsProvider() {
        getSession().setUser(provider);
        launchActivity();
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
        fail();
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
        fail();
    }

    /**
     * Go to posting a new task.
     * Should: show a New Task button in the requester view
     *         open a new task on click
     */
    @Test
    public void testStartNewTask() {
        launchAsRequester();
        fail();
    }

    /**
     * Search for tasks.
     * Should: display search results in a list
     *         view the result on click
     */
    @Test
    public void testSearchView() {
        launchAsProvider();
        fail();
    }
}
