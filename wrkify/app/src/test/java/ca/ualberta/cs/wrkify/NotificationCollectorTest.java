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


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.*;

public class NotificationCollectorTest {
    @Test
    public void testNotificationCollector() {
        NotificationCollector collector = new NotificationCollector();

        // The first notification of an empty collector is null
        assertNull(collector.getFirstNotification());
        assertEquals(0, collector.getNotificationCount());

        // Adding a notification makes it the first notification
        NotificationInfo notification1 = new NotificationInfo("1", "1", "1");
        collector.putNotification(notification1);

        assertEquals(notification1, collector.getFirstNotification());
        assertEquals(1, collector.getNotificationCount());
        assertEquals(Arrays.asList(notification1), collector.getNotifications());

        // Adding a second normal notification places it after the first (oldest first)
        NotificationInfo notification2 = new NotificationInfo("2", "2", "2");
        collector.putNotification(notification2);

        assertEquals(notification1, collector.getFirstNotification());
        assertEquals(2, collector.getNotificationCount());
        assertEquals(Arrays.asList(notification1, notification2), collector.getNotifications());

        // An important notification always goes to the front
        NotificationInfo importantNotification1 = new NotificationInfo("3", "3", "3");
        importantNotification1.setIsImportant(true);
        collector.putNotification(importantNotification1);

        assertEquals(importantNotification1, collector.getFirstNotification());
        assertEquals(3, collector.getNotificationCount());
        assertEquals(Arrays.asList(importantNotification1, notification1, notification2), collector.getNotifications());

        // Important notifications are listed newest first
        NotificationInfo importantNotification2 = new NotificationInfo("4", "4", "4");
        importantNotification2.setIsImportant(true);
        collector.putNotification(importantNotification2);

        assertEquals(importantNotification2, collector.getFirstNotification());
        assertEquals(4, collector.getNotificationCount());
        assertEquals(Arrays.asList(importantNotification2, importantNotification1, notification1, notification2),
                collector.getNotifications());

        // Notifications can be deleted by reference
        collector.clearNotification(importantNotification2);

        assertEquals(importantNotification1, collector.getFirstNotification());
        assertEquals(3, collector.getNotificationCount());
        assertEquals(Arrays.asList(importantNotification1, notification1, notification2), collector.getNotifications());

        collector.clearNotification(notification1);

        assertEquals(importantNotification1, collector.getFirstNotification());
        assertEquals(2, collector.getNotificationCount());
        assertEquals(Arrays.asList(importantNotification1, notification2), collector.getNotifications());
    }
}
