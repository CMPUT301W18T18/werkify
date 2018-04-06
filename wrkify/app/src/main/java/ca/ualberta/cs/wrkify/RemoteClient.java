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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

abstract class RemoteClient {
    /**
     * Create a new RemoteObject from its constructor, and upload it.
     * @param type the type of your instance
     * @param conArgs the arguments to your constructor
     * @return the new instance
     */
    abstract <T extends RemoteObject> T create(Class<T> type, Object... conArgs);

    /**
     * Re-uploads changes of an existing RemoteObject
     * to this RemoteClient
     * @param obj the object to upload
     */
    abstract void upload(RemoteObject obj) throws IOException;

    /**
     * Uploads a RemoteObject to the server that was not initially there.
     */
    abstract <T extends RemoteObject> T uploadNew(Class<T> type, T obj) throws IOException;

    /**
     * Deletes a remote object
     * @param obj the remote object
     */
    abstract void delete(RemoteObject obj);

    /**
     * Downloads an object given type and id
     *
     * @param id object id
     * @param type the type of the object
     * @return the RemoteObject to return
     * @throws IOException when executing fails
     */
    abstract <T extends RemoteObject> T download(String id, Class<T> type) throws IOException;

    /**
     * Returns objects matching a query
     *
     * @param query query string
     * @param type a remote object you are searching for
     * @param <T> the type of remote object you are searching for
     * @return a list<T>
     * @throws IOException according to execute
     */
    abstract  <T extends RemoteObject> List<T> search(String query, Class<T> type) throws IOException;

    /**
     * newInstance provides a dynamic constructor interface that takes a Class
     * and the normal constructor arguments and returns an instance.
     *
     * TODO: this function does not work with constructors that use primitive types
     *
     * @param type
     * @param conArgs
     * @return
     * @throws NoSuchMethodException according to Constructor<T>.getConstructor()
     * @throws IllegalAccessException according to Constructor<T>.newInstance()
     * @throws InstantiationException according to Constructor<T>.newInstance()
     * @throws InvocationTargetException according to Constructor<T>.newInstance()
     */
    protected static <T> T newInstance(Class<T> type, Object ...conArgs)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        Class[] classes = new Class[conArgs.length];
        for (int i = 0; i < conArgs.length; ++i) {
            classes[i] = conArgs[i].getClass();
        }
        Constructor<T> con = type.getConstructor(classes);
        return con.newInstance(conArgs);
    }
}
