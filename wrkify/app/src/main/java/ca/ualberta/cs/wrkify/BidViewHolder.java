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

import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BidViewHolder extends RecyclerView.ViewHolder{
    private TextView taskCompleter;
    private TextView bidAmount;
    private Button viewProfile;
    private Button reject;
    private Button accept;
    private CardView cardView;


    public BidViewHolder(View v){
        super(v);
        this.taskCompleter = v.findViewById(R.id.bidListItem_taskCompleter);
        this.bidAmount = v.findViewById(R.id.bidListItem_bidAmount);
        this.viewProfile = v.findViewById(R.id.bidListItem_viewProfile);
        this.reject = v.findViewById(R.id.bidListItem_reject);
        this.accept = v.findViewById(R.id.bidListItem_accept);
        this.cardView = v.findViewById(R.id.bidListItem_cardView);

        setButtonTextBolding(reject);
        setButtonTextBolding(accept);
        setButtonTextBolding(viewProfile);

    }


    private void setButtonTextBolding(final Button b){
        b.setTypeface(Typeface.DEFAULT);

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        b.setTypeface(Typeface.DEFAULT);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        b.setTypeface(Typeface.DEFAULT_BOLD);
                        break;
                }

                return false;
            }
        });
    }

    public void setData(Bid b){
        taskCompleter.setText(b.getBidder().getUsername());
        bidAmount.setText(b.getValue().toString());
    }

    public void expand(){
        this.viewProfile.setVisibility(View.VISIBLE);
        this.reject.setVisibility(View.VISIBLE);
        this.accept.setVisibility(View.VISIBLE);
    }

    public void collapse(){
        this.viewProfile.setVisibility(View.GONE);
        this.reject.setVisibility(View.GONE);
        this.accept.setVisibility(View.GONE);
    }

    public TextView getTaskCompleter(){
        return this.taskCompleter;
    }

    public TextView getBidAmount(){
        return bidAmount;
    }


    public Button getViewProfile(){
        return viewProfile;
    }

    public Button getReject(){
        return reject;
    }

    public Button getAccept(){
        return accept;
    }

    public CardView getCardView(){
        return cardView;
    }
}
