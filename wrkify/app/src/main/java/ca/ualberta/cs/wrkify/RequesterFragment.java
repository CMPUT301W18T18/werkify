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

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * RequesterFragment displays the lists of task that a task requester needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 */
public class RequesterFragment extends TasksOverviewFragment {
    @Override
    protected List<ArrayList<Task>> getTaskLists() {
        // TODO get actual tasks



        User u1 = WrkifyClient.getInstance().create(User.class, "Peter2", "peter@example.com", "1234567890");
        User u2 = WrkifyClient.getInstance().create(User.class,"Stefan2", "stefan@example.com", "1234567890");
        User u3 = WrkifyClient.getInstance().create(User.class,"Stefan3", "stefan@example.com", "1234567890");
        User u4 = WrkifyClient.getInstance().create(User.class,"Stefan4", "stefan@example.com", "1234567890");

        //Temp tasks

        Task t1 = WrkifyClient.getInstance().create(Task.class, "Taskk 1", u1, "This is task 1");
        Task t2 = WrkifyClient.getInstance().create(Task.class,"Taskk 2", u2, "This is task 2");
        t2.addBid(new Bid(24.0, u3));
        t2.addBid(new Bid(325.3, u4));

        //remove these later

        ArrayList<Task> tasks1 = new ArrayList<>();
        tasks1.add(t1);
        tasks1.add(t2);



        /*
        List<Task> tasksReturned = new ArrayList<>();

        try {
            tasksReturned = Searcher.findTasksByRequester(WrkifyClient.getInstance(), Session.getInstance(getContext()).getUser());
        } catch (Exception e) {
            Log.e("RequesterFragment", "Some IO Exception");
        }
        */

        //Requested, assigned, closed


        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(tasks1);
        pageTaskLists.add(tasks1);
        pageTaskLists.add(tasks1);
        /*
        pageTaskLists.add(new ArrayList<Task>(tasksReturned));
        pageTaskLists.add(new ArrayList<Task>(tasksReturned));
        pageTaskLists.add(new ArrayList<Task>(tasksReturned));
        */

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
}
