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

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * Tests for ViewProfileActivity.
 */
@IntentTest
public class ViewProfileActivityTest extends AbstractIntentTest<ViewProfileActivity> {
    private User user1;
    private User user2;

    @Override
    protected Class<ViewProfileActivity> getActivityClass() {
        return ViewProfileActivity.class;
    }

    @Override
    protected User getInitialSessionUser() {
        return user1;
    }

    @Override
    protected boolean shouldStartAutomatically() {
        return false;
    }

    @Override
    protected void createMockData(CachingClient<MockRemoteClient> client) {
        user1 = client.create(User.class, "User1", "user1@example.com", "1740382957");
        user2 = client.create(User.class, "User2", "user2@example.com", "5830275947");
    }

    /**
     * Show another user's profile.
     * Should: show their information
     *         hide the edit button
     */
    @Test
    public void testViewProfileActivity() {
        Intent intent = new Intent();
        intent.putExtra(ViewProfileActivity.USER_EXTRA, user2);
        launchActivity(intent);

        onView(withId(R.id.UserName)).check(matches(withText("User2")));
        onView(withId(R.id.email)).check(matches(withText("user2@example.com")));
        onView(withId(R.id.PhoneNumber)).check(matches(withText("5830275947")));

        onView(withId(R.id.editButton)).check(matches(not(isDisplayed())));
    }

    /**
     * Show your own profile.
     * Should: show your information
     *         show the edit button
     *         launch EditProfileActivity when the edit button is clicked
     */
    @Test
    public void testViewOwnProfile() {
        Intent intent = new Intent();
        intent.putExtra(ViewProfileActivity.USER_EXTRA, user1);
        launchActivity(intent);

        onView(withId(R.id.UserName)).check(matches(withText("User1")));
        onView(withId(R.id.email)).check(matches(withText("user1@example.com")));
        onView(withId(R.id.PhoneNumber)).check(matches(withText("1740382957")));

        onView(withId(R.id.editButton)).check(matches(isDisplayed()));
        onView(withId(R.id.editButton)).perform(click());

        intended(allOf(
                hasComponent(component(EditProfileActivity.class)),
                hasExtra(EditProfileActivity.EXTRA_TARGET_USER, user1)));
    }
}
