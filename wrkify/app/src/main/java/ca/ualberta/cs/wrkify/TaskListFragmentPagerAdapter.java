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

import android.support.annotation.Nullable;
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
    private List<String> pageTitles;
    
    /**
     * Creates a TaskListFragmentPagerAdapter for the specified set of task lists.
     * Each entry in the outer pageTaskLists list will be a single page containing
     * the entry's inner list displayed in a TaskListFragment.
     * @param fragmentManager Fragment manager to instantiate from (passed to super FragmentPagerAdapter)
     * @param pageTaskLists List of task lists to display as pages
     */
    public TaskListFragmentPagerAdapter(FragmentManager fragmentManager, List<ArrayList<Task>> pageTaskLists, List<String> pageTitles) {
        super(fragmentManager);
        Log.i("-->", "task lists: " + pageTaskLists.size());

        if (pageTaskLists.size() != pageTitles.size()) {
            throw new IllegalArgumentException();
        }

        this.pageTaskLists = pageTaskLists;
        this.pageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("-->", "got item " + position);
        return TaskListFragment.makeTaskList(this.pageTaskLists.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.pageTitles.get(position);
    }

    @Override
    public int getCount() {
        return pageTaskLists.size();
    }

    /**
     * Adds a task to one of the adapter's lists.
     * Used to update the adapter when tasks are created or otherwise
     * come into membership of the list.
     * @param listIndex list to add to
     * @param task task to add
     */
    public void appendTaskToList(int listIndex, Task task) {
        this.pageTaskLists.get(listIndex).add(task);
    }
}
