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
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Base class for bottom sheets of the ViewTaskActivity.
 * Defines the basic behaviour of such a bottom sheet:
 * it has a status line and a detail line, and it expands
 * when clicked on.
 */
public abstract class ViewTaskBottomSheet extends ConstraintLayout {
    public ViewTaskBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.populateView();
    }

    public ViewTaskBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.populateView();
    }

    public ViewTaskBottomSheet(Context context) {
        super(context);
        this.populateView();
    }

    /**
     * The default status message to be displayed on the sheet.
     * @return default status message
     */
    abstract protected String getStatusString();

    /**
     * Determines the background color of this bottom sheet.
     * @return int corresponding to background color
     */
    abstract protected int getBackgroundColor();

    /**
     * Returns the layout to use for the expanded content of the
     * bottom sheet when it is opened. (This View is created along
     * with the bottom sheet, but hidden until revealed.)
     * @return View to be displayed when the sheet is opened
     */
    abstract protected View getContentLayout();

    /**
     * Expands the bottom sheet, if it isn't already expanded.
     */
    public void expand(View view) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(this);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     * Creates the view. The base layout is inflated from
     * activity_view_task_bottom_sheet, and contains two
     * TextViews (taskViewBottomSheetStatus, taskViewBottomSheetDetail).
     * The default value for Status comes from getStatusString().
     * Detail is initially empty.
     * The background color of the sheet is set by the class's
     * getBackgroundColor(). The base layout also contains a
     * hidden container in which is placed the class's getContentLayout()
     * which will be revealed when the bottom sheet is clicked.
     */
    public void populateView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_view_task_bottom_sheet, this, false);

        // Set background
        this.setBackground(new ColorDrawable(getResources().getColor(this.getBackgroundColor())));
        this.setElevation(10);

        // Set status text
        TextView statusView = view.findViewById(R.id.taskViewBottomSheetStatus);
        statusView.setText(this.getStatusString());

        // Set content view
        FrameLayout frame = view.findViewById(R.id.taskViewBottomSheetContent);
        frame.addView(this.getContentLayout());

        // Set click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand(view);
            }
        });
        this.addView(view);
    }

    /**
     * Set the peek height of the bottom sheet to only show the header.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(this);
        behavior.setPeekHeight(findViewById(R.id.taskViewBottomSheetHeader).getHeight());
    }

    /**
     * Initialize the view's behavior relative to its CoordinatorLayout on attach.
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) this.getLayoutParams();
        params.setBehavior(new BottomSheetBehavior(){
            @Override
            public float getScrimOpacity(CoordinatorLayout parent, View child) {
                Log.i("-->", "getScrimOpacity");
                Log.i("-->", String.valueOf(getState()));
                if (getState() == STATE_COLLAPSED) {
                    Log.i("-->", "collapsed");
                    return 0.0f;
                } else {
                    Log.i("-->", "expanded");
                    return 0.5f;
                }
            }
        });
        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
        params.insetEdge = LayoutParams.BOTTOM;

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(this);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    setTranslationZ(0);
                }
                else {
                    setTranslationZ(12);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void setDetailString(String detailString) {
        TextView detailView = findViewById(R.id.taskViewBottomSheetDetail);
        detailView.setText(detailString);
    }
}
