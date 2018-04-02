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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Abstract class to display a CheckList in a LinearLayout.
 * This class contains the shared functionality for a View
 * displaying a CheckList.
 *
 * @see CheckList
 */
abstract class CheckListView extends LinearLayout {
    private CheckList checkList;

    private OnDataSetChangedListener onDataSetChangedListener;

    /** Default View constructor. */
    public CheckListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Default View constructor. */
    public CheckListView(Context context) {
        super(context);
    }

    /** Default View constructor. */
    public CheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Clears the view, and binds and displays the given CheckList.
     * This will also trigger the view's attached OnDataSetChangedListener, if it has one.
     * @param checkList CheckList to bind
     */
    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;

        // Reset the view
        this.removeAllViews();
        this.setOrientation(VERTICAL);

        if (checkList != null) {
            // Add new item views
            for (CheckList.CheckListItem item: checkList.getItems()) {
                this.addView(this.makeItemView(item));
            }
        }

        // Trigger listener
        if (onDataSetChangedListener != null) {
            onDataSetChangedListener.onDataSetChanged(checkList);
        }
    }

    /**
     * Gets the bound CheckList of the view.
     * @return CheckList CheckList bound to the view
     */
    public CheckList getCheckList() {
        return checkList;
    }

    /**
     * Refreshes the view to reflect the current state of the bound CheckList.
     * This will trigger the view's attached OnDataSetChangedListener, if it has one.
     */
    public void notifyDataSetChanged() {
        setCheckList(this.checkList);
    }

    /**
     * Attaches a listener that will be triggered when the view is refreshed.
     * (It will also be triggered when a new CheckList is bound to the view.)
     * @param onDataSetChangedListener listener to be triggered
     */
    public void setOnDataSetChangedListener(OnDataSetChangedListener onDataSetChangedListener) {
        this.onDataSetChangedListener = onDataSetChangedListener;
    }

    protected abstract CheckListItemView makeItemView(CheckList.CheckListItem item);

    /**
     * Listener that is triggered when the CheckListView refreshes.
     */
    public interface OnDataSetChangedListener {
        /**
         * Called when the CheckListView is refreshed.
         * @param data CheckList displayed in the view after refreshing. May be null if the view was cleared.
         */
        void onDataSetChanged(@Nullable CheckList data);
    }

    /**
     * Abstract class for a view representing a single CheckListItem.
     */
    public static abstract class CheckListItemView extends LinearLayout {
        /** Default View constructor. */
        public CheckListItemView(Context context) {
            super(context);
            createViews();
        }

        /** Default View constructor. */
        public CheckListItemView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            createViews();
        }

        /** Default View constructor. */
        public CheckListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            createViews();
        }

        /**
         * Templatized method to initialize the CheckListItemView.
         * This will be called when the CheckListView refreshes, with the item
         * that this CheckListItemView should now represent. The CheckListItemView
         * should set its state to display this item.
         * @param item new item to display in the view
         */
        public abstract void setItem(CheckList.CheckListItem item);

        /**
         * Templatized method to create the CheckListItemView.
         * This is called on creation of the CheckListItemView. It should create
         * and attach the contents of the view. (Any content dependent on the
         * CheckListItem being displayed should not be initialized here, but
         * rather should be set in setItem().)
         */
        protected abstract void createViews();
    }
}
