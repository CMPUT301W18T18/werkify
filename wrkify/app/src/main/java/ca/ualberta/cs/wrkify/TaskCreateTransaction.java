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

import android.util.Log;

import java.io.IOException;

/**
 * TaskCreateTransaction is a Transaction for creating
 * a task
 */
public class TaskCreateTransaction extends Transaction<Task> {
    private Task localTask;

    /**
     * create a TaskCreateTransaction from a local task
     * @param localTask the task to create.
     */
    public TaskCreateTransaction(Task localTask) {
        super(localTask, Task.class);
        this.localTask = localTask;
    }

    /**
     * apply the creation to the client
     * @param client the client we are uploading to
     * @return true if sucessful, false otherwise.
     * @throws IOException according the client
     */
    @Override
    public boolean applyInClient(CachingClient client) throws IOException {
        // TODO images etc. will have to be uploaded and canonicalized before uploadNew
        String tid = localTask.getId();

        Log.i("-->", "id before: " + localTask.getId());
        client.uploadNew(Task.class, localTask);
        Log.i("-->", "id after: " +localTask.getId());
        return true;
    }
}
