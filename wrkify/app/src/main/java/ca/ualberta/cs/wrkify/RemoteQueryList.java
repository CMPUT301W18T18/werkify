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

import java.util.List;

/**
 * RemoteQueryList is a RemoteList that defines refresh behavior
 * based on a query to a remoteClient
 * @param <T>
 */
public abstract class RemoteQueryList<T extends RemoteObject> extends RemoteList<T> {
    private CachingClient client;

    /**
     * creates the RemoteQueryList from the client.
     * @param client the client that we will refresh from.
     * @param type the type that this list holds.
     */
    public RemoteQueryList(CachingClient client, Class<T> type) {
        super(client, type);
        this.client = client;
    }

    /**
     * refresh the list from the client
     */
    @Override
    public void refresh() {
        List<T> objs = query(this.client);
        if (objs != null) {
            this.setObjs(objs);
        }
    }

    /**
     * subclasses can define query to get custom refresh
     * behavior.
     * @param client the client to query.
     * @return a List of results of the query.
     */
    public abstract List<T> query(CachingClient client);
}
