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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RequesterFragment displays the lists of task that a task requester needs to see
 * this Fragment is displayed by MainActivity
 *
 * @see MainActivity
 */
public class RequesterFragment extends TasksOverviewFragment {
    private static final int REQUEST_NEW_TASK = 13;
    
    @Override
    protected String getAppBarTitle() {
        return "My posts";
    }

    @Override
    protected boolean isAddButtonEnabled(int index) {
        return (index == 0);
    }

    @Override
    protected void onAddButtonClick(View v) {
        Intent newTaskIntent = new Intent(getContext(), EditTaskActivity.class);
        startActivityForResult(newTaskIntent, REQUEST_NEW_TASK);
    }

    @Override
    protected FragmentPagerAdapter getFragmentPagerAdapter(FragmentManager fragmentManager) {
        return new RequesterFragment.RequesterFragmentPagerAdapter(fragmentManager);
    }

    // TODO move to the taskFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_TASK && resultCode == EditTaskActivity.RESULT_TASK_CREATED) {

        }
    }

    static class RequesterFragmentPagerAdapter extends FragmentPagerAdapter {

        public RequesterFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("-->", "got item " + position);
            switch (position) {
                case 0:
                    return new RequestedListFragment();
                case 1:
                    return new AssignedListFragment();
                case 2:
                    return new ClosedListFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return (new CharSequence[]{"Requested", "Assigned", "Closed"})[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class RequestedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByRequester(
                                client,
                                Session.getInstance(getActivity(),client).getUser(),
                                TaskStatus.REQUESTED, TaskStatus.BIDDED
                        );
                    } catch (IOException e) {
                        return null;
                    }
                }
            };
        }
    }

    public static class AssignedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByRequester(
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

    public static class ClosedListFragment extends TaskListFragment {
        @Override
        protected RemoteList getTaskList() {
            return new RemoteQueryList<Task>(WrkifyClient.getInstance(), Task.class) {
                @Override
                public List query(RemoteClient client) {
                    try {
                        return Searcher.findTasksByRequester(
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
