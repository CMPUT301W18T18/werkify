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

import android.location.Location;

/**
 * TaskLocationTransaction represents a Transaction for
 * Task.setLocation()
 *
 * @see StateChangeTransaction
 * @see Task
 */
public class TaskLocationTransaction extends StateChangeTransaction<Task> {
    private TaskLocation location;

    /**
     * create a transaction from the Task and the location
     * @param task the task you want to update
     * @param location the location you want to set
     */
    public TaskLocationTransaction(Task task, TaskLocation location) {
        super(task, Task.class);
        this.location = location;
    }

    /**
     * set the location of the provided task
     * @param task the task you are changing the location of
     * @return true if successful, false otherwise.
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.setLocation(this.location);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
