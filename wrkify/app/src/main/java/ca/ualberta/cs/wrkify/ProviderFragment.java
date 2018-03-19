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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * ProviderFragment displays the lists of task that a task provider needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 */
public class ProviderFragment extends TasksOverviewFragment {
    @Override
    protected List<ArrayList<Task>> getTaskLists() {
        List<Task> biddedTasks;
        List<Task> rawProvidedTasks;
        try {
            rawProvidedTasks = Searcher.findTasksByProvider(WrkifyClient.getInstance(), Session.getInstance(getContext()).getUser());
            biddedTasks = Searcher.findTasksByProvider(WrkifyClient.getInstance(), Session.getInstance(getContext()).getUser());
        } catch (IOException e) {
            // TODO You are offline.
            return new ArrayList<>();
        }

        // Filter tasks into assigned, completed
        ArrayList<Task> assignedTasks = new ArrayList<>();
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (Task t: rawProvidedTasks) {
            switch (t.getStatus()) {
                case ASSIGNED:
                    assignedTasks.add(t);
                case DONE:
                    completedTasks.add(t);
            }
        }

        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(assignedTasks);
        pageTaskLists.add(new ArrayList<>(biddedTasks));
        pageTaskLists.add(completedTasks);

        return pageTaskLists;
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{"Assigned", "Bidded", "Completed"};
    }
    
    @Override
    protected String getAppBarTitle() {
        return "My tasks";
    }

    @Override
    protected boolean isAddButtonEnabled(int index) {
        return false;
    }
}
