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

import static junit.framework.Assert.assertEquals;

/**
 * Created by peter on 10/03/18.
 */

public class ElasticUserTest {
    @Test
    public void testGetSetUsername() {
        MockElasticClient client = new MockElasticClient();

        ConcreteUser user = new ConcreteUser("user", "user@example.com", "(780) 555-5555");

        ElasticUser elu = new ElasticUser(user, client);

        elu.setUsername("user2");

        ElasticUser elu2 = new ElasticUser(elu.getId(), client);

        assertEquals(elu.getUsername(), "user2");
        assertEquals(elu2.getUsername(), "user2");
    }

    @Test
    public void testGetSetEmail() {
        MockElasticClient client = new MockElasticClient();

        ConcreteUser user = new ConcreteUser("user", "user@example.com", "(780) 555-5555");

        ElasticUser elu = new ElasticUser(user, client);

        elu.setEmail("user2@example.com");

        ElasticUser elu2 = new ElasticUser(elu.getId(), client);

        assertEquals(elu.getEmail(), "user2@example.com");
        assertEquals(elu2.getEmail(), "user2@example.com");
    }

    @Test
    public void testGetSetPhoneNumber() {
        MockElasticClient client = new MockElasticClient();

        ConcreteUser user = new ConcreteUser("user", "user@example.com", "(780) 555-5555");

        ElasticUser elu = new ElasticUser(user, client);

        elu.setPhoneNumber("(780) 666-6666");

        ElasticUser elu2 = new ElasticUser(elu.getId(), client);

        assertEquals(elu.getPhoneNumber(), "7806666666");
        assertEquals(elu2.getPhoneNumber(), "7806666666");
    }
}
