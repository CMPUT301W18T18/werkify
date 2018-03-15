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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;



public class ViewBidsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_bids);
        setContentView(R.layout.bidlistitem);



        int[] a = {R.id.button, R.id.button2, R.id.button3};

        for (int i = 0; i < a.length; i++) {
            final Button b = findViewById(a[i]);

            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            b.setTypeface(Typeface.DEFAULT_BOLD);
                            break;

                        case MotionEvent.ACTION_UP:
                            b.setTypeface(Typeface.DEFAULT);
                            break;

                    }

                    return false;
                }
            });
        }





        setTitle("Bids");
    }



}
