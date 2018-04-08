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
 */
public class Cache {
    private HashMap<String, RemoteObject> map = new HashMap<>();

    public Cache() { }

    public <T extends RemoteObject> T get(String id) {
        return (T) this.map.get(id);
    }

    public <T extends RemoteObject> void put(String id, T object) {
        this.map.put(id, object);
    }

    public void discard(String id) {
        this.map.remove(id);
    }

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
