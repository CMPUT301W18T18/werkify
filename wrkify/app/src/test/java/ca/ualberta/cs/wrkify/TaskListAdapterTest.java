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

import android.support.v7.app.AppCompatActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 *
 */

public class TaskListAdapterTest {

    @Test
    public void constructorTestConcreteTask(){
        AppCompatActivity context = new AppCompatActivity();
        List<Task> taskList = new ArrayList<>();
        User user = new User("user","abc@abc.com","111-1111");
        RemoteReference<User> rUser = null;
        Task task = new Task("Task 1",rUser,"description");
        taskList.add(task);
        TaskListAdapter<Task> adapter = new TaskListAdapter<>(context,taskList,true,user);
        assertEquals(adapter.getTaskList(),taskList);
        assertEquals(adapter.getTaskList().get(0).getTitle(),"Task 1");
    }
}

