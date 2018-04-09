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

import android.content.Context;
import android.os.AsyncTask;

/**
 * TransactionAsyncTask is an AsyncTask for applying as transaction
 * to an object.
 *
 * @see Transaction
 */
public class TransactionAsyncTask extends AsyncTask<Object, Void, Void> {

    /**
     * applies the transaction
     * @param objs the Task, Transaction, and context
     * @return unused
     */
    @Override
    protected Void doInBackground(Object... objs) {
        Task task = (Task) objs[0];
        Transaction<Task> transaction = (Transaction<Task>) objs[1];
        Context ctx = (Context) objs[2];

        TransactionManager transactionManager = Session.getInstance(ctx).getTransactionManager();
        transactionManager.enqueue(transaction);

        // TODO notify of offline status
        transactionManager.flush(WrkifyClient.getInstance());

        WrkifyClient.getInstance().updateCached(task);

        return null;
    }
}
