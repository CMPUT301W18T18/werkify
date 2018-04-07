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
 * TaskCompleteTransaction provides a Transaction for the
 * Task.complete() operation.
 *
 * @see StateChangeTransaction
 * @see Task
 */

public class TaskCompleteTransaction extends StateChangeTransaction<Task> {

    /**
     * create a transaction to complete task
     * @param task the task to complete
     */
    public TaskCompleteTransaction(Task task) {
        super(task, Task.class);
    }

    /**
     * applys the completeion of task
     * @param task the task to mark as completed
     * @return true if can be completed, false otherwise
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.complete();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
