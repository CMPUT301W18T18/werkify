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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for lists of Tasks or objects in that family.
 * To be used as an adapter for RecyclerView whenever
 * a potentially large list of Tasks is to be displayed
 * Tasks are displayed as CardViews but can
 * be easily changed to something else in TaskViewHolder.
 *
 * @see TaskViewHolder
 * @see SearchFragment
 */

public class TaskListAdapter<T extends Task> extends RecyclerView.Adapter<TaskViewHolder> {
    private static int taskLayoutID = R.layout.taskcardview;
    protected List<T> taskList;
    public AppCompatActivity context;
    private RecyclerView recyclerView;

    /*
    *Sets the task list to be used for a RecyclerView, sets sessionUser
    *
    * @param context AppCompatActivity of calling Activity
    * @param List<T> where T is anything that extends Task
    * @param isRequester, boolean indicating calling perspective (Requester/Provider)
     */
    public TaskListAdapter(AppCompatActivity context,List<T> taskList){
        this.taskList = taskList;
        this.context = context;
    }


    /*
    *Creates TaskViewHolder for caching Task CardViews
    *
    * @param parent ViewGroup of parent activity
    * @param viewType
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(TaskListAdapter.taskLayoutID,parent,false);
        TaskViewHolder holder = new TaskViewHolder(v);
        return holder;
    }


    /*
    *Binds a holder to a the recyvler view
    *
    * @param holder TaskViewHolder containing a Task Card View
    * @param position Index in the recycler list
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final T task = taskList.get(position);

        holder.getTaskTitle().setText(task.getTitle());
        holder.getTaskDescription().setText(task.getDescription());

        User requester;
        try {
            requester = task.getRemoteRequester(WrkifyClient.getInstance());
        } catch (IOException e) {
            return;
        }

        final User sessionUser = Session.getInstance(context).getUser();

        if(sessionUser.equals(requester)) {
            try {
                User provider = task.getRemoteProvider(WrkifyClient.getInstance());
                if(provider!=null) {
                    holder.getTaskUser().setText(provider.getUsername());
                }
            }
            catch (IOException e) {
                holder.getTaskUser().setText("");
            }
        } else {
            if (requester != null) {
                holder.getTaskUser().setText(requester.getUsername());
            }
        }

        if(task.getBidList()!=null) {
            if(task.getStatus()!=null) {
                holder.getTaskStatus().setStatus(task.getStatus(), task.getBidList().get(0).getValue());
            }
        }

        holder.setTask(task);

        holder.getTaskView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTask(sessionUser, task);
            }
        });
    }

    /*
    *Returns the size of recycler list.
     */
    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    /*
    *Runs the ViewTaskActivity when a task is selected
    * from the recycler view list
    *
    * @param sessionUser app User
    * @param task The task that was clicked
    */
    public void viewTask(User sessionUser, T task){
        Intent intent = new Intent(this.context, ViewTaskActivity.class);
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);

        context.startActivity(intent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    /*
    *Sets the list to be adapted for a recycler view
    *
    * @param taskList List of objects in Task family
    */
    public void setTaskList(List<T> taskList){
        this.taskList = taskList;
    }

    /*
    *Returns the List of tasks that are being adapted
     */
    public List<T> getTaskList(){
        return this.taskList;
    }

}
