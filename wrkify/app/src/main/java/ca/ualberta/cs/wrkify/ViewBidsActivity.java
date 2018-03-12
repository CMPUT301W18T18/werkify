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
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ViewBidsActivity extends Activity {
    protected ArrayList<String> headers;
    protected ArrayList<Integer> items;
    protected ExclusiveExpandableListView listView;
    protected BidListAdapter adapter;
    protected Button addMoreButton;
    protected int nextNumber = 0;


    protected int color_headerDefault;
    protected int color_headerSelected;
    protected int color_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);

        color_headerDefault = Color.parseColor("#FF9F33");
        color_headerSelected = Color.parseColor("#5F5F5F");
        color_item = Color.parseColor("#5F5F5F");



        headers = new ArrayList<String>();
        items = new ArrayList<Integer>();


        listView = (ExclusiveExpandableListView) findViewById(R.id.bidListView);
        listView.setColors(color_headerDefault, color_headerSelected, color_item);

        adapter = new BidListAdapter(this, headers, items, listView);

        listView.setAdapter(adapter);

        for (int i = 0; i < 4; i++) {
            addOneMore();
        }


        Intent intent = getIntent();


        addMoreButton = (Button) findViewById(R.id.addAnotherBid);
        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneMore();
            }
        });


        setTitle("Bids");
    }


    public void addOneMore(){
        headers.add(Integer.toString(nextNumber));
        items.add(nextNumber);
        nextNumber += 1;
        adapter.notifyDataSetChanged();
    }




}
