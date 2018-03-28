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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class CheckListEditorView extends CheckListView {
    /**
     * Default View constructor.
     */
    public CheckListEditorView(Context context) {
        super(context);
    }

    /**
     * Default View constructor.
     */
    public CheckListEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Default View constructor.
     */
    public CheckListEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected CheckListItemView makeItemView(CheckList.CheckListItem item) {
        CheckListItemView itemView = new CheckListItemView(getContext());
        itemView.setItem(item);
        return itemView;
    }

    public class CheckListItemView extends CheckListView.CheckListItemView {
        private CheckBox toggleBox;
        private EditText descriptionField;
        private ImageButton deleteButton;

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

        @Override
        protected void createViews() {
            inflate(getContext(), R.layout.view_checkitem_edit, this);
            this.toggleBox = findViewById(R.id.checkitemEditToggle);
            this.descriptionField = findViewById(R.id.checkitemEditDescription);
            this.deleteButton = findViewById(R.id.checkitemEditButtonDelete);
        }

        @Override
        public void setItem(final CheckList.CheckListItem item) {
            this.descriptionField.setText(item.getDescription());
            this.toggleBox.setEnabled(item.getStatus());

            this.deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCheckList().removeItem(item);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
