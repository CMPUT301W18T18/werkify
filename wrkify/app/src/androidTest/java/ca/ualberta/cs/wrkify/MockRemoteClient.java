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
 * will work by operating locally on a HashMap.
 */
class MockRemoteClient extends RemoteClient {
    private Cache cache = new Cache();
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
        this.cache.put(obj.getId(), obj);
    }

    @Override
    <T extends RemoteObject> T uploadNew(Class<T> type, T instance) {
        String id = UUID.randomUUID().toString();
        this.cache.put(id, instance);
        instance.setId(id);

        return instance;
    }

    @Override
    public void delete(RemoteObject obj) {
        this.cache.discard(obj.getId());
    }

    @Override
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        return type.cast(this.cache.get(id));
    }

    @Override
    Searcher getSearcher() {
        return searcher;
    }

    public void mockNextKeywordSearch(RemoteObject... objects) {
        this.nextSearchResult = objects;
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

    public class MockSearcher extends Searcher<MockRemoteClient> {
        public MockSearcher(MockRemoteClient client) {
            super(client);
        }

        @Override
        public List<Task> findTasksByBidder(final User bidder) {
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    return (task.getBidForUser(bidder) != null);
                }
            });
        }

        @Override
        public List<Task> findTasksByBidder(final User bidder, final TaskStatus... statuses) {
            final List<TaskStatus> statusList = Arrays.asList(statuses);
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    return (task.getBidForUser(bidder) != null && statusList.contains(task.getStatus()));
                }
            });
        }

        @Override
        public List<Task> findTasksByKeywordsNear(String keywords, TaskLocation location) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByKeywords(String keywords) {
            return mockSearch();
        }

        @Override
        public List<Task> findTasksByProvider(final User provider) {
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    try {
                        return (provider.equals(task.getRemoteProvider(client)));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        @Override
        public List<Task> findTasksByProvider(final User provider, final TaskStatus... statuses) {
            final List<TaskStatus> statusList = Arrays.asList(statuses);
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    try {
                        return (provider.equals(task.getRemoteProvider(client)) && statusList.contains(task.getStatus()));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        @Override
        public List<Task> findTasksByRequester(final User requester) {
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    try {
                        return (requester.equals(task.getRemoteRequester(client)));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        @Override
        public List<Task> findTasksByRequester(final User requester, TaskStatus... statuses) {
            final List<TaskStatus> statusList = Arrays.asList(statuses);
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    try {
                        return (requester.equals(task.getRemoteRequester(client)) && statusList.contains(task.getStatus()));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        @Override
        public User getUser(final String username) {
            List<User> results = cache.findMatching(new CacheMatcher<User>() {
                @Override
                public boolean isMatch(User user) {
                    return (username.equals(user.getUsername()));
                }
            });

            if (results.size() == 0) { return null; }
            else { return results.get(0); }
        }
    }
}
