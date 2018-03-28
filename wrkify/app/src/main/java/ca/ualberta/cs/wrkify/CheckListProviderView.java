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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * View for a task checklist of ViewTaskActivity.
 * Displays the items as a static vertical list. This CheckListView
 * makes no modifications to the CheckList internally, though it
 * provides a listener interface to its checkboxes to allow an invoking
 * class to make them actually toggle the task statuses.
 */
public class CheckListProviderView extends CheckListView {
    private boolean editingEnabled;

    private OnItemToggledListener onItemToggledListener;

    /** Default View constructor. */
    public CheckListProviderView(Context context) {
        super(context);
    }

    /** Default View constructor. */
    public CheckListProviderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /** Default View constructor. */
    public CheckListProviderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Makes the checklist editable/uneditable (allow toggling its items).
     * This will automatically refresh the View.
     * @param editingEnabled whether to allow toggling items
     */
    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
        this.notifyDataSetChanged();
    }

    /**
     * Sets a listener that will be triggered when an item's checkbox is
     * toggled. (This will only ever occur if the checklist is editable.)
     * Note that toggling the checkboxes by default has no entity-level
     * effect (ie. actually toggling the status of the underlying CheckListItem
     * should be implemented by the invoker and attached to this listener).
     * @param onItemToggledListener Listener to attach
     */
    public void setOnItemToggledListener(OnItemToggledListener onItemToggledListener) {
        this.onItemToggledListener = onItemToggledListener;
    }

    @Override
    protected CheckListItemView makeItemView(CheckList.CheckListItem item) {
        CheckListItemView itemView = new CheckListItemView(getContext());
        itemView.setEditingEnabled(this.editingEnabled);
        itemView.setItem(item);
        return itemView;
    }

    /**
     * Listener for item checkbox toggle events.
     */
    public interface OnItemToggledListener {
        /**
         * Called when an item's checkbox is toggled.
         * @param item item that was toggled
         */
        void onItemToggled(@NonNull CheckList.CheckListItem item);
    }

    /**
     * An item in the provider checklist view. Consists of a possibly-toggleable
     * checkbox and a description.
     */
    public class CheckListItemView extends CheckListView.CheckListItemView {
        private boolean editingEnabled;

        private TextView descriptionField;
        private CheckBox toggleBox;

        /**
         * Default View constructor.
         */
        public CheckListItemView(Context context) {
            super(context);
        }

        /**
         * Default View constructor.
         */
        public CheckListItemView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        /**
         * Default View constructor.
         */
        public CheckListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        /**
         * Enables or disables the checkbox for this item.
         * @param editingEnabled whether to enable the checkbox
         */
        public void setEditingEnabled(boolean editingEnabled) {
            this.editingEnabled = editingEnabled;
        }

        @Override
        public void setItem(final CheckList.CheckListItem item) {
            Log.i("-->", "" + item.getStatus());
            this.descriptionField.setText(item.getDescription());
            this.toggleBox.setChecked(item.getStatus());
            this.toggleBox.setClickable(false);
            this.toggleBox.setAlpha(editingEnabled? 1.0f : 0.3f);

            if (editingEnabled) {
                this.toggleBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemToggledListener != null) {
                            onItemToggledListener.onItemToggled(item);
                        }

                        toggleBox.setChecked(item.getStatus());
                    }
                });
            }
        }

        @Override
        protected void createViews() {
            inflate(getContext(), R.layout.view_checkitem, this);
            this.descriptionField = findViewById(R.id.checkitemDescription);
            this.toggleBox = findViewById(R.id.checkitemToggle);
        }
    }
}
