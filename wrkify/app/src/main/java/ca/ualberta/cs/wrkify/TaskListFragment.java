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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that displays a list of tasks.
 * This is used as the pages of a TaskListFragmentPagerAdapter,
 * but can probably be used in other contexts as well.
 * Receives an ArrayList of Tasks as ARGUMENT_TASK_LIST,
 * and displays those tasks.
 *
 * TODO adapter is currently not implemented.
 */
public class TaskListFragment extends Fragment {
    public static final String ARGUMENT_TASK_LIST = "ca.ualberta.cs.wrkify.ARGUMENT_TASK_LIST";
    
    /**
     * Creates a TaskListFragment for a list of tasks. This is a simplifying
     * factory wrapper around TaskListFragment.
     * @param tasks list of tasks
     * @return TaskListFragment to display the list of tasks
     */
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
        Log.i("-->", "created view");
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        if (tasks.size() == 0) {
            view.findViewById(R.id.taskListView).setVisibility(View.GONE);
            view.findViewById(R.id.taskListEmptyMessage).setVisibility(View.VISIBLE);
        }

        final ViewGroup notificationContainer = view.findViewById(R.id.taskListNotificationContainer);
        final RecyclerView recyclerView = view.findViewById(R.id.taskListView);

        // test
        NotificationView notificationView = new NotificationView(getContext());
        notificationView.setNotification(new NotificationInfo("Pre", "Target", "Post"));
        ((ViewGroup) view.findViewById(R.id.taskListNotificationTarget)).addView(notificationView);

        // Task list dodges notifications
        notificationContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.setPadding(0, bottom - top + 16, 0, 0);
            }
        });

        // Hide notifications when scrolling down
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                notificationContainer.setVisibility(dy <= 0? View.VISIBLE : View.GONE);
            }
        });

        TaskListAdapter<Task> taskListAdapter = new TaskListAdapter<Task>(getContext(), tasks);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.i("-->", "nv height is " + notificationView.getHeight());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getView() != null) {
            this.getView().findViewById(R.id.taskListView).setVisibility(tasks.size() == 0? View.GONE : View.VISIBLE);
            this.getView().findViewById(R.id.taskListEmptyMessage).setVisibility(tasks.size() == 0? View.VISIBLE : View.GONE);
        }
    }
}
