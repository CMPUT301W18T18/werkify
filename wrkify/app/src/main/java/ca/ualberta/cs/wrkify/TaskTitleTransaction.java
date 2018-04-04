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
 * TaskTitleTransaction represents a Transaction
 * that changes the title of a task.
 *
 * @see Task
 * @see Transaction
 */

public class TaskTitleTransaction extends Transaction<Task> {
    private String title;

    /**
     * create a Transaction with the task and the title.
     *
     * @param task the task that the transaction should be applied to
     * @param title the title you want to set
     */
    public TaskTitleTransaction(Task task, String title) {
        super(task, Task.class);
        this.title = title;
    }

    /**
     * apply is called by applyTo to set the title.
     * @param task the task you are applying to
     * @return true if successful, false otherwise
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.setTitle(this.title);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
