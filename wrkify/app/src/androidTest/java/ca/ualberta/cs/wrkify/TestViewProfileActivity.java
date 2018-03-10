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
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

/**
 * Empty test to run ViewProfileActivity
 */

public class TestViewProfileActivity {
    @Rule
    public ActivityTestRule<ViewProfileActivity> activityTestRule = new ActivityTestRule<>(
            ViewProfileActivity.class, false, false);

    @Test
    public void testViewProfileActivity() {
        Intent intent = new Intent();

        ConcreteUser user = new ConcreteUser("user1", "user1@example.com", "(555) 555-5555");

        intent.putExtra(ViewProfileActivity.USER_EXTRA, user);
        activityTestRule.launchActivity(intent);
    }
}
