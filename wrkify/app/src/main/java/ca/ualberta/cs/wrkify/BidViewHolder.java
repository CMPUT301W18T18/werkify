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
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
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
    private ConstraintLayout cardLayout;
    private View line;
    private Drawable defaultBackground;
    private Drawable selectedBackground;
    private boolean isRequester = true;

    public BidViewHolder(View v) {
        super(v);
        this.taskCompleter = v.findViewById(R.id.bidListItem_taskCompleter);
        this.bidAmount = v.findViewById(R.id.bidListItem_bidAmount);
        this.viewProfile = v.findViewById(R.id.bidListItem_viewProfile);
        this.reject = v.findViewById(R.id.bidListItem_reject);
        this.accept = v.findViewById(R.id.bidListItem_accept);
        this.cardView = v.findViewById(R.id.bidListItem_cardView);
        this.cardLayout = v.findViewById(R.id.bidListItem_cardLayout);
        this.line = v.findViewById(R.id.bidListItem_line);

        defaultBackground = cardLayout.getBackground();
        selectedBackground = defaultBackground;
        itemView.setTranslationZ(0f);
    }

    public void setData(Bid b) {
        taskCompleter.setText(b.getBidder().getUsername());
        bidAmount.setText("$" + b.getValue().toString());
    }

    public void expand() {
        this.viewProfile.setVisibility(View.VISIBLE);
        if (this.isRequester) {
            this.reject.setVisibility(View.VISIBLE);
            this.accept.setVisibility(View.VISIBLE);
        }
        this.cardLayout.setBackground(selectedBackground);
        this.itemView.setTranslationZ(8f);
    }

    public void collapse() {
        this.viewProfile.setVisibility(View.GONE);
        this.reject.setVisibility(View.GONE);
        this.accept.setVisibility(View.GONE);
        this.cardLayout.setBackground(defaultBackground);
        this.itemView.setTranslationZ(0f);
    }

    public void totalCollapse() {
        this.viewProfile.setVisibility(View.GONE);
        this.reject.setVisibility(View.GONE);
        this.accept.setVisibility(View.GONE);
        this.bidAmount.setVisibility(View.GONE);
        this.taskCompleter.setVisibility(View.GONE);
        this.cardLayout.setBackground(defaultBackground);
        this.itemView.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        this.itemView.setTranslationZ(0f);
    }

    public void restoreSize() {
        bidAmount.setVisibility(View.VISIBLE);
        taskCompleter.setVisibility(View.VISIBLE);
        this.itemView.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
    }

    public TextView getTaskCompleter() {
        return this.taskCompleter;
    }

    public TextView getBidAmount() {
        return bidAmount;
    }

    public Button getViewProfile() {
        return viewProfile;
    }

    public Button getReject() {
        return reject;
    }

    public Button getAccept() {
        return accept;
    }

    public CardView getCardView() {
        return cardView;
    }

    public ConstraintLayout getCardLayout(){
        return cardLayout;
    }

    public Drawable getDefaultBackground(){
        return defaultBackground;
    }

    public void setDefaultBackground(Drawable bg){
        this.defaultBackground = bg;
    }

    public Drawable getSelectedBackground(){
        return selectedBackground;
    }

    public void setSelectedBackground(Drawable bg){
        this.selectedBackground = bg;
    }

    public void setIsRequester(boolean isRequester){
        this.isRequester = isRequester;
    }

    public boolean getIsRequester(){
        return isRequester;
    }

}
