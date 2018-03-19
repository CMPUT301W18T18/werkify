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

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 *TaskViewHolder acts as a cache for task CardViews
 * for us in tandem with an adapter (TaskListAdapter)
 * and a RecyclerView to minimize costly findViewById calls
 * for a potentially large RecyclerView lists of tasks.
 *
 * @see TaskListAdapter
 * @see SearchFragment
 */

public class TaskViewHolder<T extends Task> extends RecyclerView.ViewHolder {
    protected CardView taskView;
    protected TextView taskTitle;
    protected TextView taskDescription;
    protected TextView taskUser;
    protected StatusView taskStatus;
    protected T task;
    /*
    *Sets the root view of the card view
    * and child views.
    *
    * @param view Root view of calling adapter
    */
    TaskViewHolder(View view){
        super(view);
        this.taskView = (CardView) view.findViewById(R.id.taskCardView);
        this.taskTitle = (TextView) this.taskView.findViewById(R.id.taskTitle);
        this.taskDescription = (TextView) this.taskView.findViewById(R.id.taskDescription);
        this.taskUser = (TextView) this.taskView.findViewById(R.id.taskUser);
        this.taskStatus = (StatusView) this.taskView.findViewById(R.id.taskStatus);
    }


    /*
    *Sets the task card view.
    *
    * @param cv allows for post init customization of card view
    */
    public void setTaskView(CardView cv){
        this.taskView = cv;
    }

    /*
    * Returns the task card view for this holder
     */
    public CardView getTaskView(){
        return  this.taskView;
    }


    /*
    *Sets the task title TextView
    *
    *@param tv Textview from a layout
    */
    public void setTaskTitle(TextView tv){
        this.taskTitle = tv;
    }

    /*
    *Returns the TextView that contains the task title for this holder
     */
    public TextView getTaskTitle(){
        return this.taskTitle;
    }


    /*
    *Sets the task description TextView
    *
    *@param tv Textview from a layout
    */
    public void setTaskDescription(TextView tv){
        this.taskDescription = tv;
    }

    /*
    *Returns the TextView that contains the task description for this holder
     */
    public TextView getTaskDescription(){
        return this.taskDescription;
    }


    /*
   *Sets the task user TextView
   *
   *@param tv Textview from a layout
   */
    public void setTaskUser(TextView tv){
        this.taskUser = tv;
    }

    /*
    *Returns the TextView that contains the username for this holder
     */
    public TextView getTaskUser(){
        return this.taskUser;
    }


    /*
   *Sets the task description StatusView
   *
   *@param sv StatusView from a layout
   *@see StatusView
   */
    public void setTaskStatus(StatusView sv){
        this.taskStatus = sv;
    }

    /*
   *Returns the StatusView contained by this holder
   */
    public StatusView getTaskStatus(){
        return this.taskStatus;
    }


    /*
   *Sets the task that is displayed by this holder
   *
   *@param task Task or any subclass of task
   */
    public void setTask(T task){
        this.task = task;
    }

    /*
    *Returns the task contained by this holder
    */
    public Task getTask(){
        return this.task;
    }
}
