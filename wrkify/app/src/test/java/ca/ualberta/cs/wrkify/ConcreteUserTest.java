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

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * unit tests for the ConcreteUser class
 *
 * @author Taylor Folkersen
 * @see ConcreteUser
 */
public class ConcreteUserTest {

    @Test
    public void testConstructor() throws Exception {
        String username = "UserNameHere";
        String email = "name@website.com";
        String phoneNumber = "780-123-4567";

        ConcreteUser user = new ConcreteUser(username, email, phoneNumber);
        String resultUsername = user.getUsername();
        String resultEmail = user.getEmail();
        String resultPhoneNumber = user.getPhoneNumber();

        assertEquals(resultUsername, username);
        assertEquals(resultEmail, email);
        assertEquals(resultPhoneNumber, "7801234567");
    }

    @Test
    public void testUsernames() throws Exception {
        String allowed = "abcdefg1234567890hijklmn"; //This is 24 characters
        String disallowed = "abcdefg1234567890hijklmno"; //This is 25 characters

        String email = "name@website.com";
        String phoneNumber = "(587)-987-7654";


        ConcreteUser allowedUser = new ConcreteUser(allowed, email, phoneNumber);

        boolean failed = false;

        try {
            ConcreteUser disallowedUser = new ConcreteUser(disallowed, email, phoneNumber);
        } catch (IllegalArgumentException e) {
            failed = true;
        }

        assertEquals(failed, true);
    }

    @Test
    public void testGettersAndSetters() throws Exception {
        String A_username = "AllowedName";
        String A2_username = "AnotherName";
        String D_username = "Disallowed Name";
        String D2_username = "";

        String email1 = "name@website.ca";
        String email2 = "name2@website.gov";

        String phoneNumber1 = "(780)-562-9801";
        String phoneNumber2 = "123.456.7643";
        String phoneNumber3 = "123-567-5232";

        ConcreteUser user = new ConcreteUser(A_username, email1, phoneNumber1);

        user.setUsername(A2_username);
        assertEquals(user.getUsername(), A2_username);

        boolean failed = false;
        try {
            user.setUsername(D_username);
        } catch (IllegalArgumentException e) {
            failed = true;
        }

        assertEquals(failed, true);
        assertEquals(user.getUsername(), A2_username);
        failed = false;

        try {
            user.setUsername(D2_username);
        } catch (IllegalArgumentException e) {
            failed = true;
        }
        assertEquals(failed, true);
        assertEquals(user.getUsername(), A2_username);

        user.setEmail(email2);
        assertEquals(user.getEmail(), email2);

        user.setPhoneNumber(phoneNumber2);
        assertEquals(user.getPhoneNumber(), "1234567643");

        user.setPhoneNumber(phoneNumber3);
        assertEquals(user.getPhoneNumber(), "1235675232");
    }

}
