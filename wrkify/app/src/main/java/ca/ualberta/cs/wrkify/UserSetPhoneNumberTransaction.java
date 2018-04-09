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
 * Transaction to set a user's phone number.
 */
public class UserSetPhoneNumberTransaction extends StateChangeTransaction<User> {
    private String phoneNumber;

    /**
     * Creates a transaction to change the target user's phone number.
     * @param user Target user
     * @param phoneNumber New email address
     */
    public UserSetPhoneNumberTransaction(User user, String phoneNumber) {
        super(user, User.class);
        this.phoneNumber = phoneNumber;
    }

    /**
     * apply the setting of the users phone number
     * @param user the user to set
     * @return true if successful false otherwise
     */
    @Override
    protected Boolean apply(User user) {
        try {
            user.setPhoneNumber(phoneNumber);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
