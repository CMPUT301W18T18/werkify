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
 * Networked tests for ElasticClient
 *
 * these test have to be instrumented, because jestdroid does not
 * work off of android.
 */

public class ElasticClientTest {
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
    public void testCreateGetDelete() {
        ElasticClient ec = ElasticClient.getInstance();

        String id = ec.create(
                new ConcreteTestObject("t1", "t1", 0),
                ConcreteTestObject.class
        );

        assertNotEquals(id, null);

        ConcreteTestObject ct;
        try {
            ct = ec.get(id, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(ct.param1, "t1");
        assertEquals(ct.param2, "t1");
        assertEquals(ct.param3, 0);

        try {
            ec.delete(id, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
        }

        try {
            ct = ec.get(id, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(ct, null);
    }

    @Test
    public void testUpdate() {
        ElasticClient ec = ElasticClient.getInstance();

        String id = ec.create(
                new ConcreteTestObject("t1", "t1", 0),
                ConcreteTestObject.class
        );

        assertNotEquals(id, null);

        ec.update(
                id,
                new ConcreteTestObject("t2", "t2", 1),
                ConcreteTestObject.class
        );

        ConcreteTestObject ct;
        try {
            ct = ec.get(id, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(ct.param1, "t2");
        assertEquals(ct.param2, "t2");
        assertEquals(ct.param3, 1);

        try {
            ec.delete(id, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testSearch() {
        ElasticClient ec = ElasticClient.getInstance();

        String id1 = ec.create(
                new ConcreteTestObject("t1", "t1", 0),
                ConcreteTestObject.class
        );

        String id2 = ec.create(
                new ConcreteTestObject("t2", "t1", 0),
                ConcreteTestObject.class
        );

        assertNotEquals(null, id1);
        assertNotEquals(null, id2);


        List<ElasticTestObject> arr;


        try {
            // give elasticsearch time to index
            Thread.sleep(1000);
            arr = ec.search(
                    "{\"query\": {\"match\": {\"param1\": \"t2\"}}}",
                    ElasticTestObject.class,
                    ConcreteTestObject.class
            );
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        ConcreteTestObject obj;
        try {
            obj = arr.get(0).getObj();
        } catch(IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(obj.param1, "t2");

        try {
            ec.delete(id1, ConcreteTestObject.class);
            ec.delete(id2, ConcreteTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}