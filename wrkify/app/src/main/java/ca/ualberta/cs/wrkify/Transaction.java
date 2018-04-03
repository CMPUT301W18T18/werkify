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

/**
 * Created by peter on 03/04/18.
 */

public abstract class Transaction<T extends RemoteObject> {
    private String id;
    private Class<T> type;

    public Transaction(T remObj, Class<T> type) {
        this.id = remObj.getId();
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public Class<T> getType() {
        return this.type;
    }

    public Boolean applyTo(T object) {
        if (object.getId() != this.getId() && !object.getId().equals(this.getId())) {
            return false;
        }

        return application(object);
    }

    protected abstract Boolean application(T object);
}
