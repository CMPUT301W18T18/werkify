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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOError;
import java.io.IOException;

/**
 * Allows a user to log in.
 * In the current implementation lacking authentication infrastructure,
 * this just asks the user for their username. The user being logged
 * in as must exist in the database.
 * Takes no input intents.
 * Returns the user logged in as as EXTRA_SESSION_USER.
 */
public class LoginActivity extends Activity {
    public static String EXTRA_SESSION_USER = "ca.ualberta.cs.wrkify.EXTRA_SESSION_USER";

    private static Integer REQUEST_REGISTER = 11;

    private EditText loginField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActionBar().hide();

        this.loginField = findViewById(R.id.loginField);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.loginButtonRegister);

        // pressing the login button finishes the activity
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySubmitAndFinish(loginField.getText().toString());
            }
        });

        // submitting the username field finishes the activity
        loginField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    trySubmitAndFinish(loginField.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // pressing the sign up button launches registration
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER);
            }
        });
    }

    /**
     * Attempts to log in with the given username. If successful,
     * finishes the activity and returns the logged-in user.
     * Otherwise, sets an error message and doesn't finish.
     * @param username username to log in as
     */
    private void trySubmitAndFinish(String username) {
        // TODO actually look up user on the server
        User user = new ConcreteUser(username, "testuser@example.com", "0000000000");
        getIntent().putExtra(EXTRA_SESSION_USER, user);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            getIntent().putExtra(EXTRA_SESSION_USER, data.getStringExtra(EXTRA_SESSION_USER));
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
