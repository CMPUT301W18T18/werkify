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
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
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

    private ArrayList<RemoteListItem<T>> items;

    /**
     * creates a RemoteList from the client and the type
     * @param client the client that you want to get from
     * @param type the type of <T> (needed to download)
     */
    public RemoteList(RemoteClient client, Class<T> type) {
        this.client = client;
        this.type = type;

        this.items = new ArrayList<>();
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
        T obj = this.items.get(index).object;
        if (obj != null) {
            // if we already have the object, return it
            return obj;
            // I didn't know you could do this
        } else try {
            // if we don't have the object, get it from the remote, and return it.
            obj = this.items.get(index).reference.getRemote(this.client, this.type);
            this.items.get(index).object = obj;
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
        return this.items.size();
    }

    /**
     * refreshes the list such that new requests to get
     * will be gotten from the client.
     */
    public void refresh() {
        for (int i = 0; i < this.items.size(); ++i) {
            // get() gets from the client when null
            this.items.set(i, null);
        }
        this.sortByTimestamp();
    }

    protected void setObjs(List<T> objs) {
        this.items.clear();

        for (T obj: objs) {
            this.items.add(new RemoteListItem<>(obj, obj.<T>reference()));
        }

        this.sortByTimestamp();
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
            list.items.add(new RemoteListItem<>(null, ref));
        }

        return list;
    }

    /**
     * sort the items in the list by their
     * creation date.
     */
    private void sortByTimestamp() {
        Collections.sort(items);
    }

    /**
     * a RemoteListItem is an item in a RemoteList
     * @param <U> the specific type of RemoteObject in the list
     */
    private static class RemoteListItem<U extends RemoteObject> implements Comparable<RemoteListItem<U>> {
        public RemoteReference<U> reference;
        public U object;

        public RemoteListItem(U object, RemoteReference<U> reference) {
            this.reference = reference;
            this.object = object;
        }

        @Override
        public int compareTo(@NonNull RemoteListItem<U> other) {
            return Long.compare(object.getTimestamp(), other.object.getTimestamp());
        }

        @Override
        public String toString() {
            return object.getId();
        }
    }
}
