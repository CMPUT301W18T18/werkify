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
        //makeData2(100);


        recyclerView = findViewById(R.id.bidListRecyclerView);

        adapter = new BidListAdapter(this, bids);
        adapter.setAnimationTime(200);

        ScrollDisableableLinearLayoutManager manager = new ScrollDisableableLinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Log.i("Size of data set:", Integer.toString(bids.size()));
        setTitle("Bids");
    }


    protected void makeData2(int amount){
        bids = new ArrayList<Bid>();
        users = new ArrayList<User>();

        for (int i = 0; i < amount; i++) {
            bids.add(new Bid(Double.parseDouble(Integer.toString(i)), new ConcreteUser("User" + Integer.toString(i), "email@webiste.com", "230-234-1234") ));
        }
    }

    protected void makeData(){
        bids = new ArrayList<Bid>();
        users = new ArrayList<User>();

        users.add(new ConcreteUser("UsernameHere", "email@website.com", "780-123-4567"));
        users.add(new ConcreteUser("UsernameHere2", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere3", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere4", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere5", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere6", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere7", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere8", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere9", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere10", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere11", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere12", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere13", "email2@website.com", "780-223-4567"));
        users.add(new ConcreteUser("UsernameHere14", "email2@website.com", "780-223-4567"));


        bids.add(new Bid(20.0, users.get(0)));
        bids.add(new Bid(41.2, users.get(1)));
        bids.add(new Bid(41.3, users.get(2)));
        bids.add(new Bid(41.4, users.get(3)));
        bids.add(new Bid(41.5, users.get(4)));
        bids.add(new Bid(41.6, users.get(5)));
        bids.add(new Bid(41.7, users.get(6)));
        bids.add(new Bid(41.8, users.get(7)));
        bids.add(new Bid(41.9, users.get(8)));
        bids.add(new Bid(42.0, users.get(9)));
        bids.add(new Bid(42.1, users.get(10)));
        bids.add(new Bid(42.2, users.get(11)));
        bids.add(new Bid(42.3, users.get(12)));
        bids.add(new Bid(42.4, users.get(13)));


    }



}
