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
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by peter on 18/03/18.
 */

public class SearcherTest {

    private User user1;
    private User user2;
    private User user3;

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    RemoteClient rc;

    @Before
    public void createData() {
        rc = WrkifyClient.getInstance();

        user1 = rc.create(User.class, "peter", "peter@a.com", "1");
        user2 = rc.create(User.class, "taylor", "taylor@a.com", "2");
        user3 = rc.create(User.class, "john", "john@a.com", "3");

        task1 = rc.create(Task.class, "task 1", user1, "");
        task2 = rc.create(Task.class, "task 2", user1, "");
        task3 = rc.create(Task.class, "task 3", user2, "");
        task4 = rc.create(Task.class, "task 4", user2, "");

        // give elasticsearch time to index
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    @After
    public void removeData() {
        rc.delete(user1);
        rc.delete(user2);
        rc.delete(user3);

        rc.delete(task1);
        rc.delete(task2);
        rc.delete(task3);
        rc.delete(task4);
    }

    @Test
    public void testFindTasksByRequester() {

        List<Task> res1;
        try {
            res1 = Searcher.findTasksByRequester(rc, user1);
        } catch (IOException e) {
            fail();
            return;
        }

        assertEquals(2, res1.size());
        assertTrue(res1.contains(task1));
        assertTrue(res1.contains(task2));

        List<Task> res3;
        try {
            res3 = Searcher.findTasksByRequester(rc, user3);
        } catch (IOException e) {
            fail();
            return;
        }

        assertEquals(0, res3.size());
    }
}
