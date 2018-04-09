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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

/**
 * Activity for a task requester to edit a task that they own
 * or to create a new task. An existing task should be passed
 * in as EXTRA_EXISTING_TASK. If it is unset, a new task will
 * be created. Edited/created tasks will be returned as
 * EXTRA_RETURNED_TASK, with an appropriate result code:
 *     RESULT_TASK_CREATED for a new task
 *     RESULT_OK for an edited task
 * If the task is deleted from this activity,
 * EXTRA_RETURNED_TASK will be unset and the result will be
 * RESULT_TASK_DELETED. The parent activity must perform the
 * actual deletion of the task in this case.
 */
public class EditTaskActivity extends AppCompatActivity {
    /** Task being passed in to EditTaskActivity */
    public static final String EXTRA_EXISTING_TASK = "ca.ualberta.cs.wrkify.EXTRA_EXISTING_TASK";

    /** Task being passed back from EditTaskActivity */
    public static final String EXTRA_RETURNED_TASK = "ca.ualberta.cs.wrkify.EXTRA_RETURNED_TASK";

    /** The task being returned is a newly-created task and should be added to the appropriate context */
    public static final int RESULT_TASK_CREATED = 11;

    /** The task being edited was deleted and should be removed from its context */
    public static final int RESULT_TASK_DELETED = 12;

    /** The task was edited, but the changes were not synced successfully. */
    public static final int RESULT_UNSYNCED_CHANGES = 20;

    public static final int RESULT_LOCATION = 25;


    private Task task;
    private CheckList checkList;
    private boolean taskIsNew = false;

    private EditText titleField;
    private EditText descriptionField;
    private CheckListEditorView checkListEditorView;
    private Button checkListNewButton;
    private Button checkListAddButton;
    private TextView locationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ActionBar actionBar = getSupportActionBar();

        this.titleField = findViewById(R.id.editTaskTitleField);
        this.descriptionField = findViewById(R.id.editTaskDescriptionField);
        this.checkListEditorView = findViewById(R.id.editTaskChecklist);
        this.checkListNewButton = findViewById(R.id.editTaskButtonChecklistNew);
        this.checkListAddButton = findViewById(R.id.editTaskButtonChecklistAdd);
        this.locationField = findViewById(R.id.editTaskLocationField);

        this.task = (Task) getIntent().getSerializableExtra(EXTRA_EXISTING_TASK);

        if (this.task == null) {
            // TODO this may or may not need to be changed to allow server connectivity
            // TODO change the new user thing later

            this.taskIsNew = true;
            this.checkList = new CheckList();
            if (actionBar != null) {
                actionBar.setTitle("New task");
            }
        } else {
            this.taskIsNew = false;
            this.checkList = task.getCheckList();
            if (actionBar != null) {
                actionBar.setTitle("Editing task");
                actionBar.setSubtitle(task.getTitle());
            }

            // populate fields
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());

