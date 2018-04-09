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


import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.Locale;

/**
 * Bottom sheet to use for a task that is completed.
 * Has no content.
 */
public class ViewTaskDoneBottomSheetFragment extends ViewTaskBottomSheetFragment {
    /**
     * gets the status string
     * @return always "Done"
     */
    @Override
    protected String getStatusString() {
        return "Done";
    }

    /**
     * gets the color of the bottomsheet
     * @return always colorStatusDone
     */
    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusDone;
    }

    /**
     * initialize the bottomsheet from a task
     * @param container ViewGroup containing the header and content frame
     * @param task task object to initialize from
     */
    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        User assignee;
        try {
            assignee = task.getRemoteProvider(WrkifyClient.getInstance());
        } catch (IOException e) {
            // TODO handle this correctly
            return;
        }
        if (assignee != null) {
            setDetailString(container,
                    String.format(Locale.US, "Completed by %s", assignee.getUsername()));
        }
    }

    /**
     * gets the content layout
     * @param root
     * @return always null
     */
    @Override
    protected View getContentLayout(ViewGroup root) {
        return null;
    }
}
