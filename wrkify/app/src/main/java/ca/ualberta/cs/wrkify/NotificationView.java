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
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


/**
 * View displaying a NotificationInfo object.
 * A NotificationView has an Action button and a Dismiss button.
 * The Action button fires the NotificationInfo object's Intent, and the
 * Dismiss button clears the notification. Both of these buttons can also
 * have listeners bound from outside the NotificationView.
 *
 * @see NotificationInfo
 */
public class NotificationView extends ConstraintLayout {
    private TextView upperField;
    private TextView middleField;
    private TextView lowerField;
    private Button actionButton;
    private Button dismissButton;

    private OnClickListener afterActionListener;
    private OnClickListener dismissListener;

    private NotificationInfo notification;

    /**
     * Creates a NotificationView (standard View constructor)
     */
    public NotificationView(Context context) {
        super(context);
        createViews();
    }

    /**
     * Creates a NotificationView (standard View constructor)
     */
    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews();
    }

    /**
     * Creates a NotificationView (standard View constructor)
     */
    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews();
    }

    /**
     * Sets the NotificationInfo to display in this NotificationView.
     * This fills in the text fields of the view, and attaches the NotificationInfo
     * object's Intent to the view if one exists. It will be launched on
     * clicking the Action button in the view.
     * @param notification notification to display
     */
    public void setNotification(NotificationInfo notification) {
        this.notification = notification;

        this.upperField.setText(notification.getPreText());
        this.middleField.setText(notification.getTargetText());
        this.lowerField.setText(notification.getPostText());

        if (notification.getTargetId() != null) {
            actionButton.setVisibility(VISIBLE);
        }
    }

    /**
     * Sets an OnClickListener to fire when the Dismiss button is clicked.
     * The listener will be invoked after the notification is cleared.
     * @param listener OnClickListener to fire after dismissing notification
     */
    public void setOnDismissListener(OnClickListener listener) {
        this.dismissListener = listener;
    }

    /**
     * Sets an OnClickListener to fire when the Action button is clicked.
     * The listener will be invoked after launching the notification's Intent.
     * (If the notification has no Intent, the listener will still fire.)
     * @param listener OnClickListener to fire after launching notification action
     */
    public void setAfterActionListener(OnClickListener listener) {
        this.afterActionListener = listener;

        if (listener != null) {
            actionButton.setVisibility(VISIBLE);
        }
    }

    /**
     * Populates the NotificationView.
     */
    private void createViews() {
        inflate(getContext(), R.layout.view_notification, this);

        this.upperField = findViewById(R.id.notificationUpper);
        this.middleField = findViewById(R.id.notificationMiddle);
        this.lowerField = findViewById(R.id.notificationLower);

        this.actionButton = findViewById(R.id.notificationButtonAction);
        this.dismissButton = findViewById(R.id.notificationButtonDismiss);

        // Hide the action button by default
        actionButton.setVisibility(INVISIBLE);

        // Delete notification on dismiss, and then fire attached listener
        this.dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance(getContext()).getNotificationCollector().clearNotification(notification);

                if (dismissListener != null) {
                    dismissListener.onClick(v);
                }

                destroyAssociatedSignals();
            }
        });

        // Launch notification intent on action, and then fire attached listener
        this.actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.getNotificationAction() != null) {
                    notification.launchNotificationAction(getContext());
                }

                if (afterActionListener != null) {
                    afterActionListener.onClick(v);
                }

                destroyAssociatedSignals();
            }
        });
    }

    private void destroyAssociatedSignals() {
        this.new DestroySignalTask().execute();
    }

    private class DestroySignalTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            TransactionManager transactionManager = Session.getInstance(getContext()).getTransactionManager();
            transactionManager.enqueue(new UserDeleteSignalsTransaction(
                    Session.getInstance(getContext()).getUser(), notification.getTargetId()));

            // TODO notify of offline status
            transactionManager.flush(WrkifyClient.getInstance());

            return null;
        }
    }
}
