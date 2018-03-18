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

import java.io.Serializable;

import io.searchbox.annotations.JestId;

/**
 * Created by peter on 17/03/18.
 */

public abstract class RemoteObject implements Serializable {
    transient private @JestId String id;
    transient private RemoteClient client;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setClient(RemoteClient client) {
        this.client = client;
    }

    public void upload() {
        this.client.upload(this);
    }
    
    public RemoteClient getClient() {
        return this.client;
    }
    
    public <T extends RemoteObject> RemoteReference<T> reference() {
        return new RemoteReference(client, id, this.getClass());
    }
}
