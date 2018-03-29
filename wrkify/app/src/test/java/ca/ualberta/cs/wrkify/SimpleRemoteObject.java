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
 * Mock remote object for testing.
 */
class SimpleRemoteObject extends RemoteObject {
    public int field;

    /**
     * Constructor for the mock object.
     * @param field arbitrary integer
     */
    public SimpleRemoteObject(Integer field) {
        this.field = field;
    }

    /**
     * Constructor that also sets the object's ID, for use
     * outside of RemoteClient.create().
     * @param id id to set on the object
     * @param field arbitrary integer
     */
    public SimpleRemoteObject(String id, Integer field) {
        this(field);
        this.setId(id);
    }

    @Transact
    public void setFieldTo1() {
        this.field = 1;
    }

    @Transact
    public void setFieldTo2() {
        this.field = 2;
    }

    public void setFieldTo3() {
        this.field = 3;
    }
}
