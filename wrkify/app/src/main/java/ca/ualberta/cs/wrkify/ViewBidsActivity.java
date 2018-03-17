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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * ViewBidsActivity shows a list of bids on a task that the viewer can scroll through, and if the
 * viewer is a requester of said task, they can accept or reject bids
 *
 * Start with an Intent that has extras:
 * EXTRA_VIEWBIDS_VIEWER: ConcreteUser who is viewing the list
 * EXTRA_VIEWBIDS_TASK: ConcreteTask that is being viewed
 */
public class ViewBidsActivity extends AppCompatActivity {
    public static final String EXTRA_VIEWBIDS_VIEWER = "ca.ualberta.cs.wrkify.EXTRA_VIEWBIDS_VIEWER";
    public static final String EXTRA_VIEWBIDS_TASK = "ca.ualberta.cs.wrkify.EXTRA_VIEWBIDS_TASK";

    protected RecyclerView recyclerView;
    protected BidListAdapter adapter;
    protected TextView bidCountView;

    protected Intent intent;

    /**
     * Initializes activity, setting up all Views and data
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);

        intent = getIntent();

        //Get the data
        ConcreteUser viewer = (ConcreteUser) intent.getSerializableExtra(EXTRA_VIEWBIDS_VIEWER);
        ConcreteTask task = (ConcreteTask) intent.getSerializableExtra(EXTRA_VIEWBIDS_TASK);

        boolean isRequester = viewer.getUsername().equals(task.getRequester().getUsername());

        recyclerView = findViewById(R.id.bidListRecyclerView);
        bidCountView = findViewById(R.id.bidListActivity_bidCount);

        adapter = new BidListAdapter(this, task, isRequester);
        adapter.setAnimationTime(200);

        ScrollDisableableLinearLayoutManager manager = new ScrollDisableableLinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateCount();
            }
        };
        adapter.registerAdapterDataObserver(observer);
        setTitle("Bids");

        updateCount();
    }


    /**
     * Refreshes the View which displays the number of bids in the list
     */
    protected void updateCount() {
        int count = adapter.getItemCount();
        String message;
        if (count == 1) {
            message = " bid so far";
        } else {
            message = " bids so far";
        }

        this.bidCountView.setText(Integer.toString(count) + message);
    }

}
