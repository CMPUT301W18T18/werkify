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

import android.content.Intent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Tests for EditProfileActivity.
 */
@IntentTest
public class EditProfileActivityTest extends AbstractIntentTest<EditProfileActivity> {
    private User user;

    @Override
    protected Class<EditProfileActivity> getActivityClass() {
        return EditProfileActivity.class;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Override
    protected User getInitialSessionUser() {
        return user;
    }

    @Override
    protected void createMockData(CachingClient<MockRemoteClient> client) {
        user = client.create(User.class, "EditingUser", "editing-user@example.com", "5043081024");
    }

    @Before
    public void startActivity() {
        Intent intent = new Intent();
        intent.putExtra(EditProfileActivity.EXTRA_TARGET_USER, user);
        launchActivity(intent);
    }

    /**
     * Edit contact information.
     * Should: show current information
     *         save changed information
     */
    @Test
    public void testEditProfileActivity() {
        onView(withId(R.id.editProfileUsername)).check(matches(withText("EditingUser")));
        onView(withId(R.id.editProfileEmailField)).check(matches(withText("editing-user@example.com")));
        onView(withId(R.id.editProfilePhoneField)).check(matches(withText("5043081024")));

        onView(withId(R.id.editProfileEmailField)).perform(clearText(), typeText("edited-user@example.com"));
        onView(withId(R.id.editProfilePhoneField)).perform(clearText(), typeText("4018390284"));

        onView(withId(R.id.menuItemSaveProfile)).perform(click());

        assertActivityFinished();
        assertEquals(EditProfileActivity.RESULT_OK, getActivityResultCode());

        User returnedUser = (User) getActivityResultExtra(EditProfileActivity.EXTRA_RETURNED_USER);

        assertNotNull(returnedUser);
        assertEquals("EditingUser", returnedUser.getUsername());
        assertEquals("edited-user@example.com", returnedUser.getEmail());
        assertEquals("4018390284", returnedUser.getPhoneNumber());
    }

    /**
     * Set an empty email address.
     * Should: fail
     */
    @Test
    public void testEmptyEmail() {
        onView(withId(R.id.editProfileEmailField)).perform(clearText());
        onView(withId(R.id.menuItemSaveProfile)).perform(click());

        assertActivityNotFinished();
    }

    /**
     * Set an empty phone number.
     * Should: fail
     */
    @Test
    public void testEmptyPhoneNumber() {
        onView(withId(R.id.editProfilePhoneField)).perform(clearText());
        onView(withId(R.id.menuItemSaveProfile)).perform(click());

        assertActivityNotFinished();
    }

    /**
     * Set an invalid email address.
     * Should: fail
     */
    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.editProfileEmailField)).perform(clearText(), typeText("not a valid email"));
        onView(withId(R.id.menuItemSaveProfile)).perform(click());

        assertActivityNotFinished();
    }
}
