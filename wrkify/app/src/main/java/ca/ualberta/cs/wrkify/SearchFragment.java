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
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SeachFragment allows users to search for tasks
 * this Fragment is displayed by MainActivity
 * TODO: this class has not been implemented
 *
 * @see MainActivity
 */
public class SearchFragment extends Fragment {
    // From https://developer.android.com/guide/components/fragments.html (2018-03-11)
    private List<Task> taskList = new ArrayList<>();
    private TaskListAdapter<Task> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.taskSearchRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        AppCompatActivity context = new AppCompatActivity();
        TaskListAdapter<Task> ad = new TaskListAdapter<Task>(context,this.taskList);
        recyclerView.setAdapter(ad);
        this.adapter = ad;

        final EditText searchBar = (EditText) rootView.findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchTasks(searchBar.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
        return rootView;
    }



    public boolean searchTasks(String query){
        List<Task> tasks;
        try {
            tasks = Searcher.findTasksByKeywords(WrkifyClient.getInstance(), query);
        } catch (IOException e){
            tasks = new ArrayList<>();
            return false;
        }
        if(tasks==null){
            this.taskList = new ArrayList<>();
            return true;
        }
        this.taskList = tasks;
        this.adapter.setTaskList(tasks);
        return true;
    }
}
