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
        // TODO get actual tasks
        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(new ArrayList<Task>());
        pageTaskLists.add(new ArrayList<Task>());
        pageTaskLists.add(new ArrayList<Task>());

        return pageTaskLists;
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{"Assigned", "Bidded", "Completed"};
    }
}
