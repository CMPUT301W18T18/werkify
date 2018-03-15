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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ScrollDisableableLinearLayoutManager extends LinearLayoutManager implements ScrollDisableable{


    private boolean scrollEnabled = true;

    public ScrollDisableableLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollDisableableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollDisableableLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean enabled){
        this.scrollEnabled = enabled;
    }

    public boolean getScrollEnabled(){
        return scrollEnabled;
    }


    @Override
    public void scrollToPosition(int position) {
        if (!scrollEnabled) {
            return;
        }
        super.scrollToPosition(position);
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        if (!scrollEnabled) {
            return;
        }
        super.scrollToPositionWithOffset(position, offset);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (!scrollEnabled) {
            return 0;
        }
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (!scrollEnabled) {
            return 0;
        }
        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
