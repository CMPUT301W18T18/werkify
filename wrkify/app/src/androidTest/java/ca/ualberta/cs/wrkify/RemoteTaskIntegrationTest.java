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

import java.io.IOException;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by peter on 18/03/18.
 */

public class RemoteTaskIntegrationTest {
    @Test
    public void testGetRequester() {
        RemoteClient rc = new ElasticClient(WrkifyClient.URL, "cmput301w18t18-test");

        User use = rc.create(User.class, "peter", "peter@example.com", "(780) 555-5555");
        Task task = rc.create(Task.class, "mow my lawn", use, "mow my lawn");

        assertNotEquals(null, use);
        assertNotEquals(null, task);

        User use2;
        try {
            use2 = task.getRemoteRequester(rc);
        } catch (IOException e) {
            fail();
            return;
        }

        assertEquals(use.getId(), use2.getId());
        assertEquals("peter", use2.getUsername());
        assertEquals("peter@example.com", use2.getEmail());
        assertEquals("7805555555", use2.getPhoneNumber());

        rc.delete(use);
        rc.delete(task);
    }
}
