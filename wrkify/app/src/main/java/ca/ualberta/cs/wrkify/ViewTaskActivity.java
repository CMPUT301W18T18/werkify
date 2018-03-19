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


import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * ViewTaskActivity displays an expanded view of a Task.
 * It contains a view of the title, the provider, the task
 * description, and provides a bottom sheet displaying the
 * task status and allowing interaction.
 */
public class ViewTaskActivity extends AppCompatActivity {
    public static final String EXTRA_TARGET_TASK = "ca.ualberta.cs.wrkify.EXTRA_TARGET_TASK";

    private static final String FRAGMENT_BOTTOM_SHEET = "ca.ualberta.cs.wrkify.FRAGMENT_BOTTOM_SHEET";
    private static final int REQUEST_EDIT_TASK = 18;

    private Task task;
    private User sessionUser;

    /**
     * Create the ViewTaskActivity.
     * Populates the layout with details from the intended
     * Task and creates the appropriate bottom sheet.
     *
     * @param savedInstanceState passed to superclass constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);


        Log.i("-->", "onCreate called");

        Intent intent = this.getIntent();

        this.sessionUser = Session.getInstance(this).getUser();
        this.initializeFromTask((Task) intent.getSerializableExtra(EXTRA_TARGET_TASK));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // TODO sync these changes to the database
            if (resultCode == RESULT_OK) {
                // Reinitialize the view when the task is edited
                this.initializeFromTask((Task) data.getSerializableExtra(EditTaskActivity.EXTRA_RETURNED_TASK));
            } else if (resultCode == EditTaskActivity.RESULT_TASK_DELETED) {
                // Exit if the task was deleted
                finish();
            }
        }
    }

    /**
     * Sets the activity to display the given task.
     * Binds the task to the activity, populates views, and sets up UI miscellanea.
     * This also needs to be called whenever the displayed task changes, or the
     * changes will not be reflected in the UI.
     * @param task task to display
     */
    private void initializeFromTask(Task task) {
        this.task = task;

        // Determine if the session user owns this task
        // TODO? this comparison seems like it should be encapsulable as User.equals
        Boolean sessionUserIsRequester;

        try {
            User remoteRequester = task.getRemoteRequester(WrkifyClient.getInstance());
            if (remoteRequester == null) {
                sessionUserIsRequester = false;
            } else {
                sessionUserIsRequester = remoteRequester.equals(Session.getInstance(this).getUser());
            }
        } catch (IOException e) {
            // TODO handle this correctly
            return;
        }

        // Set the task title
        TextView titleView = findViewById(R.id.taskViewTitle);
        titleView.setText(task.getTitle());

        // Set the task user view
        UserView userView = findViewById(R.id.taskViewUser);
        try {
            User remoteRequester = task.getRemoteRequester(WrkifyClient.getInstance());
            if (remoteRequester != null) {
                userView.setUserName(remoteRequester.getUsername());
            }
        } catch (IOException e) {
            // TODO handle this correctly
            return;
        }

        // Set the task description
        TextView descriptionView = findViewById(R.id.taskViewDescription);
        descriptionView.setText(task.getDescription());

        // Add the bottom sheet if it doesn't exist already from a previous initialization
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(FRAGMENT_BOTTOM_SHEET) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ViewTaskBottomSheetFragment bottomSheet = generateBottomSheetFor(task, sessionUserIsRequester);
            Bundle arguments = new Bundle();
            arguments.putSerializable(ViewTaskBottomSheetFragment.ARGUMENT_TARGET_TASK, task);
            bottomSheet.setArguments(arguments);

            transaction.add(R.id.taskViewInner, bottomSheet, FRAGMENT_BOTTOM_SHEET);
            transaction.commit();
        }

        // Set up the app bar
        setTitle("Task");

        // Set up the edit button if appropriate
        if (sessionUserIsRequester && task.getStatus() == TaskStatus.REQUESTED) {
            FloatingActionButton editButton = findViewById(R.id.taskViewButtonEdit);
            editButton.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Edit the task
                    Intent editIntent = new Intent(ViewTaskActivity.this,
                            EditTaskActivity.class);
                    editIntent.putExtra(EditTaskActivity.EXTRA_EXISTING_TASK, ViewTaskActivity.this.task);                    
                    startActivityForResult(editIntent, REQUEST_EDIT_TASK);
                }
            });
        }
    }

    /**
     * Determines and returns the appropriate bottom sheet for the task.
     * @param task task being viewed
     * @param sessionUserIsRequester whether the session user is the task requester
     * @return bottom sheet view selected by the parameters
     */
    private ViewTaskBottomSheetFragment generateBottomSheetFor(Task task, Boolean sessionUserIsRequester) {
        if (sessionUserIsRequester) {
            switch(task.getStatus()) {
                case REQUESTED: return new ViewTaskRequestedBottomSheetFragment();
                case BIDDED: return new ViewTaskBiddedBottomSheetFragment();
                case ASSIGNED: return new ViewTaskRequesterAssignedBottomSheetFragment();
                case DONE: return new ViewTaskDoneBottomSheetFragment();
            }
        }
        switch(task.getStatus()) {
            case REQUESTED:
            case BIDDED: return new ViewTaskOpenBottomSheetFragment();
            case ASSIGNED: return new ViewTaskAssignedBottomSheetFragment();
            case DONE: return new ViewTaskDoneBottomSheetFragment();
        }
        throw new InvalidParameterException();
    }

    /**
     * Collapses the bottom sheet on back press if it is open, without actually
     * going back. (This effectively makes the open bottom sheet a back state.)
     * Pressing back with the bottom sheet closed behaves normally.
     */
    @Override
    public void onBackPressed() {
        ViewTaskBottomSheetFragment bottomSheet = (ViewTaskBottomSheetFragment)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_BOTTOM_SHEET);
        if (bottomSheet.isExpanded()) {
            bottomSheet.collapse();
        } else {
            super.onBackPressed();
        }
    }
}
