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

/**
 * TaskDeleteTransaction is a Transaction that models
 * the deleteion of a Task
 */
public class TaskDeleteTransaction extends Transaction<Task> {
    /**
     * create a TaskDeleteTransaction from a Task
     * @param task the task to delete
     */
    public TaskDeleteTransaction(Task task) {
        super(task, Task.class);
    }

    /**
     * apply the deletion of the task
     * @param client the client we are using
     * @return true if sucessful, false otherwise.
     * @throws IOException according to client
     */
    @Override
    public boolean applyInClient(CachingClient client) throws IOException {
        Task task = (Task) client.downloadFromRemote(getId(), getType());
        if (task == null) { return false; }
        client.delete(task);

        return true;
    }
}
