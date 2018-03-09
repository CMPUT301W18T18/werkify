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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ViewProfileActivity displays the contact information of
 * a User, as well as provides the option to edit info
 *
 * @see User
 * @see EditProfileActivity
 */
public class ViewProfileActivity extends AppCompatActivity {

    public static final String USER_EXTRA = "ca.ualberta.cs.wrkify.USER_INTENT";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        TextView username = (TextView) findViewById(R.id.UserName);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phonenumber = (TextView) findViewById(R.id.PhoneNumber);

        username.setText(user.getUsername());
        email.setText("email: " + user.getEmail());
        //TODO phone number formating
        phonenumber.setText("phone: " + user.getPhoneNumber());

        //TODO FAB hidden if user is not us
    }
}
