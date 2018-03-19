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

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * RequesterFragment displays the lists of task that a task requester needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 */
public class RequesterFragment extends TasksOverviewFragment {
    private static final int REQUEST_NEW_TASK = 13;

    @Override
    protected List<ArrayList<Task>> getTaskLists() {
        List<Task> rawTasks;
        try {
            rawTasks = Searcher.findTasksByRequester(WrkifyClient.getInstance(), Session.getInstance(getContext()).getUser());
        } catch (IOException e) {
            // TODO You are offline.
            return new ArrayList<>();
        }

        // Filter tasks into requested, assigned, completed
        ArrayList<Task> requestedTasks = new ArrayList<>();
        ArrayList<Task> assignedTasks = new ArrayList<>();
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (Task t: rawTasks) {
            switch (t.getStatus()) {
                case REQUESTED:
                case BIDDED:
                    requestedTasks.add(t);
                    break;
                case ASSIGNED:
                    assignedTasks.add(t);
                case DONE:
                    completedTasks.add(t);
            }
        }

        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(requestedTasks);
        pageTaskLists.add(assignedTasks);
        pageTaskLists.add(completedTasks);

        return pageTaskLists;
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{"Requested", "Assigned", "Closed"};
    }
    
    @Override
    protected String getAppBarTitle() {
        return "My posts";
    }

    @Override
    protected boolean isAddButtonEnabled(int index) {
        return (index == 0);
    }

    @Override
    protected void onAddButtonClick(View v) {
        Intent newTaskIntent = new Intent(getContext(), EditTaskActivity.class);
        startActivityForResult(newTaskIntent, REQUEST_NEW_TASK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_TASK && resultCode == EditTaskActivity.RESULT_TASK_CREATED) {
            // Append the new task to task list
            TaskListFragmentPagerAdapter adapter = (TaskListFragmentPagerAdapter) getPager().getAdapter();
            Task task = (Task) data.getSerializableExtra(EditTaskActivity.EXTRA_RETURNED_TASK);
            adapter.appendTaskToList(0, task);
        }
    }
}
