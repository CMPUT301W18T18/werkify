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


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Notifier analyzes the changes that have occurred in some domain of RemoteObjects
 * since a known state. It can generate notifications for any significant changes.
 * @param <T> Type of RemoteObject this Notifier can analyze
 */
public abstract class Notifier<T extends RemoteObject> implements Serializable {
    private Map<String, T> lastKnown;

    /**
     * Creates a Notifier with empty saved domain state.
     */
    public Notifier() {
        this.lastKnown = new HashMap<>();
    }

    /**
     * Generates notifications for the difference between the Notifier's saved domain state
     * and the given domain state. The domain state is the set of all objects that this Notifier
     * is observing for changes. Every object in the Notifier's saved domain state will be
     * compared to its self in the given domain state. If the Notifier decides that the difference
     * warrants a notification, one will be added to the returned set. Objects in the given
     * domain state that are not in the saved domain state will never generate notifications.
     * @param currentState Current domain state
     * @return Set of generated notifications.
     */
    public Set<NotificationInfo> generateNotifications(Map<String, T> currentState) {
        Set<NotificationInfo> notifications = new HashSet<>();
        for (final T item: lastKnown.values()) {
            final T currentItem = currentState.get(item.getId());
            NotificationInfo notification = this.compare(item, currentItem);

            if (notification != null) {
                notification.setOnNotificationDestroyedListener(new NotificationInfo.OnNotificationDestroyedListener() {
                    @Override
                    public void onNotificationDestroyed() {
                        if (currentItem == null) { clearLastKnown(item.getId()); }
                        else { updateLastKnown(currentItem); }
                    }
                });
                notifications.add(notification);
            }
        }

        return notifications;
    }

    /**
     * Adapter to call generateNotifications from a list rather than a map.
     * Generates the map by calling getId() on each element of the list.
     * @param currentState List of RemoteObjects representing the current state of the notification domain.
     * @return Set of generated notifications.
     */
    public Set<NotificationInfo> generateNotifications(List<T> currentState) {
        Map<String, T> map = new HashMap<>();
        for (T elem: currentState) { map.put(elem.getId(), elem); }
        return generateNotifications(map);
    }

    /**
     * Updates or adds an object in the Notifier's saved domain state.
     * When notifications are generated, they will be generated relative to this state.
     * @param object object to update or add
     */
    public void updateLastKnown(@NonNull T object) {
        lastKnown.put(object.getId(), object);
    }

    /**
     * Removes an object from the Notifier's saved domain state.
     * A removed object will not generate notifications until it is re-added.
     * @param id ID of object to remove
     */
    public void clearLastKnown(String id) {
        lastKnown.remove(id);
    }

    /**
     * Implementation of notification generation.
     * Every object in the Notifier's saved domain state will be passed to this function,
     * along with its counterpart in the current domain state (which may be null if the
     * object does not exist in the current domain state). This function should return
     * a NotificationInfo if the change between the known and current object state
     * warrants a notification; it can also return null if no notification is desired.
     * @param known Object's saved state
     * @param current Object's current state (may be null if object does not currently exist)
     * @return NotificationInfo of a generated notification, or null
     */
    @Nullable
    protected abstract NotificationInfo compare(@NonNull T known, @Nullable T current);
}
