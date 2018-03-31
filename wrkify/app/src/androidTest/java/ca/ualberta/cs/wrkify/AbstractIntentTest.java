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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;

import java.io.Serializable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Framework superclass for an intent test. Contains common setup
 * functionality and various simplifying utility methods for
 * intent tests.
 * @param <T> Class of the activity being tested by this intent test
 */
abstract class AbstractIntentTest<T extends Activity> {
    private MockRemoteClient client;
    private MockSession session;

    @Rule
    public IntentsTestRule<T> intentsTestRule =
            new IntentsTestRule<>(getActivityClass(), false, false);

    /**
     * Common intent test setup.
     * Sets mock objects for the global session and client,
     * and initializes them as specified by createMockData()
     * and getInitialSessionUser(). The activity will also be
     * started automatically here if shouldStartAutomatically()
     * returns true. Specific intent tests should override the
     * aformentioned template methods, not this method.
     */
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

    /**
     * Gets the active MockRemoteClient.
     * @return active client
     */
    public final @NonNull MockRemoteClient getClient() {
        return client;
    }

    /**
     * Informs the MockRemoteClient to return the given RemoteObjects
     * as the results of the next search. This should always be called
     * before taking any action that will trigger a search, as MockRemoteClient
     * cannot interpret real searches and will always return no results.
     * @param results RemoteObjects to return as the results of the next search
     */
    public final void mockNextSearch(RemoteObject... results) {
        getClient().mockNextSearch(results);
    }

    /**
     * Gets the active MockSession.
     * @return active session
     */
    public final @NonNull MockSession getSession() {
        return session;
    }

    /**
     * Launches the activity being tested. (This is a wrapper
     * around IntentsTestRule.launchActivity.)
     * @param intent Intent to launch the activity with
     */
    public final void launchActivity(Intent intent) {
        this.intentsTestRule.launchActivity(intent);
    }

    /**
     * Launches the tested activity with an empty Intent.
     */
    public final void launchActivity() {
        launchActivity(new Intent());
    }

    /**
     * Gets the running activity. (This is a wrapper around
     * IntentsTestRule.getActivity.)
     * @return running activity
     */
    public final Activity getActivity() {
        return this.intentsTestRule.getActivity();
    }

    /**
     * Gets the result of the test activity. (This is a wrapper
     * around IntentsTestRule.getActivityResult.)
     * @return ActivityResult of test activity
     */
    public final Instrumentation.ActivityResult getActivityResult() {
        return this.intentsTestRule.getActivityResult();
    }

    /**
     * Gets the result code returned by the test activity.
     * @return result code
     */
    public final int getActivityResultCode() {
        return getActivityResult().getResultCode();
    }

    /**
     * Gets a Serializable extra from the test activity's
     * returned data.
     * @param name Extra name to get from data
     * @return Serializable content of requested Extra
     */
    public final Serializable getActivityResultExtra(String name) {
        return getActivityResult().getResultData().getSerializableExtra(name);
    }

    /**
     * Asserts that the test activity should have exited.
     */
    public final void assertActivityFinished() {
        assertTrue("Expected activity to finish", getActivity().isFinishing());
    }

    /**
     * Asserts that the test activity should not have exited, and
     * should still be running.
     */
    public final void assertActivityNotFinished() {
        assertFalse("Expected activity to be running", getActivity().isFinishing());
    }

    /**
     * Utility method to generate a ComponentName from only a Class,
     * using the running activity as the Context.
     * @param aClass Class to make ComponentName for
     * @return ComponentName of class, with running activity as context
     */
    public final ComponentName component(Class aClass) {
        return new ComponentName(getActivity(), aClass);
    }

    /**
     * Gets the Activity that this intent test will test.
     * This should always return the class corresponding to the
     * type parameter of the AbstractIntentTest.
     * @return Activity class being tested
     */
    protected abstract Class<T> getActivityClass();

    /**
     * Template method to determine whether the tested activity
     * should start before running each test or not.
     * @return true to start before each test; false to not start
     */
    protected boolean shouldStartAutomatically() {
        return true;
    }

    /**
     * Template method to determine the initial session user
     * that will be used to create the MockSession. This user
     * will be the session user for the test activity unless
     * and until the activity or test changes the session user.
     * Returning null will create a logged-out MockSession.
     * The session user will be reset before each test.
     * Note that this method should always be called after
     * createMockData for each test, so it can return a User
     * that was created there.
     * @return User to use as session user
     */
    protected @Nullable User getInitialSessionUser() {
        return null;
    }

    /**
     * Template method that will be called during MockRemoteClient
     * initialization for each test. The test can use this method
     * to create test data that will be the same for each test
     * (likely by calling client.create(...)).
     * Note that the client is recreated for each test, so the results
     * of this method applied to an empty client should always
     * constitute the entire state of the client at the start of
     * each test.
     * @param client current MockRemoteClient
     */
    protected void createMockData(MockRemoteClient client) {

    }
}
