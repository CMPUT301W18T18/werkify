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


import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

public class CacheTest {
    private Cache cache;

    @Before
    public void setUpCache() {
        this.cache = new Cache();
    }

    @Test
    public void testSimpleCacheOperations() {
        assertNull(cache.get("ID1"));
        assertNull(cache.get("ID0"));

        SimpleRemoteObject object = new SimpleRemoteObject("RID1", 5);
        cache.put("ID1", object);

        assertEquals(object, cache.get("ID1"));
        assertNull(cache.get("ID0"));

        cache.put("ID2", object);

        assertEquals(object, cache.get("ID2"));
        assertEquals(object, cache.get("ID1"));

        cache.discard("ID1");

        assertEquals(object, cache.get("ID2"));
        assertNull(cache.get("ID1"));

        SimpleRemoteObject newObject = new SimpleRemoteObject("RID2", 12);
        cache.put("ID1", newObject);

        assertEquals(newObject, cache.get("ID1"));

        cache.put("ID3", newObject);
        cache.discard("ID1");

        assertEquals(newObject, cache.get("ID3"));
    }

}
