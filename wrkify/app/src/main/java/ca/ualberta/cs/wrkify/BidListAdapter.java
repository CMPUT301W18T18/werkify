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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BidListAdapter extends RecyclerView.Adapter<BidViewHolder> {
    public static int itemLayoutId = R.layout.bidlistitem;
    private Context context;
    private List<Bid> data;
    private RecyclerView recyclerView;


    private int currentSelectedPos = -1;
    private BidViewHolder currentSelected = null;
    private boolean selectedIsVisible = false;


    public BidListAdapter(Context context, List<Bid> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getItemCount(){
        return data.size();
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(BidListAdapter.itemLayoutId, viewGroup, false);
        BidViewHolder holder = new BidViewHolder(v);

        holder.collapse();

        return holder;
    }

    private void cardClicked(BidViewHolder holder, int position){
        if (position != currentSelectedPos) {
            if (selectedIsVisible) {
                expandAndCollapseViews(holder, currentSelected);
            } else {
                expandView(holder);
            }

            currentSelected = holder;
            selectedIsVisible = true;
            currentSelectedPos = position;
        } else {

            collapseView(holder);
            currentSelected = null;
            selectedIsVisible = false;
            currentSelectedPos = -1;
        }

    }

    private void expandView(BidViewHolder holder){
        ChangeBounds cb = new ChangeBounds();
        cb.setDuration(200);
        TransitionManager.beginDelayedTransition(recyclerView, cb);
        holder.expand();

    }

    private void collapseView(BidViewHolder holder){
        ChangeBounds cb = new ChangeBounds();
        cb.setDuration(200);
        TransitionManager.beginDelayedTransition(recyclerView, cb);
        holder.collapse();
    }

    private void expandAndCollapseViews(BidViewHolder toExpand, BidViewHolder toCollapse){
        toExpand.expand();
        toCollapse.collapse();
        ChangeBounds cb = new ChangeBounds();
        cb.setDuration(200);
        TransitionManager.beginDelayedTransition(recyclerView, cb);
    }


    @Override
    public void onBindViewHolder(final BidViewHolder holder, final int position){
        Log.i("BINDING VIEW HOLDER:", Integer.toString(position));

        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICKED:", Integer.toString(position));
                cardClicked(holder, position);
            }
        });

        holder.setData(data.get(position));
        restoreSelectionStatus(holder, position);
    }

    private void restoreSelectionStatus(BidViewHolder holder, int position){
        if (currentSelectedPos == position) {
            holder.expand();
            selectedIsVisible = true;
        }
    }

    @Override
    public long getItemId(int position){
        return (long) position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onViewRecycled(BidViewHolder holder){

        Log.i("Recycling: ", Integer.toString(holder.getLayoutPosition()));

        holder.collapse();
        if (holder == currentSelected) {
            selectedIsVisible = false;
        }
    }


}
