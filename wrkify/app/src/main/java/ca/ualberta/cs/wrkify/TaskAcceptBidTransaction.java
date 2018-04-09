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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * TaskAcceptBidTransaction models accepting a bid as
 * a Transaction
 *
 * @see StateChangeTransaction
 * @see Task
 */

public class TaskAcceptBidTransaction extends StateChangeTransaction<Task> {
    private Bid bid;

    /**
     * creates a transaction with the bid to be accepted
     * @param task the task which will accept the bid
     * @param bid the bid the be accepted
     */
    public TaskAcceptBidTransaction(Task task, Bid bid) {
        super(task, Task.class);
        this.bid = bid;
    }

    /**
     * applys task.acceptBid() to the provides task
     * @param task the task you are modifying
     * @return true if bid accepted, false if not.
     */
    @Override
    protected Boolean apply(Task task) {
        try {
            task.acceptBid(this.bid);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    @NonNull
    @Override
    protected Signal[] generateSignals(CachingClient client, Task task) throws IOException {
        return new Signal[]{
                new Signal(bid.getRemoteBidder(client), Signal.SignalType.SIGNAL_ASSIGNED, task.getId(), task.getTitle())
        };
    }
}
