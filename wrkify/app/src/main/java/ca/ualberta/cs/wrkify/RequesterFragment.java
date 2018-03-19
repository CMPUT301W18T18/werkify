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
import android.util.Log;
import android.view.View;

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


        List<Task> tasks = new ArrayList<>();

        try {
            tasks = Searcher.findTasksByRequester(WrkifyClient.getInstance(), Session.getInstance(getContext()).getUser());
        } catch (Exception e) {
            Log.e("RequesterFragment", "Some exception happened while searching");
        }

        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(new ArrayList<Task>(tasks));
        pageTaskLists.add(new ArrayList<Task>(tasks));
        pageTaskLists.add(new ArrayList<Task>(tasks));

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

    @Override
    protected boolean isAddButtonEnabled(int index) {
        return (index == 0);
    }

    @Override
    protected void onAddButtonClick(View v) {
        Intent newTaskIntent = new Intent(getContext(), EditTaskActivity.class);
        startActivity(newTaskIntent);
    }
}
