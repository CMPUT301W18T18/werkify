/* Copyright 2018 CMPUT301W18T18
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
 */
package ca.ualberta.cs.wrkify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConcreteUser is the java implementation of User
 * TODO: add restrictions to setters
 *
 * @see User
 */

public class ConcreteUser implements User {
    private String username;
    private String email;
    private String phoneNumber;

    // from https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/email
    // (2018-02-26)
    private final String emailRegex = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@"
            + "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\."
            + "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";


    public ConcreteUser(String username, String email, String phoneNumber) throws IllegalArgumentException {

        InternalSetUsername(username);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * InternalSetUsername provides a final implementation of SetUsername
     * that both the constructor and setUsername can use.
     *
     * it enforces the following restrictions:
     * - username contains no whitespace
     * - username is less than 24 characters
     * - username is not empty
     *
     * @param username
     * @throws IllegalArgumentException when restrictions are violated.
     */
    private final void InternalSetUsername(String username) throws IllegalArgumentException {
        // from https://stackoverflow.com/questions/4067809/ (2018-02-26)
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(username);

        if (matcher.find()) {
            throw new IllegalArgumentException("username cannot contain whitespace");
        } else if (username.length() > 24) {
            throw new IllegalArgumentException("username too long");
        } else if (username.isEmpty()) {
            throw new IllegalArgumentException("username cannot be empty");
        } else {
            this.username = username;
        }
    }

    /**
     * InternalSetEmail provides a final implementation of setEmail that
     * both the constructor and setEmail can use.
     *
     * the email verification used is from the w3c standard for
     * html email fields.
     *
     * @param email
     * @throws IllegalArgumentException when email is not an email
     */
    private final void InternalSetEmail(String email) throws IllegalArgumentException {
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("email does not appear to be valid");
        } else {
            this.email = email;
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * SetUsername sets the username via InternalSetUsername
     *
     * it enforces the following restrictions:
     * - username contains no whitespace
     * - username is less than 24 characters
     * - username is not empty
     *
     * @param username
     * @throws IllegalArgumentException when restrictions are violated.
     */
    public void setUsername(String username) throws IllegalArgumentException {
        InternalSetUsername(username);
    }

    /**
     * SetEmail sets the email via InternalSetEmail
     *
     * the email verification used is from the w3c standard for
     * html email fields.
     *
     * @param email
     * @throws IllegalArgumentException when email is not an email
     */
    public void setEmail(String email) throws IllegalArgumentException {
        InternalSetEmail(email);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
