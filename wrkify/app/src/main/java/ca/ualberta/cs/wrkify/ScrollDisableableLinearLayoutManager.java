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

/**
 * Normal LinearLayoutManager but can toggle scrolling on and off
 *
 * @see LinearLayoutManager
 * @see ScrollDisableable
 */
public class ScrollDisableableLinearLayoutManager extends LinearLayoutManager implements ScrollDisableable {
    private boolean scrollEnabled = true;

    /**
     * Default constructor for LinearLayoutManager
     * @param context
     * @see LinearLayoutManager
     */
    public ScrollDisableableLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * Default constructor for LinearLayoutManager
     * @param context
     * @param orientation
     * @param reverseLayout
     * @see LinearLayoutManager
     */
    public ScrollDisableableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * Default constructor for LinearLayoutManager
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @see LinearLayoutManager
     */
    public ScrollDisableableLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * If scrolling is enabled, call super's implementation
     * @param position to be scrolled to
     */
    @Override
    public void scrollToPosition(int position) {
        if (!scrollEnabled) {
            return;
        }
        super.scrollToPosition(position);
    }

    /**
     * If scrolling is enabled, call super's implementation
     * @param position
     * @param offset
     */
    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        if (!scrollEnabled) {
            return;
        }
        super.scrollToPositionWithOffset(position, offset);
    }

    /**
     * If scrolling is enabled, call super's implementation
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (!scrollEnabled) {
            return 0;
        }
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * If scrolling is enabled, call super's implementation
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (!scrollEnabled) {
            return 0;
        }
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /**
     * @param enabled True if scrolling is allowed, false otherwise
     */
    public void setScrollEnabled(boolean enabled) {
        this.scrollEnabled = enabled;
    }

    /**
     * @return If scrolling is enabled
     */
    public boolean getScrollEnabled() {
        return scrollEnabled;
    }
}
