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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Cache for RemoteObjects.
 * This is basically a slightly more type-aware
 * wrapper around a HashMap.
 *
 * @see RemoteObject
 */
public class Cache {
    private HashMap<String, RemoteObject> map = new HashMap<>();

    /**
     * the empty constructor for cache
     */
    public Cache() { }

    /**
     * get an object by it's id.
     * @param id the id of the object.
     * @param <T> the remoteObject that you are getting.
     * @return the object pointed to by id.
     */
    public <T extends RemoteObject> T get(String id) {
        return (T) this.map.get(id);
    }

    /**
     * put an object in the cache.
     * @param id the id associated with the object.
     * @param object the object you are putting in the cache
     * @param <T> the type of object
     */
    public <T extends RemoteObject> void put(String id, T object) {
        this.map.put(id, object);
    }

    /**
     * put a collection of objects in the cache.
     * @param objects the objects to put in the cache.
     * @param <T> the type of remoteObject that the objects are
     */
    public <T extends RemoteObject> void putAll(Collection<T> objects) {
        for (RemoteObject object: objects) {
            put(object.getId(), object);
        }
    }

    /**
     * remove an object from the cache by it's id.
     * @param id the id of the object to remove.
     */
    public void discard(String id) {
        this.map.remove(id);
    }

    /**
     * finds all items in the cache that match the CacheMatcher.
     * @param matcher the CacheMatcher, specifying what to match.
     * @param <T> the type of RemoteObject to match.
     * @return a List of the objects that match.
     */
    public <T extends RemoteObject> List<T> findMatching(CacheMatcher<T> matcher) {
        List<T> results = new ArrayList<>();
        Collection<String> keys = map.keySet();
        for (String key: keys) {
            RemoteObject object = map.get(key);
            try {
                T castObject = (T) object;
                if (matcher.isMatch(castObject)) {
                    results.add(castObject);
                }
            } catch (ClassCastException e) {
                // continue
            }
        }

        return results;
    }
}
