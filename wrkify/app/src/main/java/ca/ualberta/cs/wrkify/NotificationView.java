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
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class NotificationView extends ConstraintLayout {
    private TextView upperField;
    private TextView middleField;
    private TextView lowerField;

    public NotificationView(Context context) {
        super(context);
        createViews();
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews();
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews();
    }

    public void setNotification(NotificationInfo notification) {
        this.upperField.setText(notification.getPreText());
        this.middleField.setText(notification.getTargetText());
        this.lowerField.setText(notification.getPostText());
    }

    private void createViews() {
        View view = inflate(getContext(), R.layout.view_notification, this);

        this.upperField = findViewById(R.id.notificationUpper);
        this.middleField = findViewById(R.id.notificationMiddle);
        this.lowerField = findViewById(R.id.notificationLower);
    }
}
