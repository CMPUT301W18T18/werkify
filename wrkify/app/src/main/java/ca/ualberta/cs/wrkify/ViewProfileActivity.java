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

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

/**
 * ViewProfileActivity displays the contact information of
 * a User, as well as provides the option to edit info
 *
 * @see User
 * @see EditProfileActivity
 */
public class ViewProfileActivity extends AppCompatActivity {

    public static final String USER_EXTRA = "ca.ualberta.cs.wrkify.USER_INTENT";

    private static final int REQUEST_EDIT_PROFILE = 11;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        this.initializeFromUser((User) getIntent().getSerializableExtra(USER_EXTRA));

        // Show the Edit button if the user being viewed is the session user
        FloatingActionButton editButton = findViewById(R.id.editButton);
        if (Session.getInstance(this).getUser().equals(user)) {
            editButton.setVisibility(View.VISIBLE);
        }

        // Open EditProfileActivity on edit button press
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(
                        ViewProfileActivity.this, EditProfileActivity.class);
                editIntent.putExtra(EditProfileActivity.EXTRA_TARGET_USER, user);
                startActivityForResult(editIntent, REQUEST_EDIT_PROFILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            this.initializeFromUser((User) data.getSerializableExtra(
                    EditProfileActivity.EXTRA_RETURNED_USER));
        }
    }

    private void initializeFromUser(User user) {
        this.user = user;
        refreshUser();

        TextView username = findViewById(R.id.UserName);
        TextView email = findViewById(R.id.email);
        TextView phonenumber = findViewById(R.id.PhoneNumber);

        username.setText(user.getUsername());
        email.setText(user.getEmail());
        //TODO phone number formating
        phonenumber.setText(user.getPhoneNumber());

        //TODO FAB hidden if user is not us
    }

    /**
     * Re-downloads the user being viewed from the remote server.
     * This ensures the profile being viewed is up to date.
     */
    private void refreshUser() {
        try {
            ((CachingClient) WrkifyClient.getInstance()).discardCached(this.user.getId());
            this.user = WrkifyClient.getInstance().download(this.user.getId(), this.user.getClass());
        } catch (IOException e) {
            // TODO You are offline.
        }
    }
}
