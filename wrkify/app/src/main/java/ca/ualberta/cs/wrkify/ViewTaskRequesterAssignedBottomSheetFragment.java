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


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import static android.view.View.inflate;

/**
 * Bottom sheet to use for a task requester viewing a task they
 * have assigned to a provider. Contains controls to deassign or close the task.
 */
public class ViewTaskRequesterAssignedBottomSheetFragment extends ViewTaskBottomSheetFragment {

    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        User assignee = task.getProvider();
        if (assignee != null) {
            setDetailString(container,
                    String.format(Locale.US,"to %s", assignee.getUsername()));
        }
        setRightStatusString(container,
                String.format(Locale.US, "$%.2f", task.getPrice()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Button closeTaskButton = view.findViewById(R.id.taskViewBottomSheetButtonMarkDone);
        Button deassignButton = view.findViewById(R.id.taskViewBottomSheetButtonDeassign);

        closeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialogFragment dialog = ConfirmationDialogFragment.makeDialog(
                        "Mark this task as done?", "Cancel", "Mark done",
                        new ConfirmationDialogFragment.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                // TODO not modifying tasks yet
                            }
                        });
                dialog.show(getFragmentManager(), null);
            }
        });
        deassignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bodyText = String.format(Locale.US, "Deassign this task?");
                ConfirmationDialogFragment dialog = ConfirmationDialogFragment.makeDialog(
                        bodyText, "Cancel", "Deassign",
                        new ConfirmationDialogFragment.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                // TODO not modifying tasks yet
                            }
                        }
                );
                dialog.show(getFragmentManager(), null);
            }
        });

        return view;
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
        return inflate(getActivity(), R.layout.activity_view_task_bottom_sheet_assigned, null);
    }
}
