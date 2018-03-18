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

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * For use with ViewBidsActivity
 * Creates and manages Bid Views for the RecyclerView in the activity
 * Defines Button functionality within the Bid Views
 * Should be used with a scrollable LayoutManager which implements ScrollDisableable, though not
 * totally necessary
 *
 * @see ViewBidsActivity
 * @see ScrollDisableable
 */
public class BidListAdapter extends RecyclerView.Adapter<BidViewHolder> {
    private static int itemLayoutId = R.layout.bidlistitem;
    private static int item_defaultBackgroundId = R.color.bidListItem_defaultBackground;
    private static int item_selectedBackgroundId = R.color.bidListItem_selectedBackground;

    private AppCompatActivity context;
    private List<Bid> data;
    private RecyclerView recyclerView;
    private boolean isRequester;
    private Task task;

    private long animationTime = 20;
    private int currentSelectedPos = -1; //List position of the currently selected item
    private BidViewHolder currentSelected = null; //ViewHolder for the currently selected item
    private boolean selectedIsVisible = false; /*True if the currently selected item is visible, false
        if it's not visible or no item is selected*/
    private boolean deleteAnimation = false; //True if a deletion animation is playing

    /**
     * Sets up Bid data from given Task, initializes other variables
     *
     * @param context AppCompatActivity of the ViewBidsActivity
     * @param task Task being viewed currently
     * @param isRequester If the user is the requester of the given Task
     */
    public BidListAdapter(AppCompatActivity context, Task task, boolean isRequester) {
        this.context = context;
        this.task = task;
        this.data = task.getBidList();
        this.isRequester = isRequester;
    }

    /**
     * @return number of items in the list
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * @param position position of item in the list
     * @return item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates a new ViewHolder for the RecyclerView
     *
     * @param viewGroup parent of ViewHolder being made
     * @param position position of item whose view is being created
     * @return new BidViewHolder for the item in the given position
     */
    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(BidListAdapter.itemLayoutId, viewGroup, false);
        BidViewHolder holder = new BidViewHolder(v, isRequester);

        holder.collapse();

        ColorDrawable defaultBG = new ColorDrawable();
        defaultBG.setColor(ContextCompat.getColor(context, item_defaultBackgroundId));

        ColorDrawable selectedBG = new ColorDrawable();
        selectedBG.setColor(ContextCompat.getColor(context, item_selectedBackgroundId));

        holder.setDefaultBackground(defaultBG);
        holder.setSelectedBackground(selectedBG);

