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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Base class for bottom sheets of the ViewTaskActivity.
 * Defines the basic behaviour of such a bottom sheet:
 * it has a status line and a detail line, and it expands
 * when clicked on.
 */
public abstract class ViewTaskBottomSheetFragment extends Fragment {
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
     * Creates the fragment. The base layout is inflated from
     * fragment_view_task_bottom_sheet_base, and contains two
     * TextViews (taskViewBottomSheetStatus, taskViewBottomSheetDetail).
     * The default value for Status comes from getStatusString().
     * Detail is initially empty.
     * The background color of the sheet is set by the class's
     * getBackgroundColor(). The base layout also contains a
     * hidden container in which is placed the class's getContentLayout()
     * which will be revealed when the bottom sheet is clicked.
     * @param inflater LayoutInflater to use for generating View (as in superclass)
     * @param container ViewGroup to use for generating View (as in superclass)
     * @param savedInstanceState not used
     * @return View comprising the bottom sheet
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_task_bottom_sheet_base, container, false);
        view.setBackgroundColor(this.getBackgroundColor());

        // Set status text
        TextView statusView = view.findViewById(R.id.taskViewBottomSheetStatus);
        statusView.setText(this.getStatusString());

        // Insert content layout
        FrameLayout frame = view.findViewById(R.id.taskViewBottomSheetContent);
        frame.addView(this.getContentLayout());

        return view;
    }
}
