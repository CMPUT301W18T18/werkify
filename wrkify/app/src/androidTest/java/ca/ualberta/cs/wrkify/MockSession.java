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


import android.content.Context;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simplified session object for running UI tests.
 * The MockSession object implements most of the functionality of the
 * original Session object, but with no persistence - it will not
 * load or save a session file, and will only be logged in if
 * explicitly given a User object.
 */
class MockSession extends Session {
    private User user;
    private List<Task> mockUserProvidedCache = new ArrayList<>();
    private List<Task> mockUserRequestedCache = new ArrayList<>();
    private List<Task> mockUserBiddedCache = new ArrayList<>();

    /**
     * Creates a MockSession with the given User logged in.
     * @param user initial session user (null for logged out)
     */
    public MockSession(@Nullable User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    /**
     * Sets the session user. MockSession ignores the Context
     * parameter to {@link #setUser(User, Context)}, so this
     * method simply avoids needing to specify a value.
     * @param user User to log in as
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setUser(User user, Context context) {
        this.setUser(user);
    }

    /**
     * Unsets the session user. MockSession ignores the Context
     * parameter to {@link #logout(Context)}, so this method
     * simply avoids needing to specify a value.
     */
    public void logout() {
        this.user = null;
    }

    @Override
    public void logout(Context context) {
        this.logout();
    }

    @Override
    public void refreshCaches(RemoteClient client) throws IOException {
        // ignored
    }

    @Override
    public List<Task> getUserBiddedCache() {
        return mockUserBiddedCache;
    }

    @Override
    public List<Task> getUserRequestedCache() {
        return mockUserRequestedCache;
    }

    @Override
    public List<Task> getUserProvidedCache() {
        return mockUserProvidedCache;
    }

    /**
     * Sets the list of tasks to be returned by {@link #getUserBiddedCache()}.
     * @param mockUserBiddedCache new user bidded cache
     */
    public void setMockUserBiddedCache(List<Task> mockUserBiddedCache) {
        this.mockUserBiddedCache = mockUserBiddedCache;
    }

    /**
     * Sets the list of tasks to be returned by {@link #getUserProvidedCache()}.
     * @param mockUserProvidedCache new user provided cache
     */
    public void setMockUserProvidedCache(List<Task> mockUserProvidedCache) {
        this.mockUserProvidedCache = mockUserProvidedCache;
    }

    /**
     * Sets the list of tasks to be returned by {@link #getUserRequestedCache()}.
     * @param mockUserRequestedCache new user requested cache
     */
    public void setMockUserRequestedCache(List<Task> mockUserRequestedCache) {
        this.mockUserRequestedCache = mockUserRequestedCache;
    }
}
