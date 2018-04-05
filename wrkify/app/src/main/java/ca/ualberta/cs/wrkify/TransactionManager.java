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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by peter on 25/03/18.
 */

public class TransactionManager {
    private ArrayList<Transaction> queue;

    public TransactionManager() {
        queue = new ArrayList<Transaction>();
    }

    public void enqueue(Transaction tr) {
        queue.add(tr);
    }

    public Boolean flush(CachingClient client) {
        while (queue.size() > 0) {
            try {
                Transaction tr = queue.get(0);
                tr.applyInClient(client);
                queue.remove(0);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPendingTransactions() {
        return !queue.isEmpty();
    }

    public boolean hasPendingTransactionsFor(RemoteObject object) {
        // TODO this seems inefficient
        for (Transaction transaction: queue) {
            if (transaction.getId().equals(object.getId())) {
                return true;
            }
        }
        return false;
    }

    public Transaction pop() {
        Transaction tr = queue.get(0);
        queue.remove(0);
        return tr;
    }
}
