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
import java.util.HashMap;

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

    public Boolean flush(RemoteClient client) {
        while (queue.size() > 0) {
            try {
                Transaction tr = queue.get(0);

                RemoteObject obj = client.download(tr.getObject().getId(), tr.getObject().getClass());

                tr.apply(obj);

                client.upload(obj);
                queue.remove(0);
            } catch (IOException e) {
                return false;
            }

        }
        return true;
    }

    public Transaction pop() {
        Transaction tr = queue.get(0);
        queue.remove(0);
        return tr;
    }

}
