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

public class RefreshAsync extends WatchableAsync<Boolean> {
    public static void startAsyncRefresh(Session session, AsyncWatcher<Boolean> watcher) {
        RefreshAsync async = new RefreshAsync();
        async.setSession(session);
        async.setWatcher(watcher);

        async.execute();
    }

    private Session session;

    private void setSession(Session session) {
        this.session = session;
    }

    @Override
    protected Boolean doInBackground(Void... nothing) {
        try {
            session.refreshCaches(WrkifyClient.getInstance());
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
