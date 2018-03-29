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


public class RegisterAsync extends WatchableAsync<User> {
    public static void startAsyncRegister(RemoteClient client, Object... userArgs) {
        RegisterAsync async = new RegisterAsync();
        async.setClient(client);
        async.setUserArgs(userArgs);

        async.execute();
    }

    private RemoteClient client;
    private Object[] userArgs;

    private void setClient(RemoteClient client) {
        this.client = client;
    }

    private void setUserArgs(Object[] userArgs) {
        this.userArgs = userArgs;
    }

    @Override
    protected User doInBackground(Void... nothing) {
        return client.create(User.class, userArgs);
    }
}
