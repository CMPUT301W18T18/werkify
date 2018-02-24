/*
 *  Copyright 2018 CMPUT301W18T18
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import static junit.framework.Assert.assertEquals;

public class ConcreteUserTest {

    public void testConstructor() {
        String username = "UserNameHere";
        String email = "name@website.com";
        String phoneNumber = "780-123-4567";

        ConcreteUser user = new ConcreteUser(username, email, phoneNumber);

        String resultUsername = user.getUsername();
        String resultEmail = user.getEmail();
        String resultPhoneNumber = user.getPhoneNumber();

        assertEquals(resultUsername, username);
        assertEquals(resultEmail, email);
        assertEquals(resultPhoneNumber, "(780) 123-4567");
    }


    public void testUsernames() throws Exception {
        String allowed = "abcdefg1234567890hijklmn"; //This is 24 characters
        String disallowed = "abcdefg1234567890hijklmno"; //This is 25 characters

        String email = "name@website.com";
        String phoneNumber = "587-987-7654";

        ConcreteUser allowedUser = new ConcreteUser(allowed, email, phoneNumber);

        boolean failed = false;

        try {
            ConcreteUser disallowedUser = new ConcreteUser(disallowed, email, phoneNumber);
        } catch (IllegalArgumentException e) {
            failed = true;
        }

        assertEquals(failed, true);
    }

    public void testGettersAndSetters(){

    }





}
