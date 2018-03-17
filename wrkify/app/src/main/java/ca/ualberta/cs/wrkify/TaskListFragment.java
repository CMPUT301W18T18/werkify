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

    private List<Task> tasks;

    /**
     * Requisite null constructor
     */
    public TaskListFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("-->", "created a TaskListFragment");
        TextView view = new TextView(getContext());
        view.setText("TEST");
        return view;
    }
}