        return holder;
    }

    /**
     * Re-initializes an existing ViewHolder to contain the item in the given position
     * @param holder BidViewHolder to be re-used
     * @param position List position of item to be put into the view holder
     */
    @Override
    public void onBindViewHolder(final BidViewHolder holder, final int position) {
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(holder, position);
            }
        });

        holder.setData(data.get(position));
        restoreSelectionStatus(holder, position);

        holder.getReject().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectClicked(holder, position);
            }
        });

        holder.getViewProfile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfileClicked(position);
            }
        });

        holder.getAccept().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptClicked(position);
            }
        });
    }

    /**
     * Collapses the given ViewHolder when it's recycled
     * @param holder ViewHolder being recycled
     */
    @Override
    public void onViewRecycled(BidViewHolder holder) {
        holder.collapse();
        if (holder == currentSelected) {
            selectedIsVisible = false;
        }
    }

    /**
     * Saves the RecyclerView that this adapter is attached to
     * @param recyclerView adapter is attached to this
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * @return True if the viewer is the Requester of the current Task
     */
    public boolean getIsRequester() {
        return isRequester;
    }

    /**
     * Handles collapse/expansion of list item when it's clicked. If a delete animation is
     * happening when an item is clicked, the clicked item won't respond
     * @param holder ViewHolder whose list item was clicked
     * @param position position of item in the list
     */
    private void cardClicked(BidViewHolder holder, int position) {
        if (deleteAnimation) {
            return;
        }

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

    /**
     * Expands the given BidViewHolder
     * @param holder
     */
    private void expandView(BidViewHolder holder) {
        TransitionManager.beginDelayedTransition(recyclerView, makeSizeChangeTransition());
        holder.expand();
    }

    /**
     * Collapses the given BidViewHolder
     * @param holder
     */
    private void collapseView(BidViewHolder holder) {
        TransitionManager.beginDelayedTransition(recyclerView, makeSizeChangeTransition());
        holder.collapse();
    }

    /**
     * Expands first holder, collapses second. Animates both changes
     * @param toExpand
     * @param toCollapse
     */
    private void expandAndCollapseViews(BidViewHolder toExpand, BidViewHolder toCollapse) {
        TransitionManager.beginDelayedTransition(recyclerView, makeSizeChangeTransition());
        toExpand.expand();
        toCollapse.collapse();
    }

    /**
     * Called when an existing BidViewHolder is re-bound to a different list item. Expands the View
     * if it was expanded before recycling
     * @param holder View holder to be restored
     * @param position List position of the item that should be in the holder
     */
    private void restoreSelectionStatus(BidViewHolder holder, int position) {
        if (currentSelectedPos == position) {
            holder.expand();
            selectedIsVisible = true;
        }
    }

    /**
     * @return Transition used for expanding/collapsing item Views
     */
    private Transition makeSizeChangeTransition() {
        return makeChangeBoundsTransition();
    }

    /**
     * @return ChangeBounds transition, value is currently returned by makeSizeChangeTransition()
     */
    private ChangeBounds makeChangeBoundsTransition() {
        ChangeBounds cb = new ChangeBounds();
        cb.setDuration(animationTime);
        attachScrollDisableListener(cb);
        return cb;
    }

    /**
     * @param ms Length of Transitions, in milliseconds
     */
    public void setAnimationTime(long ms) {
        this.animationTime = ms;
    }

    /**
     * Attaches a listener to the given Transition that disables/enables the scrolling of the
     * LayoutManager of the RecyclerView, if that LayoutManager has implemented ScrollDisableable
     * @param transition
     */
    private void attachScrollDisableListener(Transition transition) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (recyclerView.getLayoutManager() instanceof ScrollDisableable) {
            final ScrollDisableable manager2 = (ScrollDisableable) manager;

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    manager2.setScrollEnabled(false);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    manager2.setScrollEnabled(true);

                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    manager2.setScrollEnabled(true);

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    /**
     * Called when the "View Profile" button is clicked -- starts the View Profile activity for the
     * item at the given position in the list
     * @param position List position of the item whose "View Profile" button was clicked
     */
    private void viewProfileClicked(int position) {
        Intent profileIntent = new Intent(context, ViewProfileActivity.class);
        profileIntent.putExtra(ViewProfileActivity.USER_EXTRA, data.get(position).getBidder());
        context.startActivity(profileIntent);
    }

    /**
     * Called when the "Reject" button is clicked -- animates deletion of the rejected item,
     * disabling the scrolling of the list while the animation plays, if the RecyclerView's
     * LayoutManager has implemented ScrollDisableable
     * @param holder
     * @param position
     */
    private void rejectClicked(final BidViewHolder holder, final int position) {
        AutoTransition cb = new AutoTransition();
        cb.setDuration((long) (animationTime));

        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        final boolean isScrollDisableable = (manager instanceof ScrollDisableable);

        final BidListAdapter adapter = this;

        cb.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                deleteAnimation = true;
                if (isScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(false);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                deleteAnimation = false;
                if (isScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(true);
                }
                adapter.deleteItem(holder, position);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                deleteAnimation = false;
                if (isScrollDisableable) {
                    ((ScrollDisableable) manager).setScrollEnabled(true);
                }
                adapter.deleteItem(holder, position);

            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
        TransitionManager.beginDelayedTransition(recyclerView, cb);
        holder.totalCollapse();
    }

    /**
     * Deletes the item from the list and notifies the RecyclerView by calling the correct notify
     * functions. Should only be called by rejectClicked(). Restores size of the ViewHolder which
     * was collapsed by the deletion animation
     * @param holder BidViewHolder of the item which was deleted -- holder to be restored in size
     * @param position List position of deleted item
     */
    private void deleteItem(BidViewHolder holder, int position) {
        currentSelectedPos = -1;
        currentSelected = null;
        selectedIsVisible = false;
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
        holder.restoreSize();
        notifyDataSetChanged();
    }

    /**
     * Called when the "Accept" Button is clicked. Accepts the Bid and ends the activity
     * @param position List position of accepted item
     */
    protected void acceptClicked(int position) {
        task.acceptBid(data.get(position));
        context.finish();
    }

}