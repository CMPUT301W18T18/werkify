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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 */
class MockRemoteClient extends RemoteClient {
    private HashMap<String, Object> hmap;
    private RemoteObject[] nextSearchResult;

    public MockRemoteClient() {
        this.hmap = new HashMap<String, Object>();
    }
    
    public <T extends RemoteObject> T create(Class<T> type, Object ...conArgs) {
        T instance;
        try {
            instance = newInstance(type, conArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String id = UUID.randomUUID().toString();
        this.hmap.put(id, instance);
        instance.setId(id);
        
        return instance;
    }
    
    public void upload(RemoteObject obj) {
        this.hmap.put(obj.getId(), obj);
    }
    
    public void delete(RemoteObject obj) {
        this.hmap.remove(obj.getId());
    }
    
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        return type.cast(this.hmap.get(id));
    }

    @Override
    <T extends RemoteObject> List<T> search(String query, Class<T> type) throws IOException {
        if (this.nextSearchResult != null) {
            List<RemoteObject> results = Arrays.asList(this.nextSearchResult);
            this.nextSearchResult = null;
            return (List<T>) results;
        } else {
            return new ArrayList<>();
        }
    }

    public void mockNextSearch(RemoteObject... objects) {
        this.nextSearchResult = objects;
    }
}