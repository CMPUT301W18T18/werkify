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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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

    private static String FRAGMENT_BOTTOM_SHEET = "ca.ualberta.cs.wrkify.FRAGMENT_BOTTOM_SHEET";

    /**
     * Create the ViewTaskActivity.
     * Populates the layout with details from the intended
     * Task and creates the appropriate bottom sheet.
     *
     * @param savedInstanceState passed to superclass constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);


        Log.i("-->", "onCreate called");

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

        // Add the bottom sheet if it doesn't exist already from a previous initialization
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.findFragmentByTag(FRAGMENT_BOTTOM_SHEET) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ViewTaskBottomSheetFragment bottomSheet = generateBottomSheetFor(task, sessionUserIsRequester);
            Bundle arguments = new Bundle();
            arguments.putSerializable(ViewTaskBottomSheetFragment.ARGUMENT_TARGET_TASK, task);
            bottomSheet.setArguments(arguments);

            transaction.add(R.id.taskView, bottomSheet, FRAGMENT_BOTTOM_SHEET);
            transaction.commit();
        }

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
     * Collapse the bottom sheet on up press if it is open, instead of
     * leaving the activity altogether. (This is the intended way to
     * 'cancel' the bottom sheet.)
     */
    @Override
    public boolean onNavigateUp() {
        ViewTaskBottomSheetFragment bottomSheet = (ViewTaskBottomSheetFragment)
                getFragmentManager().findFragmentByTag(FRAGMENT_BOTTOM_SHEET);
        if (bottomSheet.isExpanded()) {
            bottomSheet.collapse();
            return false;
        }
        else {
            return super.onNavigateUp();
        }
    }
}
