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

import java.io.IOException;

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
        this.tm.enqueue(ttt);
        this.tm.flush(rc);
        try {
            this.t1 = rc.download(this.t1.getId(), Task.class);
            assertEquals("Task 1 Updated", this.t1.getTitle());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testTaskDescription() {
        TaskDescriptionTransaction tdt = new TaskDescriptionTransaction(this.t1, "description Updated");
        this.tm.enqueue(tdt);
        this.tm.flush(rc);
        try {
            this.t1 = rc.download(this.t1.getId(), Task.class);
            assertEquals("description Updated", this.t1.getDescription());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testAddBidDescription() {
        Bid b1 = new Bid(new Price(5.0), this.u2);
        TaskAddBidTransaction t = new TaskAddBidTransaction(this.t1, b1);

        Boolean status;

        status = t.application(this.t1);
        assertTrue(status);
        assertEquals(1, this.t1.getBidList().size());

        this.t1.acceptBid(b1);

        status = t.application(this.t1);
        assertFalse(status);
        assertEquals(1, this.t1.getBidList().size());
    }
}
