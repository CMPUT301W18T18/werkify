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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * RemoteList is a List of Remote objects that can be refreshed.
 * It internally stores the objects as well and gets them as needed.
 * this type should NOT be serialized.
 *
 * @param <T> the remote object that the list is of.
 *
 * @see RemoteClient
 * @see RemoteObject
 * @see List
 */
public class RemoteList<T extends RemoteObject> extends AbstractList<T> {
    private RemoteClient client;
    private Class<T> type;

    private ArrayList<RemoteReference<T>> refs;
    private ArrayList<T> objs;

    /**
     * creates a RemoteList from the client and the type
     * @param client the client that you want to get from
     * @param type the type of <T> (needed to download)
     */
    public RemoteList(RemoteClient client, Class<T> type) {
        this.client = client;
        this.type = type;

        this.refs = new ArrayList<>();
        this.objs = new ArrayList<>();
    }

    /**
     * implementation of List<T>.get(). gets the object from internal
     * storage first, otherwise gets the object from the client and
     * stores it.
     * @param index the index in the list you want to get
     * @return the RemoteObject at that index
     */
    @Override
    public T get(int index) {
        T obj = this.objs.get(index);
        if (obj != null) {
            // if we already have the object, return it
            return obj;
            // I didn't know you could do this
        } else try {
            // if we don't have the object, get it from the remote, and return it.
            obj = this.refs.get(index).getRemote(this.client, this.type);
            this.objs.set(index, obj);
            return obj;
        } catch (IOException e) {
            // this is possible but should not happen
            // with a caching client
            return null;
        }
    }

    /**
     * gets the size of the List
     * @return the size of the list
     */
    @Override
    public int size() {
        return this.refs.size();
    }

    /**
     * refreshes the list such that new requests to get
     * will be gotten from the client.
     */
    public void refresh() {
        for (int i = 0; i < this.refs.size(); ++i) {
            // get() gets from the client when null
            this.refs.set(i, null);
        }
    }

    protected void setObjs(List<T> objs) {
        for (T obj: objs) {
            this.objs.add(obj);
            this.refs.add(obj.<T>reference());
        }
    }

    /**
     * A named constructor to create a RemoteList from a
     * List of RemoteObjects
     *
     * this function exists because type erasure means that
     * two constructors with ArrayLists will have the same
     * signatures and cannot coexist.
     *
     * @param rc the client of the list
     * @param type the type of the objects in the list
     * @param objs the List of Objects.
     * @param <E> the type of RemoteObject that the list will contain
     * @return the created RemoteList
     */
    public static <E extends RemoteObject> RemoteList<E>
    fromObjects(RemoteClient rc, Class<E> type, List<E> objs) {

        RemoteList<E> list = new RemoteList<E>(rc, type);
        list.setObjs(objs);

        return list;
    }

    /**
     * A named constructor to create a RemoteList from a
     * List of RemoteReferences
     *
     * this function exists because type erasure means that
     * two constructors with ArrayLists will have the same
     * signatures and cannot coexist.
     *
     * @param rc the client of the list
     * @param type the type of the objects in the list
     * @param refs the List of references to the objects of the list
     * @param <E> the type of RemoteObject that the list will contain
     * @return the created RemoteList
     */
    public static <E extends RemoteObject> RemoteList<E>
    fromReferences(RemoteClient rc, Class<E> type, List<RemoteReference<E>> refs) {

        RemoteList<E> list = new RemoteList<E>(rc, type);

        for (RemoteReference<E> ref: refs) {
            list.objs.add(null);
            list.refs.add(ref);
        }

        return list;
    }
}
