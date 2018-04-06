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

public class RemoteList<T extends RemoteObject> extends AbstractList<T> {
    private RemoteClient client;
    private Class<T> type;

    private ArrayList<RemoteReference<T>> refs;
    private ArrayList<T> objs;

    public RemoteList(RemoteClient client, Class<T> type) {
        this.client = client;
        this.type = type;

        this.refs = new ArrayList<>();
        this.objs = new ArrayList<>();
    }

    @Override
    public T get(int index) {
        T obj = this.objs.get(index);
        if (obj != null) {
            // if we already have the object, return it
            return obj;
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

    @Override
    public int size() {
        return this.refs.size();
    }

    public void refresh() {
        for (int i = 0; i < this.refs.size(); ++i) {
            this.refs.set(i, null);
        }
    }

    public static <E extends RemoteObject> RemoteList<E>
    fromObjects(RemoteClient rc, Class<E> type, List<E> objs) {

        RemoteList<E> list = new RemoteList<E>(rc, type);

        for (E obj: objs) {
            list.objs.add(obj);
            list.refs.add(obj.<E>reference());
        }

        return list;
    }

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
