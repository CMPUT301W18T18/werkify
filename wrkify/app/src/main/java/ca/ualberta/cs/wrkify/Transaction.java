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
import java.lang.reflect.Method;
import java.util.List;

/**
 *  a Transaction models a method call to a remoteobject that changes
 *  the object.
 */

public class Transaction<T extends RemoteObject> {
    private T object;
    private Method method;
    private List<Object> args;

    public Transaction(T object, Method method, List<Object> args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    public Boolean apply(T updatingObject) {
        try {
            this.method.invoke(updatingObject, this.args);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public T getObject() {
        return this.object;
    }
}
