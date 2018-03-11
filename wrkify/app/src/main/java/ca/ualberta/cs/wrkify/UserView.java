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
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Displays a username.
 */
public class UserView extends ConstraintLayout {
    private TextView userNameView;

    /**
     * Initializes a UserView. (standard View constructor)
     */
    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.userNameView = new TextView(context);
        this.addView(userNameView);
    }

    /**
     * Initializes a UserView. (standard View constructor)
     */
    public UserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.userNameView = new TextView(context);
        this.addView(userNameView);
    }

    /**
     * Initializes a UserView. (standard View constructor)
     */
    public UserView(Context context) {
        super(context);

        this.userNameView = new TextView(context);
        this.addView(userNameView);
    }

    /**
     * Sets the name displayed in the UserView.
     * @param userName name to display
     */
    public void setUserName(String userName) {
        this.userNameView.setText(userName);
    }
}
