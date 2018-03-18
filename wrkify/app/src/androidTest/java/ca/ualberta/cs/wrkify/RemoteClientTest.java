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

import static org.junit.Assert.*;

/**
 * Created by peter on 17/03/18.
 */

public class RemoteClientTest {
    @Test
    public void testInternet() {
        String command = "ping -c 1 google.com";
        try {
            boolean success = (Runtime.getRuntime().exec(command).waitFor() == 0);
            assertTrue(success);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void TestCreateGetDelete() {
        RemoteClient rc = wrkifyClient.getInstance();

        ConcreteTestObject cto = rc.create(ConcreteTestObject.class, "p1", "p2", 1);

        assertEquals("p1", cto.param1);
        assertEquals("p2", cto.param2);

        ConcreteTestObject cto2;
        try {
             cto2 = rc.download(cto.getId(), ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals("p1", cto2.param1);
        assertEquals("p2", cto2.param2);

        rc.delete(cto);

        try {
            cto2 = rc.download(cto.getId(), ConcreteTestObject.class);
            assertEquals(cto2,null);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
