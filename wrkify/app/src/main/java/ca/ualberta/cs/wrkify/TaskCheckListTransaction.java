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
 * TaskCheckListTransaction represents a Transaction for
 * Task.setCheckList()
 *
 * @see Transaction
 * @see Task
 * @see CheckList
 */
public class TaskCheckListTransaction extends Transaction<Task> {
    private CheckList checkList;

    /**
     * creates a Transaction from a task and a checkList
     * @param task the task you want to add a checklist to
     * @param checkList the checklist
     */
    public TaskCheckListTransaction(Task task, CheckList checkList) {
        super(task, Task.class);
        this.checkList = checkList;
    }

    /**
     * calls task.setCheckList();
     * @param task the task to be updated
     * @return true if sucessful(always);
     */
    @Override
    protected Boolean apply(Task task) {
        task.setCheckList(this.checkList);
        return true;
    }
}
