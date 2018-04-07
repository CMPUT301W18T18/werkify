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
 * Local remote client for running UI tests.
 * Most RemoteClient methods called on the MockRemoteClient
 * will work by operating locally on a HashMap. Searches will
 * return no results unless explicitly mocked by calling
 * {@link #mockNextSearch(RemoteObject...)}.
 */
class MockRemoteClient extends RemoteClient {
    private HashMap<String, Object> hmap = new HashMap<>();
    private RemoteObject[] nextSearchResult;

    private MockSearcher searcher = new MockSearcher(this);

    @Override
    public <T extends RemoteObject> T create(Class<T> type, Object ...conArgs) {
        T instance;
        try {
            instance = newInstance(type, conArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return uploadNew(type, instance);
    }

    @Override
    public void upload(RemoteObject obj) {
        this.hmap.put(obj.getId(), obj);
    }

    @Override
    <T extends RemoteObject> T uploadNew(Class<T> type, T instance) {
        String id = UUID.randomUUID().toString();
        this.hmap.put(id, instance);
        instance.setId(id);

        return instance;
    }

    @Override
    public void delete(RemoteObject obj) {
        this.hmap.remove(obj.getId());
    }

    @Override
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        return type.cast(this.hmap.get(id));
    }

    private <T extends RemoteObject> List<T> mockSearch() {
        if (this.nextSearchResult != null) {
            List<RemoteObject> results = Arrays.asList(this.nextSearchResult);
            this.nextSearchResult = null;
            return (List<T>) results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    Searcher getSearcher() {
        return searcher;
    }

    /**
     * Sets the results for the next call to {@link #search(String, Class)} on the
     * MockRemoteClient.
     * @param objects search results of the next search
     */
    public void mockNextSearch(RemoteObject... objects) {
        this.nextSearchResult = objects;
    }

    public class MockSearcher extends Searcher<MockRemoteClient> {
        public MockSearcher(MockRemoteClient client) {
            super(client);
        }

        @Override
        public List<Task> findTasksByBidder(User bidder) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByBidder(User bidder, TaskStatus... statuses) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByKeywords(String keywords) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByKeywordsNear(String keywords, TaskLocation location) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByProvider(User provider) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByProvider(User provider, TaskStatus... statuses) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByRequester(User requester) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByRequester(User requester, TaskStatus... statuses) {
            return mockSearch();
        }

        @Override
        public User getUser(String username) {
            List<User> results = mockSearch();
            if (results == null || results.size() == 0) {
                return null;
            }
            return results.get(0);
        }
    }
}
