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

class MockSession extends Session {
    private User user;
    private List<Task> mockUserProvidedCache = new ArrayList<>();
    private List<Task> mockUserRequestedCache = new ArrayList<>();
    private List<Task> mockUserBiddedCache = new ArrayList<>();

    public MockSession(@Nullable User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setUser(User user, Context context) {
        this.setUser(user);
    }

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

    public void setMockUserBiddedCache(List<Task> mockUserBiddedCache) {
        this.mockUserBiddedCache = mockUserBiddedCache;
    }

    public void setMockUserProvidedCache(List<Task> mockUserProvidedCache) {
        this.mockUserProvidedCache = mockUserProvidedCache;
    }

    public void setMockUserRequestedCache(List<Task> mockUserRequestedCache) {
        this.mockUserRequestedCache = mockUserRequestedCache;
    }
}
