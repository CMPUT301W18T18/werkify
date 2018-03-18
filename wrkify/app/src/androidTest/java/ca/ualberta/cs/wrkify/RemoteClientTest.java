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
    public void TestEquals() {
        RemoteClient rc = WrkifyClient.getInstance();

        ConcreteTestObject cto = rc.create(ConcreteTestObject.class, "p1", "p2", 1);
        ConcreteTestObject cto1 = rc.create(ConcreteTestObject.class, "p1", "p2", 1);
        ConcreteTestObject cto2;
        try {
             cto2 = rc.download(cto.getId(), ConcreteTestObject.class);
        } catch(IOException e) {
            fail();
            return;
        }

        assertTrue(cto.equals(cto2));
        assertEquals(cto, cto2);
        assertNotEquals(cto, cto1);
        assertNotEquals(cto2, cto1);

        rc.delete(cto);
        rc.delete(cto1);
    }

    @Test
    public void TestCreateGetDelete() {
        RemoteClient rc = WrkifyClient.getInstance();

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
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testSearch() {
        RemoteClient rc = WrkifyClient.getInstance();

        ConcreteTestObject co = rc.create(ConcreteTestObject.class,"t1", "t1", 0);
        ConcreteTestObject co2 = rc.create(ConcreteTestObject.class,"t2", "t1", 0);

        assertNotEquals(null, co);
        assertNotEquals(null, co2);


        List<ConcreteTestObject> arr;


        try {
            // give elasticsearch time to index
            Thread.sleep(1000);
            arr = rc.search(
                    "{\"query\": {\"match\": {\"param1\": \"t2\"}}}",
                    ConcreteTestObject.class
            );
        } catch (Exception e) {
            fail();
            return;
        }

        ConcreteTestObject obj = arr.get(0);

        assertNotEquals(null, obj);

        assertEquals(obj.param1, "t2");
        assertEquals(co2.getId(), obj.getId());

        rc.delete(co);
        rc.delete(co2);
    }
}
