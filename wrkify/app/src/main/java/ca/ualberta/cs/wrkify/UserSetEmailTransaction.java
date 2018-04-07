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
 * Transaction to set a user's email address.
 */
public class UserSetEmailTransaction extends StateChangeTransaction<User> {
    private String email;

    /**
     * Creates a transaction to change the target user's email address.
     * @param user Target user
     * @param email New email address
     */
    public UserSetEmailTransaction(User user, String email) {
        super(user, User.class);
        this.email = email;
    }

    @Override
    protected Boolean apply(User user) {
        try {
            user.setEmail(email);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
