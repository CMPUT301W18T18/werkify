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


import android.view.View;
import android.view.ViewGroup;

/**
 * Bottom sheet to use for a task requester viewing their own task
 * that has no bids so far.
 */
public class ViewTaskRequestedBottomSheetFragment extends ViewTaskBottomSheetFragment {
    /**
     * gets the status string
     * @return always "Requested"
     */
    @Override
    protected String getStatusString() {
        return "Requested";
    }

    /**
     * gets the bottom sheet color
     * @return always colorStatusRequested.
     */
    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusRequested;
    }

    /**
     * reinitialize the bottomsheet with a new task
     * @param container ViewGroup containing the header and content frame
     * @param task task object to initialize from
     */
    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        setDetailString(container, "No bids yet");
    }

    /**
     * get the content layout
     * @param root the root ViewGroup
     * @return always null;
     */
    @Override
    protected View getContentLayout(ViewGroup root) {
        return null;
    }
}
