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

/**
 * Created by peter on 18/03/18.
 */

public class Session {
    private static Session instance;

    private User user;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }

        if (instance.user == null) {
            instance.load();
        }

        return instance;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        save();
    }

    public void logout() {
        this.user = null;
        //TODO clear savefile;
    }

    private void save() {
        //TODO save the id
    }

    private void load() {
        //TODO reload the user from id
    }
}
