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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

abstract class CheckListView extends LinearLayout {
    private CheckList checkList;

    private OnDataSetChangedListener onDataSetChangedListener;

    public CheckListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CheckListView(Context context) {
        super(context);
    }

    public CheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the view to be bound to the given CheckList.
     * @param checkList CheckList to display/edit
     */
    public void setCheckList(CheckList checkList) {
        Log.i("-->", "checkList: " + checkList);
        this.checkList = checkList;

        // Reset the view
        this.removeAllViews();
        this.setOrientation(VERTICAL);

        if (checkList == null) { return; }

        // Add new item views
        for (CheckList.CheckListItem item: checkList.getItems()) {
            this.addView(this.makeItemView(item));
        }
    }

    /**
     * Gets the bound CheckList of the view.
     * @return CheckList
     */
    public CheckList getCheckList() {
        return checkList;
    }

    /**
     * Refreshes the view to reflect the current state of the CheckList.
     */
    public void notifyDataSetChanged() {
        if (checkList != null) {
            setCheckList(checkList);
        }

        if (onDataSetChangedListener != null) {
            onDataSetChangedListener.onDataSetChanged(checkList);
        }
    }

    public void setOnDataSetChangedListener(OnDataSetChangedListener onDataSetChangedListener) {
        this.onDataSetChangedListener = onDataSetChangedListener;
    }

    protected abstract CheckListItemView makeItemView(CheckList.CheckListItem item);

    public interface OnDataSetChangedListener {
        public void onDataSetChanged(CheckList data);
    }

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

        public abstract void setItem(CheckList.CheckListItem item);

        protected abstract void createViews();
    }
}
