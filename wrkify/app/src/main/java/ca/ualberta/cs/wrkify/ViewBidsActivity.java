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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;


public class ViewBidsActivity extends Activity {
    protected ArrayList<Bid> bids;
    protected RecyclerView recyclerView;
    protected BidListAdapter adapter;
    protected TextView bidCount;

    protected ConcreteUser viewer;
    protected ConcreteTask task;
    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);

        intent = getIntent();

        getData();
        boolean isRequester = viewer.getUsername().equals(task.getRequester().getUsername());

        recyclerView = findViewById(R.id.bidListRecyclerView);

        adapter = new BidListAdapter(this, bids, isRequester){
            @Override
            protected void acceptClicked(int position){
                accept(position);
            }
        };
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

        bidCount = findViewById(R.id.bidListActivity_bidCount);
        updateCount();
    }

    protected void updateCount() {
        int count = adapter.getItemCount();
        String message;
        if (count == 1) {
            message = " bid so far";
        } else {
            message = " bids so far";
        }

        this.bidCount.setText(Integer.toString(count) + message);
    }

    protected void getData() {
        bids = new ArrayList<Bid>();

        viewer = (ConcreteUser) intent.getSerializableExtra("viewer");
        task = (ConcreteTask) intent.getSerializableExtra("task");

        bids = task.getBidList();
    }

    protected void accept(int position) {
        task.acceptBid(bids.get(position));
        finish();
    }
}
