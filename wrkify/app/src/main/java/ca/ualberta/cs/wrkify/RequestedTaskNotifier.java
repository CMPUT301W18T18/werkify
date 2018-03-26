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

import java.util.Locale;

/**
 * Generates notifications for tasks you have requested.
 */
public class RequestedTaskNotifier extends Notifier<Task> {
    private static final String NI_NEW_BIDS_TOP_F = "%d new bids on your post";

    @Nullable
    @Override
    protected NotificationInfo compare(@NonNull Task known, @Nullable Task current) {
        String knownTitle = known.getTitle();

        if (current == null) { return null; }

        TaskStatus currentStatus = current.getStatus();
        TaskStatus knownStatus = known.getStatus();

        if (currentStatus == TaskStatus.BIDDED && (knownStatus == TaskStatus.BIDDED || knownStatus == TaskStatus.REQUESTED)) {
            int newBids = known.getBidList().size() - current.getBidList().size();
            return new NotificationInfo(String.format(Locale.US, NI_NEW_BIDS_TOP_F, newBids), knownTitle, "");
        }

        return null;
    }
}
