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


import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Caching wrapper around a RemoteClient.
 * Only downloads and are cached. Uploads/deletes/searches/creates
 * are passed transparently to the wrapped client.
 * Once a download is cached, any further downloads of the same ID
 * will return the cached object. An object can be re-downloaded by
 * calling discardCached on its id.
 *
 * @see Cache
 * @see RemoteObject
 * @see RemoteClient
 */
public class CachingClient<TClient extends RemoteClient> extends RemoteClient {
    private TClient client;
    private Cache cache;

    private Set<String> transientIdSet;

    private CachingClientWrapperSearcher searcher = new CachingClientWrapperSearcher(this);
    private CachingClientSearcher localSearcher = new CachingClientSearcher(this);

    /**
     * creates a CachingClient to wrap an existing RemoteClient.
     * @param client the client you are wrapping.
     */
    public CachingClient(TClient client) {
        this.client = client;
        this.cache = new Cache();
        this.transientIdSet = new HashSet<>();
    }

    /**
     * discard the cached changes to an object.
     * this will force the use of the wrapped client
     * to get future references to this object.
     * @param id the id associated with the object you are discarding.
     */
    public void discardCached(String id) {
        this.cache.discard(id);
    }

    /**
     * set obj as the cached version of itself.
     * @param obj the object to put in the cache.
     */
    public void updateCached(RemoteObject obj) {
        cache.put(obj.getId(), obj);
    }

    /**
     * download the object referred to by id, from the wrapped RemoteClient
     * rather then the cache.
     * @param id the id associated with the object you want.
     * @param type the type of the RemoteObject.
     * @param <T> the generic type of the remote object.
     * @return the RemoteObject of type T referred to by id.
     * @throws IOException when download fails.
     */
    public <T extends RemoteObject> T downloadFromRemote(String id, Class<T> type) throws IOException {
        return client.download(id, type);
    }

