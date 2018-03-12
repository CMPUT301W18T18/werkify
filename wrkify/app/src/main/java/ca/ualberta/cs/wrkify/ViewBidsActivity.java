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

import java.util.ArrayList;

public class ViewBidsActivity extends Activity {
    protected ArrayList<String> headers;
    protected ArrayList<Integer> items;
    protected ExclusiveExpandableListView listView;
    protected BidListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);

        headers = new ArrayList<String>();
        items = new ArrayList<Integer>();

        makeFakeData();

        listView = (ExclusiveExpandableListView) findViewById(R.id.bidListView);
        adapter = new BidListAdapter(this, headers, items, listView);

        listView.setAdapter(adapter);



        Intent intent = getIntent();



        setTitle("Bids");
    }


    public void makeFakeData(){
        int sizeOfData = 4;
        for (int i = 0; i < sizeOfData; i++) {
            headers.add(Integer.toString(i));
            items.add(i);
        }
    }



}
