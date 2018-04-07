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
 */
public class ProviderFragment extends TasksOverviewFragment {

    @Override
    protected String getAppBarTitle() {
        return "My tasks";
    }

    @Override
    protected FragmentPagerAdapter getFragmentPagerAdapter(FragmentManager fragmentManager) {
        return new ProviderFragmentPagerAdapter(fragmentManager);
    }

    @Override
    protected boolean isAddButtonEnabled(int index) {
        return false;
    }

    static class ProviderFragmentPagerAdapter extends FragmentPagerAdapter {

        public ProviderFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("-->", "got item " + position);
            switch (position) {
                case 0:
                    return new AssignedListFragment();
                case 1:
                    return new BiddedListFragment();
                case 2:
                    return new CompletedListFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return (new CharSequence[]{"Assigned", "Bidded", "Completed"})[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class AssignedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByProvider(
                                client,
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

    public static class BiddedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByProvider(
                                client,
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

    public static class CompletedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByProvider(
                                client,
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
