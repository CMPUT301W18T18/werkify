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
                new ElasticTestObject("t1", "t1", 0),
                ElasticTestObject.class
        );

        assertNotEquals(id, null);

        ElasticTestObject et;
        try {
            et = ec.get(id, ElasticTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(et.param1, "t1");
        assertEquals(et.param2, "t1");
        assertEquals(et.param3, 0);

        try {
            ec.delete(id, ElasticTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
        }

        try {
            et = ec.get(id, ElasticTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(et, null);
    }

    @Test
    public void testUpdate() {
        ElasticClient ec = ElasticClient.getInstance();

        String id = ec.create(
                new ElasticTestObject("t1", "t1", 0),
                ElasticTestObject.class
        );

        assertNotEquals(id, null);

        ec.update(
                id,
                new ElasticTestObject("t2", "t2", 1),
                ElasticTestObject.class
        );

        ElasticTestObject et;
        try {
            et = ec.get(id, ElasticTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
            return;
        }

        assertEquals(et.param1, "t2");
        assertEquals(et.param2, "t2");
        assertEquals(et.param3, 1);

        try {
            ec.delete(id, ElasticTestObject.class);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}