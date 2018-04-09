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
import java.util.ArrayList;
import java.util.List;

/**
 * TaskAddOrReplaceBidTransaction models adding a bid to
 * a task, replacing the bidding user's existing bid if it exists.
 *
 * @see Task
 * @see StateChangeTransaction
 */

public class TaskAddOrReplaceBidTransaction extends StateChangeTransaction<Task> {
    private Bid bid;
    private Bid replaceTarget;

    /**
     * create a TaskAddOrReplaceBidTransaction with a bid to add
     * @param task the task your are adding a bid to
     * @param bid the bid you are adding to task
     */
    public TaskAddOrReplaceBidTransaction(Task task, Bid replaceTarget, Bid bid) {
        super(task, Task.class);
        this.bid = bid;
        this.replaceTarget = replaceTarget;
    }

    /**
     * adds the bid the provided task
     * @param task the task you are adding the bid to
     * @return true if bid could be added, false otherwise
     */
    protected Boolean apply(Task task) {
        try {
            task.replaceBid(replaceTarget, bid);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    @NonNull
    @Override
    protected Signal[] generateSignals(CachingClient client, Task task) throws IOException {
        ArrayList<Signal> signalList = new ArrayList<>();
        signalList.add(new Signal(task.getRemoteRequester(client), Signal.SignalType.SIGNAL_NEW_BID, task.getId(), task.getTitle()));
        if (task.getBidList().size() > 0) {
            signalList.add(new Signal(task.getBidList().get(0).getRemoteBidder(client), Signal.SignalType.SIGNAL_OUTBID, task.getId(), task.getTitle()));
        }

        return signalList.toArray(new Signal[]{});
    }
}
