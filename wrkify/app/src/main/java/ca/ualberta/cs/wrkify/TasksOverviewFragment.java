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


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * TasksOverviewFragment displays a set of tab pages, each containing a
 * tab list (a TaskListFragment). The contents of the lists can be set
 * by the subclass by overriding getTaskLists. The number of tabs and
 * their titles can be set by overriding getTabs.
 *
 * @see TaskListFragment
 */
abstract class TasksOverviewFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager pager;

    // From https://developer.android.com/guide/components/fragments.html (2018-03-11)
    // (basic structure)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        // Find views
        this.tabLayout = view.findViewById(R.id.overviewTabBar);
        this.pager = view.findViewById(R.id.overviewPager);

        // Create tabs
        for (String tabTitle: getTabTitles()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitle);
            this.tabLayout.addTab(tab);
        }

        // Bind adapter to pager
        // (getChildFragmentManager via https://stackoverflow.com/questions/15196596/ (2018-03-17))
        pager.setAdapter(new TaskListFragmentPagerAdapter(getChildFragmentManager(), getTaskLists()));

        // Switch pager pages on tab move
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("-->", "switching to tab " + tab.getPosition());
                pager.setCurrentItem(tab.getPosition());
                pager.forceLayout();
                Log.i("-->", "now: " + pager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // ignored
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // ignored
            }
        });

        // Switch tabs on pager page
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ignored
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }
        });

        return view;
    }
    
    /**
     * Selects which tasks to display in the task lists. Each element
     * in the outer list is a list of tasks that will be displayed in a tab.
     * Length of the list should be the same as the length of the list
     * returned by getTabTitles for correct behaviour.
     * @return list of task lists
     */
    protected abstract List<ArrayList<Task>> getTaskLists();

    /**
     * Determines the titles of the tabs (and the number of tabs).
     * Each string in the returned list is used as a tab title from
     * first to last. Length of the returned list should be the same
     * as the length of the list returned by getTaskLists for
     * correct behaviour.
     * @return list of tab titles
     */
    protected abstract String[] getTabTitles();
}
