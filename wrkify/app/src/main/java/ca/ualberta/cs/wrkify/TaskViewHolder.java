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
 * Created by Stefan on 2018-03-17.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {
    protected CardView taskView;
    protected TextView taskTitle;
    protected TextView taskDescription;
    protected TextView taskUser;
    protected StatusView taskStatus;
    protected Task task;

    TaskViewHolder(View taskView){
        super(taskView);
        this.taskView = (CardView) taskView.findViewById(R.id.taskCardView);
        this.taskTitle = (TextView) this.taskView.findViewById(R.id.taskTitle);
        this.taskDescription = (TextView) this.taskView.findViewById(R.id.taskDescription);
        this.taskUser = (TextView) this.taskView.findViewById(R.id.taskUser);
        this.taskStatus = (StatusView) this.taskView.findViewById(R.id.taskStatus);
    }

    public void setTaskView(CardView cv){
        this.taskView = cv;
    }

    public CardView getTaskView(){
        return  this.taskView;
    }

    public void setTaskTitle(TextView tv){
        this.taskTitle = tv;
    }

    public TextView getTaskTitle(){
        return this.taskTitle;
    }

    public void setTaskDescription(TextView tv){
        this.taskDescription = tv;
    }

    public TextView getTaskDescription(){
        return this.taskDescription;
    }

    public void setTaskUser(TextView tv){
        this.taskUser = tv;
    }

    public TextView getTaskUser(){
        return this.taskUser;
    }

    public void setTaskStatus(StatusView sv){
        this.taskStatus = sv;
    }

    public StatusView getTaskStatus(){
        return this.taskStatus;
    }

    public void setTask(Task task){
        this.task = task;
    }

    public Task getTask(){
        return this.task;
    }
}
