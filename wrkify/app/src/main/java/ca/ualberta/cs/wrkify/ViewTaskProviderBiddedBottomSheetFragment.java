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


import android.view.ViewGroup;


/**
 * ViewTaskProviderBidderBottomSheetFragment is the bottomsheet that
 * a user will see if they are the provider or bidder.
 */
public class ViewTaskProviderBiddedBottomSheetFragment extends ViewTaskOpenBottomSheetFragment {
    /**
     * gets the status string
     * @return always "Bidded"
     */
    @Override
    protected String getStatusString() {
        return "Bidded";
    }

    /**
     * gets the bottom sheet color
     * @return always colorStatusBidded
     */
    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusBidded;
    }

    /**
     * reinitializes the view with the provided task
     * @param container ViewGroup containing the header and content frame
     * @param task task object to initialize from.
     */
    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        super.initializeWithTask(container, task);
        User user = Session.getInstance(getContext()).getUser();
        setRightDetailString(container, String.format("Your bid: %s", task.getBidForUser(user).getValue()));
    }
}
