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


import android.os.AsyncTask;

public abstract class WatchableAsync<T> extends AsyncTask<Void, Void, T> {
    private AsyncWatcher<T> watcher;

    protected void setWatcher(AsyncWatcher<T> watcher) {
        this.watcher = watcher;
    }

    @Override
    protected void onPreExecute() {
        watcher.onStartWaiting();
    }

    @Override
    protected void onPostExecute(T result) {
        watcher.onFinishWaiting();
        watcher.onTaskFinished(result);
    }

    @Override
    protected void onCancelled() {
        watcher.onFinishWaiting();
        watcher.onTaskInterrupted();
    }
}
