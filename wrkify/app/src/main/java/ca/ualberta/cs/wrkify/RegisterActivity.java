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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import static ca.ualberta.cs.wrkify.LoginActivity.EXTRA_SESSION_USER;

/**
 * Allows a user to register.
 * This is currently implemented by having the user select a username,
 * and creating a user with that name.
 * Takes no input intents.
 * Returns the newly-registered user as EXTRA_SESSION_USER if successful
 * (result RESULT_OK). May also return RESULT_CANCELLED if cancelled.
 */
public class RegisterActivity extends Activity {
    private EditText registerField;
    private EditText registerEmail;
    private EditText registerPhonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_TransparentActionBar);
        setContentView(R.layout.activity_register);

        getActionBar().setTitle("Sign up");

        this.registerField = findViewById(R.id.registerField);
        this.registerEmail = findViewById(R.id.registerEmail);
        this.registerPhonenumber = findViewById(R.id.registerPhoneNumber);
        Button registerButton = findViewById(R.id.registerButton);

        // pressing the register button finishes the activity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRegisterAndFinish(
                        registerField.getText().toString(),
                        registerEmail.getText().toString(),
                        registerPhonenumber.getText().toString()
                );
            }
        });

        // submitting the username field finishes the activity
        registerField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tryRegisterAndFinish(
                            registerField.getText().toString(),
                            registerEmail.getText().toString(),
                            registerPhonenumber.getText().toString()
                    );
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Attempts to register the given username. If successful, finishes
     * the activity; otherwise sets an error message and does not finish.
     * @param username username to register
     * @param email the email to register with
     * @param phoneNumber the phone number to register with
     */
    private void tryRegisterAndFinish(String username, String email, String phoneNumber) {
        boolean valid = true;

        try {
            User.verifyUsername(username);
        } catch (IllegalArgumentException e) {
            registerField.setError(e.getMessage());
            valid = false;
        }

        try {
            User.verifyEmail(email);
        } catch (IllegalArgumentException e) {
            registerEmail.setError(e.getMessage());
            valid = false;
        }

        try {
            User.verifyPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            registerPhonenumber.setError(e.getMessage());
            valid = false;
        }

        if (!valid) { return; }

        this.new RegisterTask().execute(username, email, phoneNumber);
    }

    /**
     * RegisterTask is an AsyncTask for registering a user
     * and dispatching the necessary handlers on completion.
     */
    private class RegisterTask extends AsyncTask<String, Void, User> {
        private Session session;

        /**
         * gets the session and creates a new user with the given parameters
         * @param strings username, email, phonenumber
         * @return the new user
         */
        @Override
        protected User doInBackground(String... strings) {
            String username    = strings[0];
            String email       = strings[1];
            String phoneNumber = strings[2];

            RemoteClient rc = WrkifyClient.getInstance();

            try {
                if (rc.getSearcher().getUser(username) != null) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }

            session = Session.getInstance(RegisterActivity.this, rc);
            return rc.create(User.class, username, email, phoneNumber);
        }

        /**
         * depending on the result of doInBackground, either
         * start the main activity or notify the user that it has failed.
         * @param user the newly created user (or null if failed)
         */
        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                this.session.setUser(user, RegisterActivity.this);
                setResult(RESULT_OK);
                finish();
            } else {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content),
                        R.string.bad_user_info, Snackbar.LENGTH_LONG);
                snack.setAction("Action", null);
                snack.show();
            }
        }
    }
}
