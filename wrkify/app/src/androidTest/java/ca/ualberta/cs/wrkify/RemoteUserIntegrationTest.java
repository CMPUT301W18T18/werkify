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
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by peter on 18/03/18.
 */

public class RemoteUserIntegrationTest {
    @Test
    public void testCreateGetDelete() {
        RemoteClient rc = new ElasticClient(WrkifyClient.URL, "cmput301w18t18-test");

        User cto = rc.create(User.class, "peter", "peter@example.com", "7805555555");

        assertEquals("peter", cto.getUsername());
        assertEquals("peter@example.com", cto.getEmail());
        assertEquals("7805555555", cto.getPhoneNumber());

        User cto2;
        try {
            cto2 = rc.download(cto.getId(), User.class);
        } catch (IOException e) {
            fail();
            return;
        }

        assertEquals("peter", cto2.getUsername());
        assertEquals("peter@example.com", cto2.getEmail());
        assertEquals("7805555555", cto2.getPhoneNumber());

        rc.delete(cto);

        try {
            cto2 = rc.download(cto.getId(), User.class);
            assertEquals(cto2,null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSearch() {
        ElasticClient rc = new ElasticClient(WrkifyClient.URL, "cmput301w18t18-test");

        User use1 = rc.create(User.class, "john", "john@example.com", "(780) 666-6666");
        User use2 = rc.create(User.class, "peter", "peter@example.com", "(780) 555-5555");

        assertNotEquals(null, use1);
        assertNotEquals(null, use2);


        List<User> arr;


        try {
            // give elasticsearch time to index
            Thread.sleep(1000);
            arr = rc.executeQuery(
                    "{\"query\": {\"match\": {\"username\": \"peter\"}}}",
                    User.class
            );
        } catch (Exception e) {
            fail();
            return;
        }

        User res = arr.get(0);

        assertNotEquals(null, res);

        assertEquals(res.getUsername(), "peter");
        assertEquals(use2.getId(), res.getId());

        rc.delete(use1);
        rc.delete(use2);
    }
}
