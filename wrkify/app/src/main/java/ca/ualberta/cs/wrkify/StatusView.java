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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * StatusView provides a material design chip interface
 * for displaying a Task Status
 *
 * @author Peter Elliott
 * @see TaskStatus
 */

public class StatusView extends AppCompatTextView {

    /**
     * create a StatusView from a context
     * this is basically the default constructor.
     * @param context the android context
     */
    public StatusView(Context context) {
        super(context);
    }

    /**
     * create a StatusView from a context and an
     * AttributeSet
     * @param context the android context
     * @param attrs the AttributeSet
     */
    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * create a StatusView from a context, an
     * AttributeSet, and a style
     * @param context the android context
     * @param attrs the AttributeSet
     * @param defStyleAttr the style
     */
    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * setStatus sets the background color and the text of the chip
     *
     * @param status the TaskStatus to display
     * @param lowBid the lowest bid (used when bidded)
     */
    public void setStatus(TaskStatus status, Price lowBid) {
        if (status == TaskStatus.REQUESTED) {
            this.setText("Requested");
            this.setBackgroundResource(R.drawable.requested_chip);
        } else if (status == TaskStatus.BIDDED) {
            this.setText(lowBid.toString());
            this.setBackgroundResource(R.drawable.bidded_chip);
        } else if (status == TaskStatus.ASSIGNED) {
            this.setText("Assigned");
            this.setBackgroundResource(R.drawable.assigned_chip);
        } else if (status == TaskStatus.DONE) {
            this.setText("Done");
            this.setBackgroundResource(R.drawable.done_chip);
        }
    }
}
