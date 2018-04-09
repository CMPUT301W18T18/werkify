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
import android.content.Intent;

import java.io.IOException;

/**
 * ViewTaskNotificationAction is NotificationAction that launches
 * the associated task.
 */
public class ViewTaskNotificationAction implements NotificationAction {
    private RemoteReference<Task> reference;

    /**
     * creates a ViewTaskNotificationAction froma taskId
     * @param taskId the id of the task
     */
    public ViewTaskNotificationAction(String taskId) {
        this.reference = new RemoteReference<>(taskId);
    }

    /**
     * launches the task of the notification
     * @param context the android context
     * @param notification the notification that has been presses
     * @return
     */
    @Override
    public boolean launch(Context context, NotificationInfo notification) {
        Intent intent = new Intent(context, ViewTaskActivity.class);

        try {
            intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK,
                    reference.getRemote(WrkifyClient.getInstance(), Task.class));
            context.startActivity(intent);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
