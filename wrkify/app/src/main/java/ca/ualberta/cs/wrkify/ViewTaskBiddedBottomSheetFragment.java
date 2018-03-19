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
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * Bottom sheet to use for a task requester viewing a task
 * of their own that has been bidded on. Has no contents,
 * but will be bound to open a view of the current BidList on click.
 */
public class ViewTaskBiddedBottomSheetFragment extends ViewTaskBottomSheetFragment {
    private static final int REQUEST_VIEW_BIDS = 13;

    @Override
    protected void initializeWithTask(ViewGroup container, final Task task) {
        Bid lowestBid = task.getBidList().get(0);

        setDetailString(container,
                String.format(Locale.US, "%d bids so far", task.getBidList().size()));
        if (lowestBid != null) {
            setRightStatusString(container,
                    String.format(Locale.US, "$%.2f", lowestBid.getValue()));
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewBidsActivity.class);
                intent.putExtra(ViewBidsActivity.EXTRA_VIEWBIDS_TASK, task);
                startActivityForResult(intent, REQUEST_VIEW_BIDS);
            }
        });
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
