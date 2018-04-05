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
import java.io.Serializable;

/**
 * A RemoteReference is an object consisting of a remote ID that can be
 * dereferenced to produce an object of a specific type.
 * @param <T> type of the target object
 */
public class RemoteReference<T extends RemoteObject> implements Serializable {
    private String refId;

    /**
     * Creates a remote reference to the object with remote ID refID.
     * @param refId ID of the remote object
     */
    public RemoteReference(String refId) {
        this.refId = refId;
    }

    /**
     * Retrieves the referred-to object from the given client.
     * @param client RemoteClient to find the reference in
     * @param tClass type of the remote object
     * @return retrieved remote object
     * @throws IOException if RemoteClient failed to download the object
     */
    public T getRemote(RemoteClient client, Class<T> tClass) throws IOException {
        return client.download(this.refId, tClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RemoteReference) {
            RemoteReference other = (RemoteReference) obj;
            return (other.refId.equals(refId));
        }
        return super.equals(obj);
    }
}
