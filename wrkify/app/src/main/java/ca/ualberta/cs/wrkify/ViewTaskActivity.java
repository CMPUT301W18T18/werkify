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
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * ViewTaskActivity displays an expanded view of a Task.
 * It contains a view of the title, the provider, the task
 * description, and provides a bottom sheet displaying the
 * task status and allowing interaction.
 */
public class ViewTaskActivity extends Activity {
    public static String EXTRA_TARGET_TASK = "ca.ualberta.cs.wrkify.EXTRA_TARGET_TASK";
    public static String EXTRA_SESSION_USER = "ca.ualberta.cs.wrkify.EXTRA_SESSION_USER";

    /**
     * Create the ViewTaskActivity.
     * Populates the layout with details from the intended
     * Task and creates the appropriate bottom sheet.
     * @param savedInstanceState passed to superclass constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);


        Intent intent = this.getIntent();

        Task task = (Task) intent.getSerializableExtra(EXTRA_TARGET_TASK);
        User sessionUser = (User) intent.getSerializableExtra(EXTRA_SESSION_USER);

        // Set the task title
        TextView titleView = findViewById(R.id.taskViewTitle);
        titleView.setText(task.getTitle());

        // Set the task user view
        UserView userView = findViewById(R.id.taskViewUser);
        userView.setUserName(task.getRequester().getUsername());

        // Set the task description
        TextView descriptionView = findViewById(R.id.taskViewDescription);
        descriptionView.setText(task.getDescription());

        // Create the bottom sheet
        TaskStatus status = task.getStatus();

        ViewTaskBottomSheet bottomSheet = null;
        if (status == TaskStatus.BIDDED) {
            bottomSheet = new ViewTaskBiddedBottomSheet(this);
            bottomSheet.setDetailString(String.format(Locale.US,
                    "Current bid is $%2f (%d bids so far)",
                    task.getLowestBid().getValue(),
                    task.getBidList().size()));
        } else if (status == TaskStatus.REQUESTED) {
            bottomSheet = new ViewTaskOpenBottomSheet(this);
            bottomSheet.setDetailString("No bids yet");
        } else if (status == TaskStatus.ASSIGNED) {
            bottomSheet = new ViewTaskAssignedBottomSheet(this);
        } else if (status == TaskStatus.DONE) {
            bottomSheet = new ViewTaskDoneBottomSheet(this);
        } else {
            throw new InvalidParameterException();
        }

        // Add the bottom sheet
        CoordinatorLayout layout = findViewById(R.id.taskView);
        layout.addView(bottomSheet);

        // Set up the app bar
        setTitle("Task");
    }
}
