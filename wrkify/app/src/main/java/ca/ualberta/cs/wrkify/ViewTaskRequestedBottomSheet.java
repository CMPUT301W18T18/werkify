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

/**
 * Bottom sheet to use for a task requester viewing their own task
 * that has no bids so far.
 */
class ViewTaskRequestedBottomSheet extends ViewTaskBottomSheet {
    public ViewTaskRequestedBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTaskRequestedBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTaskRequestedBottomSheet(Context context) {
        super(context);
    }

    @Override
    protected String getStatusString() {
        return "Requested";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusRequested;
    }

    @Override
    public ViewTaskBottomSheet initializeWithTask(Task task) {
        setDetailString("No bids yet");

        return super.initializeWithTask(task);
    }

    @Override
    protected View getContentLayout(ViewGroup root) {
        return null;
    }
}
