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


import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Locale;

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

    protected static final int REQUEST_VIEW_BIDS = 19;

    private Task task;
    private User sessionUser;

    /**
     * Replaces the Task that the activity is displaying.
     * @param task task to display
     */
    public void replaceTask(Task task) {
        this.initializeFromTask(task);
    }

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

        this.sessionUser = Session.getInstance(this).getUser();
        this.initializeFromTask((Task) intent.getSerializableExtra(EXTRA_TARGET_TASK));
    }

    /**
     * update the views when EditTaskActivity finishes
     * and finishes if the task was deleted
     * @param requestCode the requestcode
     * @param resultCode the requestCode
     * @param data the intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    protected void initializeFromTask(Task task) {
        new InitializeTaskTask().execute(task);
    }

    private class InitializeTaskTask extends AsyncTask<Task, Void, Void> {

        private Boolean sessionUserIsRequester;
        private Boolean sessionUserIsProvider;
        private User remoteRequester;

        /**
         * fetch the new Task info from the client
         * @param tasks the task to update
         * @return unused
         */
        @Override
        protected Void doInBackground(Task... tasks) {
            task = tasks[0];

            try {
                User remoteRequester = task.getRemoteRequester(WrkifyClient.getInstance());
                if (remoteRequester == null) {
                    sessionUserIsRequester = false;
                } else {
                    sessionUserIsRequester = remoteRequester.equals(Session.getInstance(ViewTaskActivity.this).getUser());
                }
            } catch (IOException e) {
                // TODO handle this correctly
                return null;
            }

            try {
                User remoteProvider = task.getRemoteProvider(WrkifyClient.getInstance());
                if (remoteProvider == null) {
                    sessionUserIsProvider = false;
                } else {
                    sessionUserIsProvider = remoteProvider.equals(Session.getInstance(ViewTaskActivity.this).getUser());
                }
            } catch (IOException e) {
                // TODO handle this correctly
                return null;
            }

            try {
                remoteRequester = task.getRemoteRequester(WrkifyClient.getInstance());
            } catch (IOException e) {

            }

            return null;
        }

        /**
         * udpate the views after we fetch the new task info.
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {

            // Set the task title
            TextView titleView = findViewById(R.id.taskViewTitle);
            titleView.setText(task.getTitle());

            // Set the task user view
            UserView userView = findViewById(R.id.taskViewUser);
            if (remoteRequester != null) {
                userView.setUserName(remoteRequester.getUsername());
                userView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // View user profile from the user view
                        Intent viewUserIntent = new Intent(ViewTaskActivity.this, ViewProfileActivity.class);
                        viewUserIntent.putExtra(ViewProfileActivity.USER_EXTRA, remoteRequester);
                        startActivity(viewUserIntent);
                    }
                });
            }

            // Set the task description
            TextView descriptionView = findViewById(R.id.taskViewDescription);
            descriptionView.setText(task.getDescription());

            // Initialize the checklist view
            final CheckListProviderView checkListProviderView = findViewById(R.id.taskViewChecklist);
            checkListProviderView.setEditingEnabled(sessionUserIsProvider && task.getStatus() == TaskStatus.ASSIGNED);
            checkListProviderView.setCheckList(task.getCheckList());
            checkListProviderView.setVisibility(task.getCheckList().itemCount() == 0? View.GONE : View.VISIBLE);

            checkListProviderView.setOnItemToggledListener(new CheckListProviderView.OnItemToggledListener() {
                @Override
                public void onItemToggled(final @NonNull CheckList.CheckListItem item) {
                    String confirmationActionString = item.getStatus()? "not completed": "completed";
                    ConfirmationDialogFragment dialog = ConfirmationDialogFragment.makeDialog(
                            String.format(Locale.US, "Mark \"%s\" as %s?", item.getDescription(), confirmationActionString),
                            "Cancel",
                            String.format(Locale.US, "Mark %s", confirmationActionString),
                            new ConfirmationDialogFragment.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    item.setStatus(!item.getStatus());

                                    new TransactionAsyncTask().execute(
                                            ViewTaskActivity.this.task,
                                            new TaskCheckListTransaction(
                                                    ViewTaskActivity.this.task,
                                                    ViewTaskActivity.this.task.getCheckList()
                                            ),
                                            ViewTaskActivity.this
                                    );
                                    checkListProviderView.notifyDataSetChanged();
                                }
                            }
                    );
                    dialog.show(getFragmentManager(), null);
                }
            });

            // Add the bottom sheet if it doesn't exist already from a previous initialization
            FragmentManager fragmentManager = getSupportFragmentManager();


            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ViewTaskBottomSheetFragment bottomSheet = generateBottomSheetFor(task, sessionUserIsRequester);
            Bundle arguments = new Bundle();
            arguments.putSerializable(ViewTaskBottomSheetFragment.ARGUMENT_TARGET_TASK, task);
            bottomSheet.setArguments(arguments);

            transaction.replace(R.id.taskViewInner, bottomSheet, FRAGMENT_BOTTOM_SHEET);
            transaction.commit();

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
            case BIDDED: if (task.getBidForUser(Session.getInstance(this).getUser()) != null) {
                    return new ViewTaskProviderBiddedBottomSheetFragment();
                } else {
                    return new ViewTaskOpenBottomSheetFragment();
                }
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
