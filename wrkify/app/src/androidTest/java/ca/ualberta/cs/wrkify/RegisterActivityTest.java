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

import android.app.Activity;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for RegisterActivity.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest extends AbstractIntentTest<RegisterActivity> {
    private User existingUser;

    @Override
    protected Class<RegisterActivity> getActivityClass() {
        return RegisterActivity.class;
    }

    @Override
    protected void createMockData() {
        this.existingUser = WrkifyClient.getInstance().create(User.class, "existingUser", "a@a", "1840392743");
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
     * Utility method to get the session user.
     * @return Session user
     */
    private User getSessionUser() {
        return Session.getInstance(intentsTestRule.getActivity()).getUser();
    }

    /**
     * Register a new user.
     * Should: create new user
     *         finish activity with RESULT_OK
     */
    @Test
    public void testRegister() {
        client.mockNextSearch();

        registerWith("newUser", "new-user@example.com", "1938059920");

        User sessionUser = getSessionUser();
        assertEquals("newUser", sessionUser.getUsername());
        assertEquals("new-user@example.com", sessionUser.getEmail());
        assertEquals("1938059920", sessionUser.getPhoneNumber());

        assertTrue(intentsTestRule.getActivity().isFinishing());
        assertEquals(Activity.RESULT_OK, intentsTestRule.getActivityResult().getResultCode());
    }

    /**
     * Register with empty username.
     * Should: fail
     */
    @Test
    public void testEmptyUsername() {
        registerWith("", "new-user@example.com", "1938059920");

        assertNull(getSessionUser());
        assertFalse(intentsTestRule.getActivity().isFinishing());
    }

    /**
     * Register with empty email.
     * Should: fail
     */
    @Test
    public void testEmptyEmail() {
        registerWith("newUser", "", "1938059920");

        assertNull(getSessionUser());
        assertFalse(intentsTestRule.getActivity().isFinishing());
    }

    /**
     * Register with empty phone number.
     * Should: fail
     */
    @Test
    public void testEmptyPhoneNumber() {
        registerWith("newUser", "new-user@example.com", "");

        assertNull(getSessionUser());
        assertFalse(intentsTestRule.getActivity().isFinishing());
    }

    /**
     * Register with invalid email.
     * Should: fail
     */
    @Test
    public void testInvalidEmail() {
        registerWith("newUser", "not a valid email", "1938059920");

        assertNull(getSessionUser());
        assertFalse(intentsTestRule.getActivity().isFinishing());
    }

    /**
     * Register with an already-registered username.
     * Should: fail
     */
    @Test
    public void testUnavailableUsername() {
        client.mockNextSearch(existingUser);

        registerWith("existingUser", "new-user@example.com", "1938059920");

        assertNull(getSessionUser());
        assertFalse(intentsTestRule.getActivity().isFinishing());
    }
}
