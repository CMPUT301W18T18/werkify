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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ProviderFragment displays the lists of task that a task provider needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 * @see TasksOverviewFragment
 */
public class ProviderFragment extends TasksOverviewFragment {

    /**
     * get the title of the fragment
     * so TasksOverview fragment can display it
     * @return the fragment title
     */
    @Override
    protected String getAppBarTitle() {
        return "My tasks";
    }

    /**
     * return the FragmentPagerAdapter that this Fragment uses
     * @param fragmentManager the fragmentmanger needed FragmentPagerAdapter
     * @return the FragmentPagerAdapter
     */
    @Override
    protected FragmentPagerAdapter getFragmentPagerAdapter(FragmentManager fragmentManager) {
        return new ProviderFragmentPagerAdapter(fragmentManager);
    }

    /**
     * indicates whether we dispaly the add button for the
     * TaskListFragment at index
     * @param index Index of the current tab
     * @return always false, because we dont need that as a provider
     */
    @Override
    protected boolean isAddButtonEnabled(int index) {
        return false;
    }

    /**
     * ProviderFragmentAdapter is a FragmentPagerAdapter that
     * switches between the Assigned, Bidded, and Complete views
     * that a task provider will see.
     */
    static class ProviderFragmentPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_TABS = 3;

        TaskListFragment[] tabs;

        /**
         * create a ProviderFragmentPagerAdapter
         * @param fragmentManager the fragmentmanager
         */
        public ProviderFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            tabs = new TaskListFragment[NUM_TABS];
        }

        /**
         * gets the TaskListFragment at position
         *
         * @param position the position of the fragment in the tasklist
         * @return the TaskListFragment
         */
        @Override
        public Fragment getItem(int position) {
            Log.i("-->", "got item " + position);
            if (this.tabs[position] != null) {
                return this.tabs[position];
            }

            TaskListFragment newFrag = null;
            switch (position) {
                case 0:
                    newFrag = new AssignedListFragment();
                    break;
                case 1:
                    newFrag = new BiddedListFragment();
                    break;
                case 2:
                    newFrag = new CompletedListFragment();
                    break;
            }

            this.tabs[position] = newFrag;
            return newFrag;
        }

        /**
         * gets the title of a page given the postion
         * @param position the postion of the tab
         * @return the title of the tab
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return (new CharSequence[]{"Assigned", "Bidded", "Completed"})[position];
        }

        /**
         * gets the number of tabs
         * @return the number of tabs
         */
        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }

    /**
     * AssignedListFragment is a TaskListFragment based off of
     * tasks that are assigned to the session user
     *
     * @see TaskListFragment
     */
    public static class AssignedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List<Task> query(RemoteClient client) {
                    try {
                        return client.getSearcher().findTasksByProvider(
                                Session.getInstance(getActivity(),client).getUser(),
                                TaskStatus.ASSIGNED
                        );
                    } catch (IOException e) {
                        return null;
                    }
                }
            };
        }
    }

    /**
     * AssignedListFragment is a TaskListFragment based off of
     * tasks that are bidded on by the session error
     *
     * @see TaskListFragment
     */
    public static class BiddedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List<Task> query(RemoteClient client) {
                    try {
                        return client.getSearcher().findTasksByBidder(
                                Session.getInstance(getActivity(),client).getUser(),
                                TaskStatus.BIDDED
                        );
                    } catch (IOException e) {
                        return null;
                    }
                }
            };
        }
    }

    /**
     * AssignedListFragment is a TaskListFragment based off of
     * tasks that are completed by the session user
     *
     * @see TaskListFragment
     */
    public static class CompletedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List<Task> query(RemoteClient client) {
                    try {
                        return client.getSearcher().findTasksByProvider(
                                Session.getInstance(getActivity(),client).getUser(),
                                TaskStatus.DONE
                        );
                    } catch (IOException e) {
                        return null;
                    }
                }
            };
        }
    }
}
