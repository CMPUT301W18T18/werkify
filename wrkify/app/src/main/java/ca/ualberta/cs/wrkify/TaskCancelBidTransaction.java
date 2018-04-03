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
 * TaskCancelBidTransaction is the transaction
 * that models canceling a bid.
 *
 * @see Transaction
 * @see Task
 */

public class TaskCancelBidTransaction extends Transaction<Task> {
    private Bid bid;

    /**
     * creates a new Transaction that stores a bid to cancel
     * @param task the task the bid is to be removed from
     * @param bid the bid to remove
     */
    public TaskCancelBidTransaction(Task task, Bid bid) {
        super(task, Task.class);
        this.bid = bid;
    }

    /**
     * applys the cancelation of a bid
     * @param task the task you are canceling a bid from
     * @return always true
     */
    @Override
    protected Boolean application(Task task) {
        task.cancelBid(this.bid);
        return true;
    }
}
