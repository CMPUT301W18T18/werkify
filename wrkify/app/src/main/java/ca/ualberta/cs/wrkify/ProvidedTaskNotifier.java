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

/**
 * Generates notifications for tasks you had bidded on / were assigned to.
 */
public class ProvidedTaskNotifier extends Notifier<Task> {
    private static final String NI_GENERIC_TOP= "The task";
    private static final String NI_DELETED_BOTTOM = "has been deleted.";
    private static final String NI_CLOSED_BOTTOM = "has been marked complete by the requester.";
    private static final String NI_DEASSIGNED_TOP = "You've been deassigned from";

    @Nullable
    @Override
    protected NotificationInfo compare(@NonNull Task known, @Nullable Task current) {
        String knownTitle = known.getTitle();

        if (current == null) {
            // Task was deleted.
            return new NotificationInfo(NI_GENERIC_TOP, knownTitle, NI_DELETED_BOTTOM);
        }

        TaskStatus knownStatus = known.getStatus();
        TaskStatus currentStatus = current.getStatus();

        if (knownStatus == TaskStatus.ASSIGNED) {
            if (currentStatus == TaskStatus.BIDDED || currentStatus == TaskStatus.REQUESTED) {
                // You were deassigned from the task.
                // TODO this should also trigger if ASSIGNED or DONE but the assignee is not the same
                return new NotificationInfo(NI_DEASSIGNED_TOP, knownTitle, "");
            } else if (currentStatus == TaskStatus.DONE) {
                // The task was marked closed.
                return new NotificationInfo(NI_GENERIC_TOP, knownTitle, NI_CLOSED_BOTTOM);
            }
        } else if (known.getStatus() == TaskStatus.BIDDED) {
            // TODO check outbid - requires get bid for user
        }

        return null;
    }
}
