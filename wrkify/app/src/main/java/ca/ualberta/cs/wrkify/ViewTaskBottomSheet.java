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
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.transition.SidePropagation;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Base class for bottom sheets of the ViewTaskActivity.
 * Defines the basic behaviour of such a bottom sheet:
 * it has a status line and a detail line, and it expands
 * when clicked on.
 */
public abstract class ViewTaskBottomSheet extends ConstraintLayout {
    private Boolean expandable = true;
    private Boolean expanded = false;

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
     * Initializes the bottom sheet contents from a task object. This will set the
     * header fields appropriately, and possibly initialize the sheet contents if
     * they require initializing.
     * @param task task object to initialize from
     * @return this
     */
    public ViewTaskBottomSheet initializeWithTask(Task task) {
        return this;
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
    abstract protected View getContentLayout(ViewGroup root);

    /**
     * Checks whether the bottom sheet is currently expanded.
     * @return whether the bottom sheet is expanded
     */
    public boolean isExpanded() {
        return this.expanded;
    }

    /**
     * Expands the bottom sheet, if it isn't already expanded.
     */
    public void expand() {
        if (!this.expandable) return;

        TransitionManager.beginDelayedTransition(this, new Slide(Gravity.BOTTOM));
        findViewById(R.id.taskViewBottomSheetContent).setVisibility(VISIBLE);
        setTranslationZ(8);
        this.expanded = true;
    }

    /**
     * Collapses the bottom sheet, if it isn't already collapsed.
     */
    public void collapse() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setPropagation(new SidePropagation());
        TransitionManager.beginDelayedTransition(this, slide);
        findViewById(R.id.taskViewBottomSheetContent).setVisibility(GONE);
        setTranslationZ(0);
        this.expanded = false;
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
        int color = getResources().getColor(this.getBackgroundColor());
        view.findViewById(R.id.taskViewBottomSheetHeader).setBackground(new ColorDrawable(color));
        this.setBackground(new ColorDrawable(color));
        this.setElevation(0);

        // Set status text
        TextView statusView = view.findViewById(R.id.taskViewBottomSheetStatus);
        statusView.setText(this.getStatusString());

        // Set content view
        View content = this.getContentLayout(this);
        FrameLayout frame = view.findViewById(R.id.taskViewBottomSheetContent);

        if (content != null) {
            frame.addView(content);
            this.expandable = true;
        }
        else {
            this.expandable = false;
        }

        // Hide content frame
        frame.setVisibility(GONE);

        // Set click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });
        this.addView(view);
    }

    public void setDetailString(String detailString) {
        TextView detailView = findViewById(R.id.taskViewBottomSheetDetail);
        detailView.setText(detailString);
    }

    public void setRightStatusString(String rightStatusString) {
        TextView rightStatusView = findViewById(R.id.taskViewBottomSheetRightStatus);
        rightStatusView.setText(rightStatusString);
    }

    public void setRightDetailString(String rightDetailString) {
        TextView rightDetailView = findViewById(R.id.taskViewBottomSheetRightDetail);
        rightDetailView.setText(rightDetailString);
    }
}
