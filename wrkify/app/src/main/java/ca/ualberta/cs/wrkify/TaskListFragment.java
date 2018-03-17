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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TaskListFragment extends Fragment {
    public static final String ARGUMENT_TASK_LIST = "ca.ualberta.cs.wrkify.ARGUMENT_TASK_LIST";

    public static TaskListFragment makeTaskList(ArrayList<Task> tasks) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGUMENT_TASK_LIST, tasks);
        fragment.setArguments(arguments);
        return fragment;
    }

    private ArrayList<Task> tasks;

    /**
     * Requisite null constructor
     */
    public TaskListFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tasks = (ArrayList<Task>) this.getArguments().getSerializable(ARGUMENT_TASK_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        if (tasks.size() == 0) {
            view.findViewById(R.id.taskListView).setVisibility(View.GONE);
            view.findViewById(R.id.taskListEmptyMessage).setVisibility(View.VISIBLE);
        }
        else {
            RecyclerView recyclerView = view.findViewById(R.id.taskListView);
            recyclerView.setAdapter(null); // TODO adapter doesn't exist yet
        }

        return view;
    }
}
