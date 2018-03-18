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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Created by Stefan on 2018-03-17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static int taskLayoutID = R.layout.taskcardview;
    protected List<Task> taskList;
    public boolean isRequester;
    public User sessionUser;
    public AppCompatActivity context;
    private RecyclerView recyclerView;

    public TaskListAdapter(AppCompatActivity context,List<Task> taskList,boolean isRequester){
        this.taskList = taskList;
        this.isRequester = isRequester;
//        this.sessionUser = sessionUser;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(TaskListAdapter.taskLayoutID,parent,false);
        TaskViewHolder holder = new TaskViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final Task task = taskList.get(position);

        holder.getTaskTitle().setText(task.getTitle());
        holder.getTaskDescription().setText(task.getDescription());
        if(this.isRequester) {
            if(task.getProvider()!=null) {
                holder.getTaskUser().setText(task.getProvider().getUsername());
            }
            holder.getTaskUser().setText("");
        }
        if(!this.isRequester){
            if(task.getRequester()!=null) {
                holder.getTaskUser().setText(task.getRequester().getUsername());
            }
            holder.getTaskUser().setText("");
        }
        if(task.getBidList()!=null) {
            holder.getTaskStatus().setStatus(task.getStatus(), task.getLowestBid().getValue());
        }
        holder.setTask(task);

        holder.getTaskView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTask(sessionUser, task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    public void viewTask(User sessionUser, Task task){
        Intent intent = new Intent(this.context, ViewTaskActivity.class);
        intent.putExtra(ViewTaskActivity.EXTRA_SESSION_USER, task.getProvider());
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);

        context.startActivity(intent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

}
