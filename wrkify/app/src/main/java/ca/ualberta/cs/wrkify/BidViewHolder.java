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

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Used by BidListAdapter. Holds Views in the inflated layout representing a Bid item.
 * Handles setting of Bid data given a Bid
 * Handles collapse/expand by hiding/showing Views, though does not animate them
 * Hides accept/reject buttons when necessary
 * @see BidListAdapter
 */
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

    /**
     * Constructor; Initializes contained Views
     * @param v the View to be contained
     * @param isRequester if the viewer is a requester of the task which this Bid belongs to
     */
    public BidViewHolder(View v, boolean isRequester) {
        super(v);
        this.isRequester = isRequester;
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

    /**
     * @param b Bid whose data is to be displayed in the View
     */
    public void setData(Bid b) {
        try {
            taskCompleter.setText(b.getRemoteBidder(WrkifyClient.getInstance()).getUsername());
        } catch (IOException e) {
            // TODO handle this correctly
            return;
        }
        bidAmount.setText(b.getValue().toString());
    }

    /**
     * Expand the ViewHolder by setting the appropriate Views visible. Does not animate
     */
    public void expand() {
        this.viewProfile.setVisibility(View.VISIBLE);
        if (this.isRequester) {
            this.reject.setVisibility(View.VISIBLE);
            this.accept.setVisibility(View.VISIBLE);
        }
        this.cardLayout.setBackground(selectedBackground);
        this.itemView.setTranslationZ(8f);
    }

    /**
     * Collapse the ViewHolder by setting the appropriate Views gone. Does not animate
     */
    public void collapse() {
        this.viewProfile.setVisibility(View.GONE);
        this.reject.setVisibility(View.GONE);
        this.accept.setVisibility(View.GONE);
        this.cardLayout.setBackground(defaultBackground);
        this.itemView.setTranslationZ(0f);
    }

    /**
     * Set all Views gone; used for animating item deletions. Does not animate
     */
    public void totalCollapse() {
        this.viewProfile.setVisibility(View.GONE);
        this.reject.setVisibility(View.GONE);
        this.accept.setVisibility(View.GONE);
        this.bidAmount.setVisibility(View.GONE);
        this.taskCompleter.setVisibility(View.GONE);
        this.cardLayout.setBackground(defaultBackground);
        this.itemView.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        itemView.setTranslationZ(0f);
    }

    /**
     * Set views to visible to reuse this view after animating its deletion. Does not animate
     */
    public void restoreSize() {
        bidAmount.setVisibility(View.VISIBLE);
        taskCompleter.setVisibility(View.VISIBLE);
        itemView.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
    }

    /**
     *
     * @return TextView showing username of person who made this Bid
     */
    public TextView getTaskCompleter() {
        return this.taskCompleter;
    }

    /**
     * @return TextView showing Bid amount
     */
    public TextView getBidAmount() {
        return bidAmount;
    }

    /**
     * @return "View Profile" Button
     */
    public Button getViewProfile() {
        return viewProfile;
    }

    /**
     * @return "Reject" Button
     */
    public Button getReject() {
        return reject;
    }

    /**
     * @return "Accept" Button
     */
    public Button getAccept() {
        return accept;
    }

    /**
     * @return CardView of this Bid item
     */
    public CardView getCardView() {
        return cardView;
    }

    /**
     * @return Background drawn when the item is not expanded
     */
    public Drawable getDefaultBackground(){
        return defaultBackground;
    }

    /**
     * Set the default background
     * @param bg Drawable used as the background when the item is not expanded
     */
    public void setDefaultBackground(Drawable bg){
        this.defaultBackground = bg;
    }

    /**
     * @return Drawable background to be used when the item is expanded
     */
    public Drawable getSelectedBackground(){
        return selectedBackground;
    }

    /**
     * Set the selected background
     * @param bg Drawable background to be used when the item is expanded
     */
    public void setSelectedBackground(Drawable bg){
        this.selectedBackground = bg;
    }

    /**
     * Set isRequester to given value
     * @param isRequester true if user is the requester of the task containing this Bid
     */
    public void setIsRequester(boolean isRequester){
        this.isRequester = isRequester;
    }

    /**
     * @return True if user is the requester of the task containing this Bid
     */
    public boolean getIsRequester(){
        return isRequester;
    }

}
