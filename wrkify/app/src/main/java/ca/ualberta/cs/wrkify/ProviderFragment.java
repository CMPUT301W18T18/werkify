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

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ProviderFragment displays the lists of task that a task provider needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 */
public class ProviderFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager pager;

    // From https://developer.android.com/guide/components/fragments.html (2018-03-11)
    // (basic structure)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider, container, false);

        this.tabLayout = view.findViewById(R.id.providerTabBar);
        this.pager = view.findViewById(R.id.providerViewPager);

        // TODO get actual tasks
        List<ArrayList<Task>> pageTaskLists = new ArrayList<>();
        pageTaskLists.add(new ArrayList<Task>());
        pageTaskLists.add(new ArrayList<Task>());
        pageTaskLists.add(new ArrayList<Task>());

        pager.setAdapter(new TaskListFragmentPagerAdapter(getFragmentManager(), pageTaskLists));
        pager.setCurrentItem(0);

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
}
