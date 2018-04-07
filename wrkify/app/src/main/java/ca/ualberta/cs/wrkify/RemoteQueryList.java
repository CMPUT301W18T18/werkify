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

public abstract class RemoteQueryList<T extends RemoteObject> extends RemoteList<T> {
    private RemoteClient client;

    public RemoteQueryList(RemoteClient client, Class<T> type) {
        super(client, type);
        this.client = client;
    }

    @Override
    public void refresh() {
        this.setObjs(query(this.client));
    }

    public abstract List<T> query(RemoteClient client);
}