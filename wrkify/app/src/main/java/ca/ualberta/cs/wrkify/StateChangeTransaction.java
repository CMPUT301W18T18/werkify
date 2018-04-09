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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Transaction models an atomic change to a RemoteObject
 * transactions subclasses define the apply() function
 * in order to implement the specific behavior.
 *
 * @see RemoteObject
 */

public abstract class StateChangeTransaction<T extends RemoteObject> extends Transaction<T> {

    /**
     * sets up the id and the type
     * @param remObj the object to extract the id from
     * @param type the type of remote object.
     */
    public StateChangeTransaction(T remObj, Class<T> type) {
        super(remObj, type);
    }

    public final boolean applyInClient(CachingClient client) throws IOException {
        T obj = (T) client.downloadFromRemote(client.canonicalize(getId()), getType());

        Signal[] signals = generateSignals(client, obj);

        boolean result = this.applyTo(obj);
        client.upload(obj);

        for (Signal signal: signals) {
            client.uploadNew(Signal.class, signal);
        }

        return result;
    }

    /**
     * verifys that the object provided is correct,
     * then deffers to the apply function.
     *
     * @param object the object to applyTo
     * @return true if successful, false if failed.
     */
    public final Boolean applyTo(T object) {
        if (object.getId() != this.getId() && !object.getId().equals(this.getId())) {
            return false;
        }

        return apply(object);
    }

    /**
     * apply defines the internal behavior of the
     * Transaction. THIS FUNCTION SHOULD NOT BE
     * USED. USE applyTo INSTEAD.
     * @param object
     * @return
     */
    protected abstract Boolean apply(T object);

    @NonNull
    protected Signal[] generateSignals(CachingClient client, T object) throws IOException {
        return new Signal[]{};
    }
}
