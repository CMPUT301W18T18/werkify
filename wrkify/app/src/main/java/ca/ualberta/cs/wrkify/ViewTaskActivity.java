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
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

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

    private ViewTaskBottomSheet bottomSheet;

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

        // Determine if the session user owns this task
        Boolean sessionUserIsRequester = (task.getRequester().equals(sessionUser));

        // Set the task title
        TextView titleView = findViewById(R.id.taskViewTitle);
        titleView.setText(task.getTitle());

        // Set the task user view
        UserView userView = findViewById(R.id.taskViewUser);
        userView.setUserName(task.getRequester().getUsername());

        // Set the task description
        TextView descriptionView = findViewById(R.id.taskViewDescription);
        descriptionView.setText(task.getDescription());

        // Generate bottom sheet
        TaskStatus status = task.getStatus();
        if (status == TaskStatus.BIDDED) {
            // Bidded task bottom sheet
            // Provider still sees "open" status,
            // since the presence of other bids doesn't change anything for them.
            // Requester sees "bidded" status as expected.
            if (sessionUserIsRequester) {
                this.bottomSheet = new ViewTaskBiddedBottomSheet(this);
            }
            else {
                this.bottomSheet = new ViewTaskOpenBottomSheet(this);
            }

            Double lowestBidValue = task.getLowestBid().getValue();
            Integer bidCount = task.getBidList().size();

            // Show number of bids so far
            this.bottomSheet.setDetailString(
                    String.format(Locale.US, "%d bids so far", bidCount));

            // Show current bid
            this.bottomSheet.setRightStatusString(
                    String.format(Locale.US, "$%.2f", lowestBidValue));
        } else if (status == TaskStatus.REQUESTED) {
            // Requested (not yet bidded) task bottom sheet
            this.bottomSheet = new ViewTaskOpenBottomSheet(this);

            // Just show a status message
            this.bottomSheet.setDetailString("No bids yet");
        } else if (status == TaskStatus.ASSIGNED) {
            // Assigned bottom sheet
            bottomSheet = new ViewTaskAssignedBottomSheet(this);

            User assignee = task.getProvider();
            Double bidValue = task.getPrice();

            // Show assignee
            bottomSheet.setDetailString(
                String.format(Locale.US, "Assigned to %s", assignee.getUsername()));

            // Show their accepted bid price
            bottomSheet.setRightStatusString(
                String.format(Locale.US, "$%.2f", bidValue));
        } else if (status == TaskStatus.DONE) {
            // Completed bottom sheet
            bottomSheet = new ViewTaskDoneBottomSheet(this);

            User assignee = task.getProvider();

            // Show a status message with the assignee name
            bottomSheet.setDetailString(
                    String.format(Locale.US, "Completed by %s", assignee.getUsername()));
        } else {
            throw new InvalidParameterException();
        }

        // Add the bottom sheet
        ViewGroup layout = findViewById(R.id.taskView);
        layout.addView(bottomSheet);

        // Set up the app bar
        setTitle("Task");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Collapse the bottom sheet on up press if it is open, instead of
     * leaving the activity altogether. (This is the intended way to
     * 'cancel' the bottom sheet.)
     */
    @Override
    public boolean onNavigateUp() {
        if (this.bottomSheet.isExpanded()) {
            bottomSheet.collapse();
            return false;
        }
        else {
            return super.onNavigateUp();
        }
    }
}
