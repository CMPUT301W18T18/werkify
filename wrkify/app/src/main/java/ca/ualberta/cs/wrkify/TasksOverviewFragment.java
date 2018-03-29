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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toolbar;

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
    private FloatingActionButton addButton;
    
    // From https://developer.android.com/guide/components/fragments.html (2018-03-11)
    // (basic structure)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        // Find views
        this.tabLayout = view.findViewById(R.id.overviewTabBar);
        this.pager = view.findViewById(R.id.overviewPager);
        this.addButton = view.findViewById(R.id.overviewAddButton);

        UserView userView = view.findViewById(R.id.overviewSelfView);
        Toolbar toolbar = view.findViewById(R.id.overviewToolbar);

        // Set title
        toolbar.setTitle(getAppBarTitle());

        // Set user view
        userView.setUserName(Session.getInstance(getContext()).getUser().getUsername());
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create user view menu
                PopupMenu popup = new PopupMenu(getActivity(), v);
                getActivity().getMenuInflater().inflate(R.menu.overview, popup.getMenu());

                popup.getMenu().findItem(R.id.menuItemViewProfile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // View profile menu item
                        Intent viewUserIntent = new Intent(getContext(), ViewProfileActivity.class);
                        viewUserIntent.putExtra(ViewProfileActivity.USER_EXTRA, Session.getInstance(getContext()).getUser());
                        startActivity(viewUserIntent);

                        return true;
                    }
                });

                popup.getMenu().findItem(R.id.menuItemLogout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Logout menu item
                        Session.getInstance(getContext()).logout(getContext());
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();

                        return false;
                    }
                });

                popup.show();
            }
        });

        // Create tabs
        for (String tabTitle : getTabTitles()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitle);
            this.tabLayout.addTab(tab);
        }


        // Refresh if necessary
        tryRefreshCaches();

        return view;
    }

    private void populateTaskLists() {
        // Bind adapter to pager
        // (getChildFragmentManager via https://stackoverflow.com/questions/15196596/ (2018-03-17))
        pager.setAdapter(new TaskListFragmentPagerAdapter(getChildFragmentManager(), getTaskLists()));

        // Initialize add button
        addButton.setVisibility(isAddButtonEnabled(0)? View.VISIBLE : View.GONE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick(v);
            }
        });

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

                if (isAddButtonEnabled(position)) {
                    showAddButton();
                } else {
                    hideAddButton();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ignored
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);

                if (positionOffset != 0 && addButton.getVisibility() == View.VISIBLE) {
                    hideAddButton();
                } else if (positionOffset == 0 && isAddButtonEnabled(position)) {
                    showAddButton();
                }
            }
        });
    }

    /**
     * Hides the add button.
     * TODO add transition
     */
    private void hideAddButton() {
        addButton.setVisibility(View.GONE);
    }

    /**
     * Shows the add button.
     * TODO add transition
     */
    private void showAddButton() {
        addButton.setVisibility(View.VISIBLE);
    }

    /**
     * Gets a reference to the Fragment's ViewPager.
     * @return ViewPager containing the TaskListViews
     */
    protected ViewPager getPager() {
        return this.pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRefreshCaches();
    }

    private void tryRefreshCaches() {
        RefreshAsync.startAsyncRefresh(Session.getInstance(getContext()), new AsyncWatcher<Boolean>() {
            @Override
            public void onStartWaiting() {
                // ignored
            }

            @Override
            public void onFinishWaiting() {
                // ignored
            }

            @Override
            public void onTaskFinished(Boolean result) {
                populateTaskLists();
            }

            @Override
            public void onTaskInterrupted() {
                populateTaskLists();
            }
        });
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
    
    /**
     * Determines the 'activity title' shown when viewing this Fragment.
     * (It appears in the override app bar.)
     */
    protected abstract String getAppBarTitle();

    /**
     * Checks whether the Add button should be shown for a tab index.
     * @param index Index of the current tab
     * @return true to show the Add button, false to hide it
     */
    protected abstract boolean isAddButtonEnabled(int index);

    /**
     * Called when the add button is clicked.
     * @param v View corresponding to the add button
     */
    protected void onAddButtonClick(View v) {

    }
}
