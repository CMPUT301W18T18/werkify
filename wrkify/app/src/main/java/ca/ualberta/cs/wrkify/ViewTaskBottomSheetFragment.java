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

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.SidePropagation;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Base class for bottom sheets of the ViewTaskActivity.
 * A ViewTaskBottomSheetFragment creates a View which is effectively a simplified
 * bottom sheet. It is intended to be anchored at the bottom of the screen, and
 * it expands when clicked on. Subclasses can define the content that appears when
 * the sheet is expanded (or define a lack of such content). Subclasses can also
 * define the text that appears on the collapsed sheet and the sheet's color.
 */
public abstract class ViewTaskBottomSheetFragment extends Fragment {
    public static String ARGUMENT_TARGET_TASK = "ca.ualberta.cs.wrkify.ARGUMENT_TARGET_TASK";

    private Boolean expandable = true;
    private Boolean expanded = false;

    /**
     * Requisite null constructor
     */
    public ViewTaskBottomSheetFragment() { }

    /**
     * Initializes the bottom sheet contents from a task object. This will set the
     * header fields appropriately, and possibly initialize the sheet contents if
     * they require initializing. This is called from the default onCreateView.
     * @param container ViewGroup containing the header and content frame
     * @param task task object to initialize from
     */
    protected abstract void initializeWithTask(ViewGroup container, Task task);

    /**
     * Determines the status message to be displayed on the sheet. (eg. "Open")
     * This is called from the default onCreateView and displayed in the upper left
     * of the collapsed sheet.
     * @return default status message
     */
    abstract protected String getStatusString();

    /**
     * Determines the background color of this bottom sheet.
     * @return resource ID corresponding to background color
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

        View view = this.getView();
        if (view == null) throw new IllegalStateException();

        // Show the content
        TransitionManager.beginDelayedTransition((ViewGroup) getView(), new Slide(Gravity.BOTTOM));
        getView().findViewById(R.id.taskViewBottomSheetContent).setVisibility(View.VISIBLE);

        // Elevate the sheet
        // TODO This doesn't actually work
        getView().setTranslationZ(8);

        this.expanded = true;
    }

    /**
     * Collapses the bottom sheet, if it isn't already collapsed.
     */
    public void collapse() {
        View view = this.getView();
        if (view == null) throw new IllegalStateException();

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setPropagation(new SidePropagation());
        TransitionManager.beginDelayedTransition((ViewGroup) getView(), slide);
        view.findViewById(R.id.taskViewBottomSheetContent).setVisibility(View.GONE);
        view.setTranslationZ(0);
        this.expanded = false;
    }

    /**
     * Creates the view. The base layout is inflated from
     * activity_view_task_bottom_sheet.
     * This is a templatized method which is partially defined by subclasses:
     * - getBackgroundColor() determines the color of the bottom sheet
     * - getStatusString() determines the primary status text to display on the collapsed sheet
     * - getContentLayout() determines the View that will be displayed when the sheet is expanded
     * - initializeWithTask() is called after main initialization completes, and is intended for
     *         the subclass to fill in its detailed status fields and any expanded-view content
     *         that is dependent on the task being manipulated.
     * Subclasses can additionally override onCreateView to effect more complex specialization.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_task_bottom_sheet, container, false);

        // Set background
        int color = getResources().getColor(this.getBackgroundColor());
        view.findViewById(R.id.taskViewBottomSheetHeader).setBackground(new ColorDrawable(color));
        view.setBackground(new ColorDrawable(color));
        view.setElevation(0);

        // Set status text
        TextView statusView = view.findViewById(R.id.taskViewBottomSheetStatus);
        statusView.setText(this.getStatusString());

        // Set content view
        View content = this.getContentLayout((ViewGroup) view);
        FrameLayout frame = view.findViewById(R.id.taskViewBottomSheetContent);

        if (content != null) {
            frame.addView(content);
            this.expandable = true;
        }
        else {
            this.expandable = false;
        }

        // Hide/show content frame
        if (this.expanded) {
            frame.setVisibility(View.VISIBLE);
        } else {
            frame.setVisibility(View.GONE);
        }

        // Set click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });

        // Fill in detail fields
        this.initializeWithTask((ViewGroup) view, (Task) getArguments().getSerializable(ARGUMENT_TARGET_TASK));

        return view;
    }

    /**
     * Sets the lower-left detail string on the collapsed sheet
     * @param container ViewGroup corresponding to the sheet
     * @param detailString text to set
     */
    protected void setDetailString(ViewGroup container, String detailString) {
        TextView detailView = container.findViewById(R.id.taskViewBottomSheetDetail);
        detailView.setText(detailString);
    }

    /**
     * Sets the upper-right detail string on the collapsed sheet
     * @param container ViewGroup corresponding to the sheet
     * @param rightStatusString text to set
     */
    protected void setRightStatusString(ViewGroup container, String rightStatusString) {
        TextView rightStatusView = container.findViewById(R.id.taskViewBottomSheetRightStatus);
        rightStatusView.setText(rightStatusString);
    }

    /**
     * Sets the lower-right detail string on the collapsed sheet
     * @param container ViewGroup corresponding to the sheet
     * @param rightDetailString text to set
     */
    public void setRightDetailString(ViewGroup container, String rightDetailString) {
        TextView rightDetailView = container.findViewById(R.id.taskViewBottomSheetRightDetail);
        rightDetailView.setText(rightDetailString);
    }
}
