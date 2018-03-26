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


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulated list of notifications.
 */
public class NotificationCollector {
    private List<NotificationInfo> notifications;

    /**
     * Creates a new NotificationCollector containing no notifications.
     */
    public NotificationCollector() {
        this.notifications = new ArrayList<>();
    }

    /**
     * Adds a notification to the NotificationCollector.
     * Important notifications will be placed at the start of the list;
     * unimportant notifications will be placed at the end.
     * @param notification NotificationInfo to add
     */
    public void putNotification(NotificationInfo notification) {
        if (notification.isImportant()) {
            this.notifications.add(0, notification);
        } else {
            this.notifications.add(notification);
        }
    }

    /**
     * Wrapper to add all the notifications in a collection to the NotificationCollector.
     * @param notifications Collection of NotificationInfo objects to add all of to the NotificationCollector
     */
    public void putNotifications(Collection<NotificationInfo> notifications) {
        for (NotificationInfo notification: notifications) {
            this.putNotification(notification);
        }
    }

    /**
     * Removes a notification from the NotificationCollector.
     * @param notification NotificationInfo to remove
     */
    public void clearNotification(NotificationInfo notification) {
        notification.destroy();
        this.notifications.remove(notification);
    }

    /**
     * Gets all notifications that have been pushed to the NotificationCollector.
     * This list should always contain important notifications (if any) first,
     * followed by unimportant notifications (if any). List may be empty.
     * @return list of NotificationInfo
     */
    public List<NotificationInfo> getNotifications() {
        return this.notifications;
    }

    /**
     * Gets the first notification in the list. If there are any important
     * notifications, this should always be an important notification.
     * @return first NotificationInfo. Null if list is empty.
     */
    @Nullable
    public NotificationInfo getFirstNotification() {
        if (this.notifications.size() == 0) {
            return null;
        } else {
            return this.notifications.get(0);
        }
    }

    /**
     * Gets the number of notifications stored in the collector.
     * @return number of notifications
     */
    public int getNotificationCount() {
        return this.notifications.size();
    }
}
