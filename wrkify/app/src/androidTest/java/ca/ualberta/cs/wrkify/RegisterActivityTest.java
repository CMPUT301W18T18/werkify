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

import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Tests for RegisterActivity.
 */
@IntentTest
public class RegisterActivityTest extends AbstractIntentTest<RegisterActivity> {
    private User existingUser;

    @Override
    protected Class<RegisterActivity> getActivityClass() {
        return RegisterActivity.class;
    }

    @Override
    protected void createMockData(CachingClient<MockRemoteClient> client) {
        existingUser = client.create(User.class, "existingUser", "a@a", "1840392743");
    }

    /**
     * Utility method to enter user information and press the Register button.
     * @param username Username to enter
     * @param email Email to enter
     * @param phoneNumber Phone number to enter
     */
    private void registerWith(String username, String email, String phoneNumber) {
        onView(withId(R.id.registerField)).perform(typeText(username));
        onView(withId(R.id.registerEmail)).perform(typeText(email));
        onView(withId(R.id.registerPhoneNumber)).perform(typeText(phoneNumber));
        onView(withId(R.id.registerButton)).perform(click());
    }

    /**
     * Register a new user.
     * Should: create new user
     *         finish activity with RESULT_OK
     */
    @Test
    public void testRegister() {
        registerWith("newUser", "new-user@example.com", "1938059920");

        User sessionUser = getSession().getUser();
        assertEquals("newUser", sessionUser.getUsername());
        assertEquals("new-user@example.com", sessionUser.getEmail());
        assertEquals("1938059920", sessionUser.getPhoneNumber());

        assertActivityFinished();
        assertEquals(RegisterActivity.RESULT_OK, getActivityResultCode());
    }

    /**
     * Register with empty username.
     * Should: fail
     */
    @Test
    public void testEmptyUsername() {
        registerWith("", "new-user@example.com", "1938059920");

        assertNull(getSession().getUser());
        assertActivityNotFinished();
    }

    /**
     * Register with empty email.
     * Should: fail
     */
    @Test
    public void testEmptyEmail() {
        registerWith("newUser", "", "1938059920");

        assertNull(getSession().getUser());
        assertActivityNotFinished();
    }

    /**
     * Register with empty phone number.
     * Should: fail
     */
    @Test
    public void testEmptyPhoneNumber() {
        registerWith("newUser", "new-user@example.com", "");

        assertNull(getSession().getUser());
        assertActivityNotFinished();
    }

    /**
     * Register with invalid email.
     * Should: fail
     */
    @Test
    public void testInvalidEmail() {
        registerWith("newUser", "not a valid email", "1938059920");

        assertNull(getSession().getUser());
        assertActivityNotFinished();
    }

    /**
     * Register with an already-registered username.
     * Should: fail
     */
    @Test
    public void testUnavailableUsername() {
        registerWith("existingUser", "new-user@example.com", "1938059920");

        assertNull(getSession().getUser());
        assertActivityNotFinished();
    }
}
