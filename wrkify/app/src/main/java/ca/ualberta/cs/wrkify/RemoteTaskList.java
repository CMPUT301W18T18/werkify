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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RemoteTaskList implements List<Task> {
    private RemoteClient client;
    private ArrayList<RemoteReference<Task>> tasks;

    public RemoteTaskList(RemoteClient client) {
        this.client = client;
        this.tasks = new ArrayList<>();
    }

    public RemoteTaskList(RemoteClient client, ArrayList<RemoteReference<Task>> tasks) {
        this.client = client;
        this.tasks = tasks;
    }

    @Override
    public void add(int index, Task element) {
        this.tasks.add(index, element.<Task>reference());
    }

    @Override
    public Task get(int index) {
        try {
            return this.tasks.get(index).getRemote(this.client, Task.class);
        } catch (IOException e) {
            // this is possible but should not happen
            // with a caching client
            return null;
        }
    }

    @Override
    public Task set(int index, Task element) {
        try {
            return this.tasks.set(index, element.<Task>reference())
                    .getRemote(this.client, Task.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Task remove(int index) {
        try {
            return this.tasks.remove(index)
                    .getRemote(this.client, Task.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ListIterator<Task> listIterator() {
        return this.new Iterator();
    }

    @Override
    public ListIterator<Task> listIterator(int index) {
        return this.new Iterator(index);
    }

    private class Iterator implements ListIterator<Task> {
        private int index;

        public Iterator() {
            this.index = 0;
        }

        public Iterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            // note that task is a member of RemoteTaskList
            return index < tasks.size();
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public Task next() {
            Task task = get(this.index);
            this.index++;
            return task;
        }

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public Task previous() {
            this.index--;
            return get(this.index);
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
        public void add(Task task) {
            throw new UnsupportedOperationException("Iterator does not support add()");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Iterator does not support remove()");
        }

        @Override
        public void set(Task task) {
            throw new UnsupportedOperationException("Iterator does not support set()");
        }
    }
}
