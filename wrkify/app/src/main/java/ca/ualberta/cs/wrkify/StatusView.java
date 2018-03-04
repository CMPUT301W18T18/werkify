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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by peter on 03/03/18.
 */

public class StatusView extends AppCompatTextView {

    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStatus(TaskStatus status, Double lowBid) {
        if (status == TaskStatus.REQUESTED) {
            this.setText("Requested");
            this.setBackgroundResource(R.drawable.requested_chip);
        } else if (status == TaskStatus.BIDDED) {
            this.setText(String.format("$%.2f", lowBid));
            this.setBackgroundResource(R.drawable.bidded_chip);
        } else if (status == TaskStatus.ASSIGNED) {
            this.setText("Assigned");
            this.setBackgroundResource(R.drawable.assigned_chip);
        } else if (status == TaskStatus.DONE) {
            this.setText("Done");
            this.setBackgroundResource(R.drawable.done_chip);
        }
    }
}
