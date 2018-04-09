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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.TreeSet;

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

        public void setSelectedTint(boolean selected) {
            if (selected) {
                button.setColorFilter(Color.argb(140, 0, 75, 200));
            } else {
                button.clearColorFilter();
            }
            button.refreshDrawableState();
            button.invalidateDrawable(button.getDrawable());
        }
    }

    protected ArrayList<CompressedBitmap> thumbnails;
    protected RecyclerView recyclerView;
    protected TreeSet<Integer> selected;

    private static final int itemLayoutId = R.layout.task_image_list_item;

    public TaskImageListAdapter(ArrayList<CompressedBitmap> thumbnails) {
        this.thumbnails = thumbnails;
        this.selected = new TreeSet<>();
    }

    public ArrayList<Integer> getSelectedIds() {
        return new ArrayList<>(selected);
    }

    public void animateDeletions() {
        Transition transition = makeDeleteTransition();
        attachScrollDisableListener(transition);
        TransitionManager.beginDelayedTransition(recyclerView, transition);
        notifyItemRangeChanged(0, thumbnails.size());
        notifyDataSetChanged();
    }

    private Transition makeDeleteTransition() {
        Fade at = new Fade();
        at.setDuration(500);
        attachScrollDisableListener(at);
        return at;
    }

    private void attachScrollDisableListener(Transition transition) {
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                if (manager instanceof ScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(false);
                }
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                if (manager instanceof ScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(true);
                }
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
                if (manager instanceof ScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(true);
                }
            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {

            }
        });
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
        Log.i("THING BEING ADDED", thumbnails.get(position) + "");
        Log.i("LIST OF THINGS", "" + thumbnails);
        holder.setImage(thumbnails.get(position).getBitmap());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(position);
            }
        });
        holder.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buttonLongClicked(position);
                return true;
            }
        });
    }

    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        Log.i("Recycling", holder.toString());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    //Returns true if empty
    public void toggleSelected(int position) {
        ImageViewHolder holder = (ImageViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

        if (selected.contains(position)) {
            selected.remove(position);
            if (holder != null) {
                holder.setSelectedTint(false);
            }
        } else {
            selected.add(position);
            if (holder != null) {
                holder.setSelectedTint(true);
            }
        }
    }

    public int numberSelected() {
        return selected.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ImageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setSelectedTint(false);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ImageViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (selected.contains(holder.getAdapterPosition())) {
            holder.setSelectedTint(true);
        }
    }

    public void deselectAll() {
        for (int i : selected) {
            ImageViewHolder holder = (ImageViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                holder.setSelectedTint(false);
            } else {
                Log.i("IS NULL", "" + i);
            }

        }

        selected.clear();
    }

    public abstract void buttonClicked(int position);
    public abstract void buttonLongClicked(int position);

}
