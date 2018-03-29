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

public class LoginAsync extends WatchableAsync<User> {
    public static void startAsyncLogin(RemoteClient client, String username) {
        LoginAsync async = new LoginAsync();
        async.setUsername(username);
        async.setClient(client);

        async.execute();
    }

    private RemoteClient client;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(RemoteClient client) {
        this.client = client;
    }

    @Override
    protected User doInBackground(Void... nothing) {
        try {
            return Searcher.getUser(client, username);
        } catch (IOException e) {
            return null;
        }
    }
}
