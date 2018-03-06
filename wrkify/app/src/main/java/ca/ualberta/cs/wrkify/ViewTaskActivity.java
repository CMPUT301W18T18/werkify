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


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ViewTaskActivity displays an expanded view of a Task.
 * It contains a view of the title, the provider, the task
 * description, and provides a bottom sheet displaying the
 * task status and allowing interaction.
 */
public class ViewTaskActivity extends AppCompatActivity {

    public static String EXTRA_TARGET_USER = "ca.ualberta.cs.wrkify.EXTRA_TARGET_USER";

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

        Task task = (Task) intent.getSerializableExtra(EXTRA_TARGET_USER);

        // Set the task title
        TextView titleView = findViewById(R.id.taskViewTitle);
        titleView.setText(task.getTitle());

        // Set the task user view
        UserView userView = findViewById(R.id.taskViewUser);
        userView.setUserName(task.getProvider().getUsername());

        // Set the task description
        TextView descriptionView = findViewById(R.id.taskViewDescription);
        descriptionView.setText(task.getDescription());

        // Initialize the bottom sheet
        TaskStatus status = task.getStatus();

        // (Get a bottom sheet fragment)
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ViewTaskBottomSheetFragment fragment = new ViewTaskOpenBottomSheetFragment();

        fragmentTransaction.add(R.id.taskViewBottomSheetFrame, fragment);

        fragmentTransaction.commit();
    }
}
