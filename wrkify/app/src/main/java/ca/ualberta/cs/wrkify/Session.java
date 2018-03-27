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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Session is a global singleton that holds a reference
 * to the user that is currently logged in. It automatically
 * saves and restores a user identity from file when the app
 * is restarted.
 */
public class Session implements Serializable {
    private static final String FILENAME = "ca.ualberta.cs.wrkify.Session";
    private static Session instance;

    /**
     * Preserves Session to file
     * @param context application context; used to write file
     */
    private static void save(Context context, Session session) {
        // this is adapted from the lab 26-01-2018
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(session, out);

            out.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Loads Session from file and gets user data from RemoteClient
     * @param context application context; used to read file
     * @param client RemoteClient; used to retrieve user data
     */
    private static Session load(Context context, RemoteClient client) {
        Session session = new Session();

        // this is adapted from the lab 26-01-2018
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            session = gson.fromJson(in, Session.class);
        } catch (FileNotFoundException e) {
            session.user = null;
        }

        return session;
    }

    private User user;
    private List<Task> userRequestedCache;
    private List<Task> userProvidedCache;
    private List<Task> userBiddedCache;

    private transient NotificationCollector notificationCollector = new NotificationCollector();
    private ProvidedTaskNotifier providedTaskNotifier = new ProvidedTaskNotifier();
    private RequestedTaskNotifier requestedTaskNotifier = new RequestedTaskNotifier();

    private Session() {}

    /**
     * Gets the global Session.
     * @param context application context; used to restore a preserved Session
     * @param client RemoteClient to load user data from
     * @return global Session object
     */
    public static Session getInstance(Context context, RemoteClient client) {
        if (instance == null) {
            instance = load(context, client);
        }
        return instance;
    }

    /**
     * Gets the global Session using the default WrkifyClient.
     * @param context application context; used to restore a preserved Session
     * @return global Session object
     */
    public static Session getInstance(Context context) {
        return getInstance(context, WrkifyClient.getInstance());
    }

    /**
     * Gets the logged-in user.
     * @return logged-in user
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Sets the logged-in user.
     * @param user User to set as the session user
     * @param context application context; used to save Session
     */
    public void setUser(User user, Context context) {
        this.user = user;

        save(context, this);
    }

    /**
     * Unsets the logged-in user.
     * @param context application context; used to clear saved Session
     */
    public void logout(Context context) {
        this.user = null;
        context.deleteFile(FILENAME);
    }

    /**
     * Refreshes user task caches.
     * @param client RemoteClient to find user's tasks in
     * @throws IOException if network is disconnected
     */
    public void refreshCaches(RemoteClient client) throws IOException {
        this.userProvidedCache = Searcher.findTasksByProvider(client, this.user);
        this.userRequestedCache = Searcher.findTasksByRequester(client, this.user);
        this.userBiddedCache = Searcher.findTasksByBidder(client, this.user);

        this.notificationCollector.putNotifications(this.providedTaskNotifier.generateNotifications(this.userProvidedCache));
        this.notificationCollector.putNotifications(this.providedTaskNotifier.generateNotifications(this.userBiddedCache));
        this.notificationCollector.putNotifications(this.requestedTaskNotifier.generateNotifications(this.userRequestedCache));
    }

    /**
     * Gets the cache of the user's requested tasks.
     * @return cached list of requested tasks
     */
    public List<Task> getUserRequestedCache() {
        return userRequestedCache;
    }

    /**
     * Gets the cache of the user's provided tasks.
     * @return cached list of provided tasks
     */
    public List<Task> getUserProvidedCache() {
        return userProvidedCache;
    }

    /**
     * Gets the cache of the user's bidded tasks.
     * @return cached list of bidded tasks
     */
    public List<Task> getUserBiddedCache() {
        return userBiddedCache;
    }

    /**
     * Gets the session NotificationCollector.
     * @return NotificationCollector
     */
    public NotificationCollector getNotificationCollector() {
        return notificationCollector;
    }

    /**
     * Updates the last-known state for the given task. This essentially
     * acknowledges all current notifications about the task, and causes
     * new notifications to be generated based off of this state. This
     * should also be called on a newly-created task to allow notifications
     * to be generated for it.
     *
     * @param task task to update
     *
     * @see Notifier
     */
    public void setTaskKnown(Task task) {
        // TODO don't unnecessarily set on both notifiers
        this.providedTaskNotifier.updateLastKnown(task);
        this.requestedTaskNotifier.updateLastKnown(task);
    }

    public void initializeKnownState() {
        for (Task t: userProvidedCache) { providedTaskNotifier.updateLastKnown(t); }
        for (Task t: userBiddedCache) { providedTaskNotifier.updateLastKnown(t); }
        for (Task t: userRequestedCache) { requestedTaskNotifier.updateLastKnown(t); }
    }
}
