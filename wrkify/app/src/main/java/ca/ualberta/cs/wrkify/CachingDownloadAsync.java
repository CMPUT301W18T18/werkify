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

public class CachingDownloadAsync extends WatchableAsync<RemoteObject> {
    public static void startAsyncGet(CachingClient client, String id, Class type, AsyncWatcher<RemoteObject> watcher) {
        if (client.isCached(id)) {
            // Cache 'downloading' can be done synchronously
            watcher.onTaskFinished(client.getFromCache(id));
        } else {
            CachingDownloadAsync async = new CachingDownloadAsync();
            async.setClient(client);
            async.setTarget(id, type);

            async.execute();
        }
    }

    private RemoteClient client;
    private String targetId;
    private Class targetType;

    private void setClient(RemoteClient client) {
        this.client = client;
    }

    private void setTarget(String id, Class type) {
        this.targetId = id;
        this.targetType = type;
    }

    @Override
    protected RemoteObject doInBackground(Void... nothing) {
        try {
            return client.download(targetId, targetType);
        } catch (IOException e) {
            return null;
        }
    }
}
