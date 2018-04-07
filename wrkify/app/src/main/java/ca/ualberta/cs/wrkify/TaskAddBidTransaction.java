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
 * TaskAddBidTransaction models adding a bid to
 * a task.
 *
 * @see Task
 * @see Transaction
 */

public class TaskAddBidTransaction extends Transaction<Task> {
    private Bid bid;

    /**
     * create a TaskAddBidTransaction with a bid to add
     * @param task the task your are adding a bid to
     * @param bid the bid you are adding to task
     */
    public TaskAddBidTransaction(Task task, Bid bid) {
        super(task, Task.class);
        this.bid = bid;
    }

    /**
     * adds the bid the provided task
     * @param task the task you are adding the bid to
     * @return true if bid could be added, false otherwise
     */
    protected Boolean apply(Task task) {
        try {
            task.addBid(this.bid);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