    /**
     * create a remote object locally. this object will not be created
     * on the server but can be uploaded later.
     * @param type the type of remote object to create.
     * @param conArgs the arguments to the constructor of the remoteObject.
     * @param <T> the generic type of the RemoteObject.
     * @return the newly created object.
     */
    public <T extends RemoteObject> T createLocal(Class<T> type, Object... conArgs) {
        try {
            T object = (T) newInstance(type, conArgs);
            object.setId(makeTransientId());
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * create a remote object on the server and put it in the cache.
     * @param type the type of your instance.
     * @param conArgs the arguments to your constructor.
     * @param <T> the generic type of your instance.
     * @return the newly created object.
     */
    @Override
    @Nullable
    public <T extends RemoteObject> T create(Class<T> type, Object... conArgs) {
        T object = client.create(type, conArgs);

        if (object == null) { return null; }

        cache.put(object.getId(), object);
        return object;
    }

    /**
     * download the Object from the cache. if the cache misses,
     * download from the wrapped client.
     * @param id object id.
     * @param type the type of the object.
     * @param <T> the generic type of the object.
     * @return the downloaded object
     * @throws IOException
     */
    @Override
    @Nullable
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        T cachedObject = cache.get(id);
        if (cachedObject != null) { return cachedObject; }

        // cache miss; cache the object and return it
        T downloadedObject = client.download(id, type);
        cache.put(id, downloadedObject);
        return downloadedObject;
    }

    /**
     * upload the object to the inner client and keep
     * the new version in the caches.
     * @param obj the object to upload
     * @throws IOException when upload fails.
     */
    @Override
    public void upload(RemoteObject obj) throws IOException {
        if (this.transientIdSet.contains(obj.getId())) {
            throw new IOException("Refusing to upload transient RemoteObject");
        }

        client.upload(obj);
        cache.put(obj.getId(), obj);
    }

    /**
     * uploadNew uploads a remote object that does not exist
     * on the server, and creates a new id for it.
     * try to use create instead of this.
     * @param type the type of the object to upload.
     * @param obj the object you are uploading.
     * @param <T> the generic type of the object.
     * @return the object that was uploaded.
     * @throws IOException when the inner upload fails.
     */
    @Override
    public <T extends RemoteObject> T uploadNew(Class<T> type, T obj) throws IOException {
        if (client.uploadNew(type, obj) == null) {
            throw new IOException();
        }
        cache.put(obj.getId(), obj);
        return obj;
    }

    /**
     * delete the object from the client and
     * from the cache.
     * @param obj the remote object to delete
     */
    @Override
    public void delete(RemoteObject obj) {
        client.delete(obj);
        cache.discard(obj.getId());
    }

    /**
     * gets this clients searcher.
     * @return the searcher.
     */
    @Override
    public Searcher getSearcher() {
        return searcher;
    }

    /**
     * gets a searcher for the cache.
     * @return the local searcher.
     */
    public Searcher getLocalSearcher() {
        return localSearcher;
    }

    /**
     * Generates a unique transient ID for an object.
     * A transient ID should never be a valid Elastic ID.
     * @return new transient ID
     */
    private String makeTransientId() {
        String transientId = UUID.randomUUID().toString();
        transientIdSet.add(transientId);
        return transientId;
    }

    /**
     * gets the searcher of the client that CachingClient wraps.
     * @return the inner searcher.
     */
    private Searcher getWrappedSearcher() {
        return client.getSearcher();
    }

    /**
     * CachingClientWrapperSearcher Is a Searcher that that wraps a
     * RemoteClient Searcher
     *
     * @see Searcher
     */
    public class CachingClientWrapperSearcher extends Searcher<CachingClient> {
        /**
         * create the CachingClientWrapperSearcher from the
         * client we are wrapping
         * @param client the client we are wrapping
         */
        public CachingClientWrapperSearcher(CachingClient client) {
            super(client);
        }

        /**
         * finds task by bidder first in  wrapped client,
         * then in the cache
         * @param bidder the bidder of the tasks we match
         * @return A list of the tasks that match
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByBidder(User bidder) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByBidder(bidder);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByBidder(bidder);
            }
        }

        /**
         * finds task by bidder and statuses. first in  wrapped client,
         * then in the cache.
         *
         * @param bidder the bidder of the tasks we match.
         * @param statuses the valid statuses for the search.
         * @return A list of the tasks that match.
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByBidder(User bidder, TaskStatus... statuses) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByBidder(bidder, statuses);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByBidder(bidder, statuses);
            }
        }

        /**
         * finds task by keywords in the wrapped client.
         *
         * @param keywords the keywords of the tasks
         * @return A list of the tasks that match.
         * @throws IOException when the internal searcher throws it.
         */
        @Override
        public List<Task> findTasksByKeywords(String keywords) throws IOException {
            return getWrappedSearcher().findTasksByKeywords(keywords);
        }

        /**
         * find tasks near a location in the wrapped client.
         * @param location the TaskLocation to find tasks near.
         * @return the List<Task> of tasks that are near the location
         * @throws IOException when the wrapped client does.
         */
        @Override
        public List<Task> findTasksNear(TaskLocation location) throws IOException {
            return getWrappedSearcher().findTasksNear(location);
        }

        /**
         * finds task by provider first in  wrapped client,
         * then in the cache.
         * @param provider the User that will do the tasks.
         * @return A list of the tasks that match.
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByProvider(User provider) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByProvider(provider);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByProvider(provider);
            }
        }

        /**
         * finds task by provider and statuses. first in  wrapped client,
         * then in the cache.
         *
         * @param provider the User that will do the tasks.
         * @param statuses the valid statuses for the search.
         * @return A list of the tasks that match.
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByProvider(User provider, TaskStatus... statuses) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByProvider(provider, statuses);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByProvider(provider, statuses);
            }
        }

        /**
         * finds task by the requester first in  wrapped client,
         * then in the cache
         * @param requester the User that reuqested the Task
         * @return A list of the tasks that match
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByRequester(User requester) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByRequester(requester);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByRequester(requester);
            }
        }

        /**
         * finds task by the requester first in  wrapped client,
         * then in the cache
         * @param requester the User that reuqested the Task
         * @param statuses the valid statuses for the search.
         * @return A list of the tasks that match
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Task> findTasksByRequester(User requester, TaskStatus... statuses) throws IOException {
            try {
                List<Task> results = getWrappedSearcher().findTasksByRequester(requester, statuses);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findTasksByRequester(requester, statuses);
            }
        }

        /**
         * finds all the signals (notifications) that are
         * sent to the provided user.
         * @param user the users that you want to get signals of.
         * @return A list of the tasks that match.
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Signal> findSignalsByUser(User user) throws IOException {
            try {
                List<Signal> results = getWrappedSearcher().findSignalsByUser(user);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findSignalsByUser(user);
            }
        }

        /**
         * finds all the signals (notifications) that are
         * sent to the provided user and are about the target id.
         * @param userId the users that you want to get signals of.
         * @param targetId the targetId that we are looking for.
         * @return A list of the tasks that match.
         * @throws IOException never, however the interface defines it.
         */
        @Override
        public List<Signal> findSignalsByUserAndTargetIds(String userId, String targetId) throws IOException {
            try {
                List<Signal> results = getWrappedSearcher().findSignalsByUserAndTargetIds(userId, targetId);
                cache.putAll(results);
                return results;
            } catch (IOException e) {
                return getLocalSearcher().findSignalsByUserAndTargetIds(userId, targetId);
            }
        }

        /**
         * get a user by it's username first on server, then
         * if that fails, in the cache.
         * @param username the username of the user
         * @return the user with username username.
         * @throws IOException never, but it's part of the interface
         */
        @Override
        public User getUser(String username) throws IOException {
            try {
                User result = getWrappedSearcher().getUser(username);
                if (result == null) { return null; }
                cache.put(result.getId(), result);
                return result;
            } catch (IOException e) {
                return getLocalSearcher().getUser(username);
            }
        }
    }

    /**
     * CachingClient Searcher is a searcher that searches the cache
     * exclusively.
     *
     * @see Searcher
     * @see Cache
     */
    public class CachingClientSearcher extends Searcher<CachingClient> {

        /**
         * create the CachingClientSearcher from a CachingClient.
         * @param client the CachingClient to search
         */
        public CachingClientSearcher(CachingClient client) {
            super(client);
        }

        /**
         * gets tasks in the cache where bidder is the bidder.
         * @param bidder User to search for
         * @return the List<Task> of results
         */
        @Override
        public List<Task> findTasksByBidder(final User bidder) {
            return cache.findMatching(new CacheMatcher<Task>() {
                @Override
                public boolean isMatch(Task task) {
                    return (task.getBidForUser(bidder) != null);
                }
            });
        }

        /**
         * gets tasks in the cache where bidder is the bidder
         * and the status is one of statuses
         * @param bidder User to search for
         * @param statuses the statuses that are valid
         * @return the List<Task> of results
         */
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

        /**
         * finds tasks in the cache that are near location
         * @param location the Location to search around
         * @return the List<Task> of results
         */
        @Override
        public List<Task> findTasksNear(TaskLocation location) {
            throw new IllegalStateException("Can't perform location search on cache");
        }

        /**
         * finds tasks in the cache that contain any of the keywords.
         * @param keywords Keywords to search for.
         * @return the List<Task> of results
         */
        @Override
        public List<Task> findTasksByKeywords(String keywords) {
            throw new IllegalStateException("Can't perform keyword search on cache");
        }

        /**
         * gets tasks in the cache where provider is the provider.
         * @param provider User to search for
         * @return the List<Task> of results
         */
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

        /**
         * gets tasks in the cache where provider is the provider.
         * @param provider User to search for
         * @param statuses the statuses that are valid
         * @return the List<Task> of results
         */
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

        /**
         * finds task by the requester in the cache
         * @param requester the User that requested the Task
         * @return A list of the tasks that match
         */
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

        /**
         * finds task by the requester in the cache.
         * @param requester the User that requested the Task
         * @param statuses the valid statuses for the search.
         * @return A list of the tasks that match
         */
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

        /**
         * finds all the signals (notifications) that are
         * sent to the provided user.
         * @param user the users that you want to get signals of.
         * @return A list of the tasks that match.
         */
        @Override
        public List<Signal> findSignalsByUser(final User user) {
            return cache.findMatching(new CacheMatcher<Signal>() {
                @Override
                public boolean isMatch(Signal signal) {
                    try {
                        return (user.equals(signal.getRemoteUser(client)));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        /**
         * finds all the signals (notifications) that are
         * sent to the provided user and are about the target id.
         * @param userId the users that you want to get signals of.
         * @param targetId the targetId that we are looking for.
         * @return A list of the tasks that match.
         */
        @Override
        public List<Signal> findSignalsByUserAndTargetIds(final String userId, final String targetId) {
            return cache.findMatching(new CacheMatcher<Signal>() {
                @Override
                public boolean isMatch(Signal signal) {
                    try {
                        return (targetId.equals(signal.getTargetId()) && userId.equals(signal.getRemoteUser(client).getId()));
                    } catch (IOException e) {
                        return false;
                    }
                }
            });
        }

        /**
         * get a user by it's username first on server, then
         * if that fails, in the cache.
         * @param username the username of the user
         * @return the user with username username.
         */
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
