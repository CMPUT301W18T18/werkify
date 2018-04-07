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
 * Transaction models an atomic change to a RemoteObject
 * transactions subclasses define the apply() function
 * in order to implement the specific behavior.
 *
 * @see RemoteObject
 */

public abstract class Transaction<T extends RemoteObject> {
    private String id;
    private Class<T> type;

    /**
     * sets up the id and the type
     * @param remObj the object to extract the id from
     * @param type the type of remote object.
     */
    public Transaction(T remObj, Class<T> type) {
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

    /**
     * verifys that the object provided is correct,
     * then deffers to the apply function.
     *
     * @param object the object to applyTo
     * @return true if successful, false if failed.
     */
    public Boolean applyTo(T object) {
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
}
