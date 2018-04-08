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
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private boolean addButtonVisible;
    private ViewGroup offlineIndicator;

    // From https://developer.android.com/guide/components/fragments.html (2018-03-11)
    // (basic structure)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        
        // Find views
        this.tabLayout = view.findViewById(R.id.overviewTabBar);
        this.pager = view.findViewById(R.id.overviewPager);
        this.addButton = view.findViewById(R.id.overviewAddButton);
        this.offlineIndicator = view.findViewById(R.id.overviewOfflineIndicator);

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

        // Bind adapter to pager
        // (getChildFragmentManager via https://stackoverflow.com/questions/15196596/ (2018-03-17))
        pager.setAdapter(getFragmentPagerAdapter(getChildFragmentManager()));

        // Initialize add button
        addButtonVisible = isAddButtonEnabled(0);
        addButton.setVisibility(addButtonVisible? View.VISIBLE : View.GONE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick(v);
            }
        });

        // Bind tab layout to pager
        tabLayout.setupWithViewPager(pager);

        // Show/hide add button on appropriate pages
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // ignored
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ignored
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset != 0 && addButtonVisible) {
                    addButtonVisible = false;
                    hideAddButton();
                } else if (positionOffset == 0 && isAddButtonEnabled(position) && !addButtonVisible) {
                    addButtonVisible = true;
                    showAddButton();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Session session = Session.getInstance(getActivity());
        TransactionManager transactionManager = session.getTransactionManager();
        if (transactionManager.hasPendingTransactions()) {
            if (transactionManager.flush(WrkifyClient.getInstance())) {
                hideOfflineIndicator();
            } else {
                showOfflineIndicator();
            }

            refreshTaskLists();
        } else {
            hideOfflineIndicator();
        }

        NotificationCollector notificationCollector = Session.getInstance(getActivity())
                .getNotificationCollector();
        notificationCollector.clear();

        try {
            notificationCollector.putNotifications(WrkifyClient.getInstance().getSearcher()
                    .findSignalsByUser(Session.getInstance(getActivity()).getUser()));
        } catch (IOException e) {
            // continue
        }

        updateNotificationDisplay(getView());
    }

    private void refreshTaskLists() {
        // TODO not implemented
    }

    /**
     * Hides the add button.
     * TODO add transition
     */
    private void hideAddButton() {
        TransitionManager.beginDelayedTransition((ViewGroup) getView(), new Slide(Gravity.BOTTOM));
        addButton.setVisibility(View.GONE);
    }

    /**
     * Shows the add button.
     * TODO add transition
     */
    private void showAddButton() {
        TransitionManager.beginDelayedTransition((ViewGroup) getView(), new Slide(Gravity.BOTTOM));
        addButton.setVisibility(View.VISIBLE);
    }

    private void hideOfflineIndicator() {
        offlineIndicator.setVisibility(View.GONE);
    }

    private void showOfflineIndicator() {
        offlineIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Gets a reference to the Fragment's ViewPager.
     * @return ViewPager containing the TaskListViews
     */
    protected ViewPager getPager() {
        return this.pager;
    }

    protected abstract FragmentPagerAdapter getFragmentPagerAdapter(FragmentManager fragmentManager);

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


    /**
     * Updates the notification display.
     * @param view root view of the fragment
     */
    private void updateNotificationDisplay(final View view) {
        Log.i("-->", "und");
        ViewGroup notificationContainer = view.findViewById(R.id.overviewNotificationContainer);
        ViewGroup notificationTarget = view.findViewById(R.id.overviewNotificationTarget);
        ViewGroup notificationOverflow = view.findViewById(R.id.overviewNotificationOverflowIndicator);
        TextView notificationOverflowLabel = view.findViewById(R.id.overviewNotificationOverflowLabel);

        NotificationCollector collector = Session.getInstance(getContext()).getNotificationCollector();
        NotificationInfo notification = collector.getFirstNotification();

        if (notification != null) {
            notificationContainer.setVisibility(View.VISIBLE);

            NotificationView notificationView = new NotificationView(getContext());
            notificationView.setNotification(notification);
            notificationView.setOnDismissListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateNotificationDisplay(view);
                }
            });

            notificationTarget.addView(notificationView);

            // Show overflow indicator if more than one notification
            int extraNotificationCount = collector.getNotificationCount() - 1;
            if (extraNotificationCount > 0) {
                notificationOverflow.setVisibility(View.VISIBLE);
                notificationOverflowLabel.setText(
                        String.format(Locale.US, "%d more notifications", extraNotificationCount));
            } else {
                notificationOverflow.setVisibility(View.GONE);
            }
        } else {
            notificationContainer.setVisibility(View.GONE);
        }
    }
}
