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

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;

public abstract class TaskImageListAdapter extends RecyclerView.Adapter<TaskImageListAdapter.ImageViewHolder> {
    public static class ImageViewHolder extends RecyclerView.ViewHolder{ //do this later

        private ImageButton button;
        public ImageViewHolder(View itemView, Bitmap image) {
            super(itemView);
            button = itemView.findViewById(R.id.taskImageListButton);
            setImage(image);
        }

        public void setImage(Bitmap image) {
            //this.button.setImageBitmap(image);s
            this.button.setImageBitmap(Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 3.0), (int) (image.getHeight() * 3.0), false));
        }
    }

    protected ArrayList<CompressedBitmap> thumbnails;
    protected RecyclerView recyclerView;

    private static final int itemLayoutId = R.layout.task_image_list_item;

    public TaskImageListAdapter(ArrayList<CompressedBitmap> thumbnails) {
        this.thumbnails = thumbnails;

    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayoutId, viewGroup, false);
        ImageViewHolder holder = new ImageViewHolder(v, thumbnails.get(position).getBitmap());
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        holder.setImage(thumbnails.get(position).getBitmap());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(position);
            }
        });

        //Nothing for now?
    }

    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        Log.i("Recycling", holder.toString());
        //Nothing for now?
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public abstract void buttonClicked(int position);

}
