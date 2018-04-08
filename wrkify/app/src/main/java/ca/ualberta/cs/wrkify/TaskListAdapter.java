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
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
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
    private RecyclerView recyclerView;

    /*
    *Sets the task list to be used for a RecyclerView, sets sessionUser
    *
    * @param context AppCompatActivity of calling Activity
    * @param List<T> where T is anything that extends Task
    * @param isRequester, boolean indicating calling perspective (Requester/Provider)
     */
    public TaskListAdapter(Context context, List<T> taskList){
        this.taskList = taskList;
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
        this.new TaskDisplayTask().execute(position, holder);
    }

    private class TaskDisplayTask extends AsyncTask<Object, Void, Void> {
        private TaskViewHolder holder;

        private String title;
        private String description;
        private String username;
        private T task;
        private TaskStatus status;
        private Price price;
        private int color;

        private User sessionUser;

        @Override
        protected Void doInBackground(Object... posholder) {
            int position = (Integer) posholder[0];
            this.holder = (TaskViewHolder) posholder[1];

            task = taskList.get(position);

            this.title = task.getTitle();
            this.description = task.getDescription();

            User requester;
            try {
                requester = task.getRemoteRequester(WrkifyClient.getInstance());
            } catch (IOException e) {
                return null;
            }

            sessionUser = Session.getInstance(recyclerView.getContext()).getUser();

            if(sessionUser.equals(requester)) {
                try {
                    User provider = task.getRemoteProvider(WrkifyClient.getInstance());
                    if(provider!=null) {
                        this.username = provider.getUsername();
                    }
                }
                catch (IOException e) {
                    this.username = "";
                }
            } else {
                if (requester != null) {
                    this.username = requester.getUsername();
                }
            }

            if(task.getBidList()!=null) {
                if(task.getStatus()!=null) {
                    if(task.getBidList().size()==0){
                        this.status = TaskStatus.REQUESTED;
                        this.price = new Price(0.0);

                    }
                    else {
                        this.status = task.getStatus();
                        this.price = task.getBidList().get(0).getValue();
                    }
                }
            }

            if (Session.getInstance(recyclerView.getContext()).getTransactionManager().hasPendingTransactionsFor(task)) {
                this.color = R.color.colorOfflineBackground;
            } else {
                this.color = R.color.cardview_light_background;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            holder.getTaskTitle().setText(title);
            holder.getTaskDescription().setText(description);
            holder.getTaskUser().setText(username);
            holder.getTaskStatus().setStatus(status, price);
            holder.getTaskView().setBackgroundColor(
                    recyclerView.getContext().getResources().getColor(color));

            holder.setTask(task);

            holder.getTaskView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewTask(sessionUser, task);
                }
            });
        }
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
        Intent intent = new Intent(this.recyclerView.getContext(), ViewTaskActivity.class);
        intent.putExtra(ViewTaskActivity.EXTRA_TARGET_TASK, task);

        this.recyclerView.getContext().startActivity(intent);
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
