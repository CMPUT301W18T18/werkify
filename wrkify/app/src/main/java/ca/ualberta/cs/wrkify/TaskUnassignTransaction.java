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

/**
 * Created by peter on 03/04/18.
 */

public class TaskUnassignTransaction extends StateChangeTransaction<Task> {

    /**
     * creates a transaction to unassign the assigned
     * bid.
     * @param task the task to change
     */
    public TaskUnassignTransaction(Task task) {
        super(task, Task.class);
    }

    /**
     * applys the unassigning of the task
     * @param task the task to change.
     * @return true if the task unassigned, false otherwise.
     */
    @Override
    protected Boolean apply(Task task) {
        try {
          task.unassign();
          return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
