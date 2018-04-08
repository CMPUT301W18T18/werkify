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

public abstract class Transaction<T extends RemoteObject> {
    protected String id;
    protected Class<T> type;

    Transaction(T remObj, Class<T> type) {
        this.id = remObj.getId();
        this.type = type;
    }

    /**
     * gets the id of the object referenced by
     * the Transaction
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * gets the type of the object referenced by
     * the Transaction
     * @return the type of the object
     */
    public Class<T> getType() {
        return this.type;
    }

    public abstract boolean applyInClient(CachingClient client) throws IOException;
}
