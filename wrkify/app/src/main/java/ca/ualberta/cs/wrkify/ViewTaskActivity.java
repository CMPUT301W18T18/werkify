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
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.InvalidParameterException;

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
        // TODO? this comparison seems like it should be encapsulable as User.equals
        Boolean sessionUserIsRequester = (task.getRequester().getUsername().equals(sessionUser.getUsername()));

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
        this.bottomSheet = generateBottomSheetFor(task, sessionUserIsRequester);

        // Add the bottom sheet
        ViewGroup layout = findViewById(R.id.taskView);
        layout.addView(bottomSheet);

        // Set up the app bar
        setTitle("Task");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Determines and returns the appropriate bottom sheet for the task.
     * @param task task being viewed
     * @param sessionUserIsRequester whether the session user is the task requester
     * @return bottom sheet view selected by the parameters
     */
    private ViewTaskBottomSheet generateBottomSheetFor(Task task, Boolean sessionUserIsRequester) {
        if (sessionUserIsRequester) {
            switch(task.getStatus()) {
                case REQUESTED: return new ViewTaskRequestedBottomSheet(this).initializeWithTask(task);
                case BIDDED: return new ViewTaskBiddedBottomSheet(this).initializeWithTask(task);
                case ASSIGNED: return new ViewTaskRequesterAssignedBottomSheet(this).initializeWithTask(task);
                case DONE: return new ViewTaskDoneBottomSheet(this).initializeWithTask(task);
            }
        }
        switch(task.getStatus()) {
            case REQUESTED:
            case BIDDED: return new ViewTaskOpenBottomSheet(this).initializeWithTask(task);
            case ASSIGNED: return new ViewTaskAssignedBottomSheet(this).initializeWithTask(task);
            case DONE: return new ViewTaskDoneBottomSheet(this).initializeWithTask(task);
        }
        throw new InvalidParameterException();
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
