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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by peter on 03/04/18.
 */

public class TaskTransactionsTest {
    private static RemoteClient rc;

    private TransactionManager tm;
    private User u1, u2;
    private Task t1, t2, t3, t4;


    @BeforeClass
    public static void setup() {
        rc = new MockRemoteClient();
    }

    @Before
    public void setupTasksUsers() {
        this.tm = new TransactionManager();
        this.u1 = rc.create(User.class, "user1", "user1@example.com", "(780) 555-5555");
        this.u2 = rc.create(User.class, "user2", "user2@example.com", "(780) 666-6666");
        this.t1 = rc.create(Task.class, "Task 1", this.u1 , "description");
        this.t2 = rc.create(Task.class, "Task 2", this.u1 , "description");
        this.t3 = rc.create(Task.class, "Task 3", this.u2 , "description");
        this.t4 = rc.create(Task.class, "Task 3", this.u2 , "description");
    }

    @After
    public void deleteTasksUsers() {
        rc.delete(this.u1);
        rc.delete(this.u2);
        rc.delete(this.t1);
        rc.delete(this.t2);
        rc.delete(this.t3);
        rc.delete(this.t4);
    }

    @Test
    public void testTaskTitle() {
        TaskTitleTransaction ttt = new TaskTitleTransaction(this.t1, "Task 1 Updated");

        Boolean status;
        status = ttt.applyTo(this.t1);
        assertTrue(status);
        assertEquals("Task 1 Updated", this.t1.getTitle());

        this.t1.addBid(new Bid(new Price(5.0), this.u1));

        status = ttt.applyTo(this.t1);
        assertFalse(status);
    }

    @Test
    public void testTaskDescription() {
        TaskDescriptionTransaction tdt = new TaskDescriptionTransaction(this.t1, "description Updated");

        Boolean status;
        status = tdt.applyTo(this.t1);
        assertTrue(status);
        assertEquals("description Updated", this.t1.getDescription());

        this.t1.addBid(new Bid(new Price(5.0), this.u1));

        status = tdt.applyTo(this.t1);
        assertFalse(status);
    }

    @Test
    public void testLocation() {
        // we can't use real locations in unit tests, so we use null.
        TaskLocationTransaction tlt = new TaskLocationTransaction(this.t1, null);

        Boolean status;

        status = tlt.applyTo(this.t1);
        assertTrue(status);

        this.t1.addBid(new Bid(new Price(5.0), this.u1));

        status = tlt.applyTo(this.t1);
        assertFalse(status);
    }

    @Test
    public void testCheckList() {
        CheckList cl = new CheckList();
        StateChangeTransaction t = new TaskCheckListTransaction(this.t1, cl);

        Boolean status = t.apply(this.t1);
        assertTrue(status);
        assertEquals(cl, this.t1.getCheckList());
    }

    @Test
    public void testAddBid() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        TaskAddOrReplaceBidTransaction t = new TaskAddOrReplaceBidTransaction(this.t1, b1);

        Boolean status;

        status = t.apply(this.t1);
        assertTrue(status);
        assertEquals(1, this.t1.getBidList().size());

        this.t1.acceptBid(b1);

        status = t.apply(this.t1);
        assertFalse(status);
        assertEquals(1, this.t1.getBidList().size());
    }

    @Test
    public void TestCancelBid() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        TaskAddOrReplaceBidTransaction t = new TaskAddOrReplaceBidTransaction(this.t1, b1);
        TaskCancelBidTransaction t2 = new TaskCancelBidTransaction(this.t1, b1);

        t2.apply(this.t1);
        t.apply(this.t1);
        assertEquals(1, this.t1.getBidList().size());

        t2.apply(this.t1);
        assertEquals(0, this.t1.getBidList().size());
    }

    @Test
    public void testAddImage() {
        StateChangeTransaction t = new TaskAddImageTransaction(this.t1, null);

        Boolean status = t.applyTo(this.t1);
        assertTrue(status);
        assertEquals(1, this.t1.getImageList().size());

        this.t1.addBid(new Bid(new Price(5.0), this.u1));

        status = t.applyTo(this.t1);
        assertFalse(status);
        assertEquals(1, this.t1.getImageList().size());
    }

    @Test
    public void testDelImage() {
        this.t1.addImage(null);
        StateChangeTransaction t = new TaskDelImageTransaction(this.t1, null);

        assertEquals(1, this.t1.getImageList().size());
        Boolean status = t.applyTo(this.t1);
        assertTrue(status);
        assertEquals(0, this.t1.getImageList().size());
    }

    @Test
    public void TestAcceptBid() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        TaskAcceptBidTransaction t = new TaskAcceptBidTransaction(this.t1, b1);

        Boolean status;

        status = t.apply(this.t1);
        assertTrue(status);
        assertEquals(TaskStatus.ASSIGNED, this.t1.getStatus());

        status = t.apply(this.t1);
        assertFalse(status);
    }

    @Test
    public void TestUnassign() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        this.t1.acceptBid(b1);

        TaskUnassignTransaction t = new TaskUnassignTransaction(this.t1);

        Boolean status;
        status = t.applyTo(this.t1);
        assertTrue(status);
        assertEquals(TaskStatus.BIDDED, this.t1.getStatus());

        status = t.applyTo(this.t1);
        assertFalse(status);
    }

    @Test
    public void TestComplet() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        this.t1.acceptBid(b1);

        TaskCompleteTransaction t = new TaskCompleteTransaction(this.t1);

        Boolean status;
        status = t.applyTo(this.t1);
        assertTrue(status);
        assertEquals(TaskStatus.DONE, this.t1.getStatus());

        status = t.applyTo(this.t1);
        assertFalse(status);
    }
}
