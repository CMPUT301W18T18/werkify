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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Bottom sheet to use for a task requester viewing a task
 * of their own that has been bidded on. Has no contents,
 * but will be bound to open a view of the current BidList on click.
 */
public class ViewTaskBiddedBottomSheet extends ViewTaskBottomSheet {
    public ViewTaskBiddedBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTaskBiddedBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTaskBiddedBottomSheet(Context context) {
        super(context);
    }

    @Override
    public ViewTaskBottomSheet initializeWithTask(Task task) {
        Bid lowestBid = task.getLowestBid();

        setDetailString(String.format(Locale.US, "%d bids so far", task.getBidList().size()));
        if (lowestBid != null) {
            setRightStatusString(String.format(Locale.US, "$%.2f", lowestBid.getValue()));
        }

        return super.initializeWithTask(task);
    }

    @Override
    protected String getStatusString() {
        return "Bidded";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusBidded;
    }

    @Override
    protected View getContentLayout(ViewGroup root) {
        return null;
    }
}