            if (task.getCheckList().itemCount() > 0) {
                showChecklistEditor();
            }
        }

        checkListNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList.addItem("");
                checkListEditorView.notifyDataSetChanged();
                showChecklistEditor();
            }
        });

        checkListAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList.addItem("");
                checkListEditorView.notifyDataSetChanged();
            }
        });

        checkListEditorView.setCheckList(checkList);
        checkListEditorView.setOnDataSetChangedListener(new CheckListView.OnDataSetChangedListener() {
            @Override
            public void onDataSetChanged(@Nullable CheckList data) {
                if (data == null || data.itemCount() == 0) {
                    hideChecklistEditor();
                }
            }
        });
        this.locationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskLocation();
            }
        });
    }

    public void setTaskLocation(){
//        Intent intent = new Intent(this,SetTaskLocationActivity.class);
//        startActivityForResult(intent,RESULT_LOCATION);
        Intent intent = new Intent(this,TestMapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_LOCATION){
           TaskLocation taskcoordinates = (TaskLocation) data.getSerializableExtra(SetTaskLocationActivity.LOCATION_EXTRA);
           String locationStr = Double.valueOf(taskcoordinates.getLatitude()).toString() + ", " + Double.valueOf(taskcoordinates.getLongitude()).toString();
           this.locationField.setText(locationStr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);

        MenuItem saveItem = menu.findItem(R.id.menuItemSaveTask);
        MenuItem deleteItem = menu.findItem(R.id.menuItemDeleteTask);

        if (this.taskIsNew) {
            // Set up menu for new task:

            // Save button is labelled "post"
            saveItem.setTitle("Post");

            // Delete button is not available
            deleteItem.setVisible(false);
        }
        else {
            // Set up menu for editing existing task:

            // Delete button deletes task
            deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ConfirmationDialogFragment confirmation = ConfirmationDialogFragment.makeDialog(
                            "Delete this task?",
                            "Cancel", "Delete",
                            new ConfirmationDialogFragment.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteAndFinish();
                                }
                            }
                    );
                    confirmation.show(getFragmentManager(), null);
                    return true;
                }
            });
        }

        // Clicking save always saves task
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    saveAndFinish();
                    return true;
                }
            });

        return true;
    }

    /**
     * Finishes the activity with RESULT_TASK_DELETED.
     * This should signal the parent activity to delete the task.
     */
    private void deleteAndFinish() {
        TransactionManager transactionManager = Session.getInstance(this).getTransactionManager();
        transactionManager.enqueue(new TaskDeleteTransaction(task));

        WrkifyClient.getInstance().discardCached(task.getId());

        // TODO notify of offline status
        transactionManager.flush(WrkifyClient.getInstance());

        setResult(RESULT_TASK_DELETED);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        finish();
    }

    /**
     * Writes changes to the task and finishes the activity.
     * Returns RESULT_TASK_CREATED if this is a new task, or
     * else RESULT_OK if the task exists and has been edited.
     */
    private void saveAndFinish() {
        View focus = getCurrentFocus();
        if (focus != null) { focus.clearFocus(); }

        String newTitle = titleField.getText().toString();
        String newDescription = descriptionField.getText().toString();

        int resultCode;

        if (this.taskIsNew) {
            // TODO This will probably fail if offline
            this.task = (Task) WrkifyClient.getInstance().createLocal(Task.class,
                            this.titleField.getText().toString(),
                            Session.getInstance(this).getUser(),
                            this.descriptionField.getText().toString()
                    );
            task.setCheckList(this.checkList);

            TransactionManager transactionManager = Session.getInstance(this).getTransactionManager();
            transactionManager.enqueue(new TaskCreateTransaction(task));
            WrkifyClient.getInstance().updateCached(task);

            transactionManager.flush(WrkifyClient.getInstance());

            resultCode = RESULT_TASK_CREATED;
        } else {
            TransactionManager transactionManager = Session.getInstance(this).getTransactionManager();

            transactionManager.enqueue(new TaskTitleTransaction(this.task, newTitle));
            transactionManager.enqueue(new TaskDescriptionTransaction(this.task, newDescription));
            transactionManager.enqueue(new TaskCheckListTransaction(this.task, this.checkList));

            this.task.setTitle(newTitle);
            this.task.setDescription(newDescription);
            this.task.setCheckList(this.checkList);
            WrkifyClient.getInstance().updateCached(this.task);

            if (transactionManager.flush(WrkifyClient.getInstance())) {
                resultCode = RESULT_OK;
            } else {
                resultCode = RESULT_UNSYNCED_CHANGES;
            }
        }

        if (task == null) return;

        Intent intent = getIntent();
        intent.putExtra(EXTRA_RETURNED_TASK, this.task);
        setResult(resultCode, intent);

        finish();
    }

    private void showChecklistEditor() {
        checkListEditorView.setVisibility(View.VISIBLE);
        checkListAddButton.setVisibility(View.VISIBLE);
        checkListNewButton.setVisibility(View.GONE);
    }

    private void hideChecklistEditor() {
        checkListEditorView.setVisibility(View.GONE);
        checkListAddButton.setVisibility(View.GONE);
        checkListNewButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
