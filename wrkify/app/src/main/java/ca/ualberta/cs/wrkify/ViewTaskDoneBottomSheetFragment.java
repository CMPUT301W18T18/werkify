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

import java.util.Locale;

/**
 * Bottom sheet to use for a task that is completed.
 * Has no content.
 */
public class ViewTaskDoneBottomSheetFragment extends ViewTaskBottomSheetFragment {
    @Override
    protected String getStatusString() {
        return "Done";
    }

    @Override
    protected int getBackgroundColor() {
        return R.color.colorStatusDone;
    }

    @Override
    protected void initializeWithTask(ViewGroup container, Task task) {
        User assignee = task.getProvider();
        if (assignee != null) {
            setDetailString(container,
                    String.format(Locale.US, "Completed by %s", assignee.getUsername()));
        }
    }

    @Override
    protected View getContentLayout(ViewGroup root) {
        return null;
    }
}