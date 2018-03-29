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


import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.intent.Intents.*;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests for LoginActivity.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setupClient() {
        WrkifyClient.setRemoteClient(new MockRemoteClient());
        WrkifyClient.getInstance().create(User.class, "user4", "user4@example.com", "5018293749");
    }

    /**
     * Log in as a user that exists.
     */
    @Test
    public void testLoginActivity() {
        onView(withId(R.id.loginField)).perform(typeText("user4"));
        onView(withId(R.id.loginButton)).perform(click());

        assertEquals("user4", Session.getInstance(intentsTestRule.getActivity()).getUser().getUsername());
        intended(hasComponent(new ComponentName(intentsTestRule.getActivity(), MainActivity.class)));
    }

    /**
     * Login as a user that does not exist
     */
    @Test
    public void testLoginFail() {
        onView(withId(R.id.loginField)).perform(typeText("user5"));
        onView(withId(R.id.loginButton)).perform(click());

        
    }
}
