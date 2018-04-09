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
import java.util.ArrayList;

/**
 * TransactionManager is a queue of Transactions
 *
 * @see Transaction
 */
public class TransactionManager {
    private ArrayList<Transaction> queue;

    /**
     * creates a TransactionManager
     */
    public TransactionManager() {
        queue = new ArrayList<Transaction>();
    }

    /**
     * add a transaction to the queue
     * @param tr the transaction to add
     */
    public void enqueue(Transaction tr) {
        queue.add(tr);
    }

    /**
     * flush the queue, applying all transactions untill the
     * CachingClient stops working.
     * @param client the client
     * @return true if flushed, false otherwise
     */
    public Boolean flush(CachingClient client) {
        while (queue.size() > 0) {
            try {
                Transaction tr = queue.get(0);
                tr.applyInClient(client);
                queue.remove(0);
            } catch (IOException e) {
                Log.w("transaction", "Halting flush due to IOException:");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean hasPendingTransactions() {
        return !queue.isEmpty();
    }

    /**
     * checks if an object has a pending transaction
     * @param object the remote object to check
     * @return true if pending, false otherwise
     */
    public boolean hasPendingTransactionsFor(RemoteObject object) {
        // TODO this seems inefficient
        for (Transaction transaction: queue) {
            if (transaction.getId().equals(object.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove the next transaction from the queue without removing it
     * @return the transaction on the queue.
     */
    public Transaction pop() {
        Transaction tr = queue.get(0);
        queue.remove(0);
        return tr;
    }
}
