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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class ViewBidsActivity extends Activity {

    protected ArrayList<User> users;
    protected ArrayList<Bid> bids;
    protected RecyclerView recyclerView;
    protected BidListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);

        makeData();


        recyclerView = findViewById(R.id.bidListRecyclerView);

        adapter = new BidListAdapter(this, bids);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Log.i("Size of data set:", Integer.toString(bids.size()));
        setTitle("Bids");
    }


    protected void makeData(){
        bids = new ArrayList<Bid>();
        users = new ArrayList<User>();

        users.add(new ConcreteUser("UsernameHere", "email@website.com", "780-123-4567"));
        users.add(new ConcreteUser("UsernameHere2", "email2@website.com", "780-223-4567"));

        bids.add(new Bid(20.0, users.get(0)));
        bids.add(new Bid(41.2, users.get(1)));

    }



}
