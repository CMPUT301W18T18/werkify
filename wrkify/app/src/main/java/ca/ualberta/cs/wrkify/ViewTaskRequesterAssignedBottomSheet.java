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

import java.util.Locale;

/**
 * Bottom sheet to use for a task requester viewing a task they
 * have assigned to a provider. Contains controls to deassign or close the task.
 */
public class ViewTaskRequesterAssignedBottomSheet extends ViewTaskBottomSheet {
    public ViewTaskRequesterAssignedBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTaskRequesterAssignedBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewTaskRequesterAssignedBottomSheet(Context context) {
        super(context);
    }

    @Override
    public ViewTaskBottomSheet initializeWithTask(Task task) {
        User assignee = task.getProvider();
        if (assignee != null) {
            setDetailString(String.format(Locale.US,"assigned to %s", assignee.getUsername()));
        }
        setRightStatusString(String.format(Locale.US, "$%.2f", task.getPrice()));

        return super.initializeWithTask(task);
    }

    @Override
    protected String getStatusString() {
        return "Assigned";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusAssigned;
    }

    @Override
    protected View getContentLayout(ViewGroup root) {
        return inflate(getContext(), R.layout.activity_view_task_bottom_sheet_assigned, null);
    }
}
