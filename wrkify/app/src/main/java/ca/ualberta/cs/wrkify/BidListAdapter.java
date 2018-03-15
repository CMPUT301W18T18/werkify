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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BidListAdapter extends RecyclerView.Adapter<BidListAdapter.BidViewHolder> {
    public static int itemLayoutId = R.layout.bidlistitem;
    private Context context;
    private List<Bid> data;
    private RecyclerView recyclerView;

    public BidListAdapter(Context context, List<Bid> data) {
        this.context = context;
        this.data = data;
    }

    class BidViewHolder extends RecyclerView.ViewHolder{
        private TextView taskCompleter;
        private TextView bidAmount;
        private Button viewProfile;
        private Button reject;
        private Button accept;


        public BidViewHolder(View v){
            super(v);
            this.taskCompleter = v.findViewById(R.id.bidListItem_taskCompleter);
            this.bidAmount = v.findViewById(R.id.bidListItem_bidAmount);
            this.bidAmount = v.findViewById(R.id.bidListItem_bidAmount);

            taskCompleter.setText("XD");
        }

        public TextView getTaskCompleter(){
            return this.taskCompleter;
        }

    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(BidListAdapter.itemLayoutId, viewGroup, false);
        BidViewHolder holder = new BidViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BidViewHolder holder, int position){
        Log.i("BINDING VIEW HOLDER:", Integer.toString(position));
        holder.getTaskCompleter().setText("asdasd");
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
        return;
    }


}
