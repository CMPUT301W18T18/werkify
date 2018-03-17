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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Displays a set of TaskListFragments in a pager.
 * Used in TasksOverviewFragments.
 *
 * @see TaskListFragment
 * @see TasksOverviewFragment
 */
public class TaskListFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<ArrayList<Task>> pageTaskLists;
    
    /**
     * Creates a TaskListFragmentPagerAdapter for the specified set of task lists.
     * Each entry in the outer pageTaskLists list will be a single page containing
     * the entry's inner list displayed in a TaskListFragment.
     * @param fragmentManager Fragment manager to instantiate from (passed to super FragmentPagerAdapter)
     * @param pageTaskLists List of task lists to display as pages
     */
    public TaskListFragmentPagerAdapter(FragmentManager fragmentManager, List<ArrayList<Task>> pageTaskLists) {
        super(fragmentManager);
        Log.i("-->", "task lists: " + pageTaskLists.size());
        this.pageTaskLists = pageTaskLists;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("-->", "got item " + position);
        return TaskListFragment.makeTaskList(this.pageTaskLists.get(position));
    }

    @Override
    public int getCount() {
        return pageTaskLists.size();
    }
}
