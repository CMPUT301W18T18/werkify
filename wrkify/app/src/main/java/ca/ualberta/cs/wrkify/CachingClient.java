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
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Caching wrapper around a RemoteClient.
 * Only downloads and are cached. Uploads/deletes/searches/creates
 * are passed transparently to the wrapped client.
 * Once a download is cached, any further downloads of the same ID
 * will return the cached object. An object can be re-downloaded by
 * calling discardCached on its id.
 *
 * TODO this will ultimately also buffer uploads/deletes/searches.
 *
 * @see Cache
 */
public class CachingClient<TClient extends RemoteClient> extends RemoteClient {
    private TClient client;
    private Cache cache;

    public CachingClient(TClient client) {
        this.client = client;
        this.cache = new Cache();
    }

    public void discardCached(String id) {
        this.cache.discard(id);
    }

    public boolean isCached(String id) {
        return (this.cache.get(id) != null);
    }

    /**
     * Tries to get an object by ID from the cache, without falling through
     * to the underlying RemoteClient if not available. This will never fail
     * due to network connectivity etc. but will return null if the target ID
     * isn't currently available in the cache.
     * @param id ID to get
     * @param <T> Type of RemoteObject to get
     * @return cached RemoteObject, or null if not in cache
     */
    @Nullable
    public <T extends RemoteObject> T getFromCache(String id) {
        return this.cache.get(id);
    }

    @Override
    @Nullable
    public <T extends RemoteObject> T create(Class<T> type, Object... conArgs) {
        return client.create(type, conArgs);
    }

    @Override
    @Nullable
    public <T extends RemoteObject> T download(String id, Class<T> type) throws IOException {
        T cachedObject = cache.get(id);
        if (cachedObject != null) { return cachedObject; }

        // cache miss; cache the object and return it
        Log.i("cache miss", id);
        T downloadedObject = client.download(id, type);
        cache.put(id, downloadedObject);
        return downloadedObject;
    }

    @Override
    public void upload(RemoteObject obj) {
        client.upload(obj);
        cache.put(obj.getId(), obj);
    }

    @Override
    public void delete(RemoteObject obj) {
        client.delete(obj);
        cache.discard(obj.getId());
    }

    @Override
    public <T extends RemoteObject> List<T> search(String query, Class<T> type) throws IOException {
        return client.search(query, type);
    }

    /**
     * Generates a unique transient ID for an object.
     * A transient ID should never be a valid Elastic ID.
     * @return new transient ID
     *
     * TODO this will eventually be used for create buffering, but is currently unused.
     */
    private String makeTransientId() {
        // TODO not sure if this is the best way to do this
        return UUID.randomUUID().toString();
    }
}
