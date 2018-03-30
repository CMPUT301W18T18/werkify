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
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

abstract class AbstractIntentTest<T extends Activity> {
    protected abstract Class<T> getActivityClass();

    private MockRemoteClient client;
    private MockSession session;

    @Rule
    public IntentsTestRule<T> intentsTestRule =
            new IntentsTestRule<>(getActivityClass(), false, false);

    @Before
    public final void setup() {
        this.client = new MockRemoteClient();

        WrkifyClient.setInstance(new CachingClient<>(client));
        this.createMockData(client);

        this.session = new MockSession(getInitialSessionUser());
        Session.setInstance(session);

        if (shouldStartAutomatically()) {
            intentsTestRule.launchActivity(new Intent());
        }
    }

    public MockRemoteClient getClient() {
        return client;
    }

    public void mockNextSearch(RemoteObject... results) {
        this.client.mockNextSearch(results);
    }

    public MockSession getSession() {
        return session;
    }

    public void launchActivity(Intent intent) {
        this.intentsTestRule.launchActivity(intent);
    }

    public Activity getActivity() {
        return this.intentsTestRule.getActivity();
    }

    public Instrumentation.ActivityResult getActivityResult() {
        return this.intentsTestRule.getActivityResult();
    }

    public int getActivityResultCode() {
        return getActivityResult().getResultCode();
    }

    public Object getActivityResultExtra(String name) {
        return getActivityResult().getResultData().getSerializableExtra(name);
    }

    public void assertActivityFinished() {
        assertTrue("Expected activity to finish", getActivity().isFinishing());
    }

    public void assertActivityNotFinished() {
        assertFalse("Expected activity to be running", getActivity().isFinishing());
    }

    public ComponentName component(Class aClass) {
        return new ComponentName(getActivity(), aClass);
    }

    protected boolean shouldStartAutomatically() {
        return true;
    }

    protected User getInitialSessionUser() {
        return null;
    }

    protected void createMockData(MockRemoteClient client) {

    }
}
