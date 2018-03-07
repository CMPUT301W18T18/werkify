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
import android.widget.TextView;

/**
 * Bottom sheet to use for bidded tasks.
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
    protected String getStatusString() {
        return "Bidded";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusBidded;
    }

    @Override
    protected View getContentLayout() {
        TextView view = new TextView(getContext());
        view.setText("different test");
        return view;
    }
}
