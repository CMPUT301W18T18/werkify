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
import java.util.HashMap;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Created by peter on 09/03/18.
 */

public class ElasticObjectTest {

    @Test
    public void testConstructFromObject() {
        MockElasticClient client = new MockElasticClient();

        SimpleObject obj = new SimpleObject("hello");

        ElasticObject<SimpleObject> elo =
                new ElasticObject<SimpleObject>(obj, SimpleObject.class, client);

        try {
            assertEquals(elo.getObj().str, "hello");
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testConstructFromId() {
        MockElasticClient client = new MockElasticClient();

        SimpleObject obj = new SimpleObject("hello2");

        ElasticObject<SimpleObject> elo =
                new ElasticObject<SimpleObject>(obj, SimpleObject.class, client);

        ElasticObject<SimpleObject> elo2 =
                new ElasticObject<SimpleObject>(elo.getId(), SimpleObject.class, client);

        try {
            assertEquals(elo2.getObj().str, "hello2");
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testUpdateRefresh() {
        MockElasticClient client = new MockElasticClient();

        SimpleObject obj = new SimpleObject("hello3");

        ElasticObject<SimpleObject> elo =
                new ElasticObject<SimpleObject>(obj, SimpleObject.class, client);

        ElasticObject<SimpleObject> elo2 =
                new ElasticObject<SimpleObject>(elo.getId(), SimpleObject.class, client);

        try {
            elo2.getObj();

            obj.str = "goodbye";
            elo.update();
            elo2.refresh();

            assertEquals(elo2.getObj().str, "goodbye");
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}

/**
 * MockElasticClient is a fake ElasticClient
 * that uses HashMap internaly.
 *
 * note: the execute function is not supported
 *
 * @author Peter Elliott
 * @see ElasticClient
 */
class MockElasticClient extends ElasticClient {
    private HashMap<String, Object> hmap;

    public MockElasticClient() {
        this.hmap = new HashMap<String, Object>();
    }

    public <T> String create(T obj, Class<T> type) {
        String id = UUID.randomUUID().toString();

        hmap.put(id, obj);

        return id;
    }

    public <T> void update(String id, T obj, Class<T> type) {
        hmap.put(id, obj);
    }

    public <T> T get(String id, Class<T> type) {
        return type.cast(hmap.get(id));
    }
}

class SimpleObject {
    public String str;

    public SimpleObject(String str) {
        this.str = str;
    }
}
