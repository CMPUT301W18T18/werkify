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
import android.content.Context;
import android.content.Intent;

/**
 * Entity representing an abstract notification.
 * A notification has three text fields: a target, and text to display
 * before and after the target. A NotificationInfo object can be
 * rendered in multiple ways; most likely in a NotificationView, but
 * it could also be converted to a system notification or any other
 * kind of visual form.
 *
 * @see NotificationView
 */
public class NotificationInfo {
    private String preText;
    private String targetText;
    private String postText;
    private Boolean isImportant;

    private String targetId;
    private NotificationAction action;

    private OnNotificationAcknowledgedListener listener;

    /**
     * create the notification info.
     * @param preText first line of notification
     * @param targetText second line of notification.
     * @param postText third line of notification.
     */
    public NotificationInfo(String preText, String targetText, String postText) {
        this.preText = preText;
        this.targetText = targetText;
        this.postText = postText;
        this.isImportant = false;
    }

    /**
     * get the first line of the notification
     * @return the line text
     */
    public String getPreText() {
        return preText;
    }

    /**
     * get the second line of the notification
     * @return the line text
     */
    public String getTargetText() {
        return targetText;
    }

    /**
     * get the third line of the notification
     * @return the line text
     */
    public String getPostText() {
        return postText;
    }

    /**
     * check if the notification is flagged as
     * important.
     * @return true if important, false unimportant
     */
    public Boolean isImportant() {
        return isImportant;
    }

    /**
     * change the importance of the notification
     * @param isImportant true if important, false unimportant
     */
    public void setIsImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }

    /**
     * set what the target RemoteObject is and the action to
     * take on selection
     * @param targetId the id of the target
     * @param action the action when pressed
     */
    public void setViewTarget(String targetId, NotificationAction action) {
        this.targetId = targetId;
        this.action = action;
    }

    /**
     * get the id of the notification target.
     * @return the id.
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * get the action of the notification
     * @return the action
     */
    public NotificationAction getNotificationAction() {
        return action;
    }

    /**
     * run the NotificationAction of the Notification
     * @param context the activity context
     * @return the boolean result of the action
     */
    public boolean launchNotificationAction(Context context) {
        if (getNotificationAction() == null) { return true; }
        return getNotificationAction().launch(context, this);
    }

    /**
     * set the listener, which will run when the notification
     * is acknowledged.
     * @param listener the listener
     */
    public void setOnNotificationAcknowledgedListener(OnNotificationAcknowledgedListener listener) {
        this.listener = listener;
    }

    /**
     * runs the Acknowledgement listener of the notification
     */
    public void acknowledge() {
        if (this.listener != null) {
            listener.onNotificationAcknowledged(this);
        }
    }

    /**
     * the interface for notification acknowledgement.
     */
    public interface OnNotificationAcknowledgedListener {
        /**
         * run on notification acknowledge
         * @param notification the notification that have been acknowledged
         */
        void onNotificationAcknowledged(NotificationInfo notification);
    }
}
