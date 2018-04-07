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
import java.util.ArrayList;
import java.util.Arrays;
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
        List<Task> rawProvidedTasks = Session.getInstance(getContext()).getUserProvidedCache();
        List<Task> rawBiddedTasks = Session.getInstance(getContext()).getUserBiddedCache();

        // Filter tasks into assigned, completed
        ArrayList<Task> assignedTasks = new ArrayList<>();
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (Task t: rawProvidedTasks) {
            switch (t.getStatus()) {
                case ASSIGNED:
                    assignedTasks.add(t);
                    break;
                case DONE:
                    completedTasks.add(t);
            }
        }

        // Only show bidded tasks (you can also be a bidder on an assigned task)
        ArrayList<Task> biddedTasks = new ArrayList<>();
        for (Task t: rawBiddedTasks) {
            switch (t.getStatus()) {
                case BIDDED:
                    biddedTasks.add(t);
            }
        }

        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(assignedTasks);
        pageTaskLists.add(biddedTasks);
        pageTaskLists.add(completedTasks);

        return pageTaskLists;
    }

    @Override
    protected List<String> getTabTitles() {
        return Arrays.asList("Assigned", "Bidded", "Completed");
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
