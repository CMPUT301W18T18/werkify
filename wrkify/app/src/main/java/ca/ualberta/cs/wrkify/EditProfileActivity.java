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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {
    public static final String EXTRA_TARGET_USER = "ca.ualberta.cs.wrkify.EXTRA_TARGET_USER";
    public static final String EXTRA_RETURNED_USER = "ca.ualberta.cs.wrkify.EXTRA_RETURNED_USER";

    private User user;

    private EditText emailField;
    private EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.user = (User) getIntent().getSerializableExtra(EXTRA_TARGET_USER);

        // Populate fields
        this.emailField = findViewById(R.id.editProfileEmailField);
        this.phoneField = findViewById(R.id.editProfilePhoneField);
        TextView usernameView = findViewById(R.id.editProfileUsername);

        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        usernameView.setText(user.getUsername());

        // Set app bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setTitle("Editing profile"); }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile, menu);

        // Save button saves the user
        menu.findItem(R.id.menuItemSaveProfile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveAndFinish();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveAndFinish() {
        boolean valid = true;

        try {
            user.setEmail(emailField.getText().toString());
        } catch (IllegalArgumentException e) {
            emailField.setError("Not a valid email address");
            valid = false;
        }

        try {
            user.setPhoneNumber(phoneField.getText().toString());
        } catch (IllegalArgumentException e) {
            phoneField.setError("Not a valid phone number");
            valid = false;
        }

        if (!valid) { return; }

        Intent intent = getIntent();
        intent.putExtra(EXTRA_RETURNED_USER, user);

        setResult(RESULT_OK, intent);
        finish();
    }
}
