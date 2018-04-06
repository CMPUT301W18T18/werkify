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

public class RemoteTaskList extends AbstractList<Task> {
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
    public int size() {
        return this.tasks.size();
    }
}
