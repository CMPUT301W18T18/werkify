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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**
 * Allows a user to log in.
 * In the current implementation lacking authentication infrastructure,
 * this just asks the user for their username. The user being logged
 * in as must exist in the database.
 * Takes no input intents.
 * Returns the user logged in as as EXTRA_SESSION_USER, always as RESULT_OK.
 */
public class LoginActivity extends AppCompatActivity {
    /** Extra representing the logged-in user. */
    public static String EXTRA_SESSION_USER = "ca.ualberta.cs.wrkify.EXTRA_SESSION_USER";

    private static Integer REQUEST_REGISTER = 11;

    private EditText loginField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        this.new LoginSessionTask().execute();

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
        // submit asynchronously
        this.new LoginUsernameTask().execute(username);
    }

    /**
     * Activity result handler.
     * When called back from RegisterActivity with success, immediately returns
     * the newly-registered user.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            this.new LoginSessionTask().execute();
        }
    }

    /**
     * LoginSessionTask is an AsyncTask to get the sessionUser and
     * end the Activity.
     */
    private class LoginSessionTask extends AsyncTask<Void, Void, User> {
        /**
         * runs in the background the get the session user
         * @param voids unused
         * @return the sessionUser
         */
        @Override
        protected User doInBackground(Void... voids) {
            User sessionUser = Session.getInstance(LoginActivity.this).getUser();
            return sessionUser;
        }

        /**
         * starts the MainActivity when we get our session
         * @param sessionUser the user that is our result
         */
        @Override
        protected void onPostExecute(User sessionUser) {
            if (sessionUser != null) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    /**
     * the enum returned by LoginUsernameTask
     * to trigger various ui responses
     *
     * @see LoginUsernameTask
     */
    private enum LoginResult {
        SUCCESS,
        NOINTERNET,
        NOTFOUND,
    }

    /**
     * LoginUsernameTask offers an asyncronys way of loging in with a username.
     */
    private class LoginUsernameTask extends AsyncTask<String, Void, LoginResult> {

        private User user;
        private Session session;

        /**
         * gets the user and processes the result that should happen.
         * @param usernames an array of length 1 containing the username
         * @return the LoginResult indicating what to do.
         */
        @Override
        protected LoginResult doInBackground(String... usernames) {
            if (usernames.length != 1) {
                throw new IllegalArgumentException("Exactly one username can be provided");
            }
            String username = usernames[0];

            this.session = Session.getInstance(LoginActivity.this);

            try {
                this.user = WrkifyClient.getInstance().getSearcher().getUser(username);
                if (user != null) {
                    return LoginResult.SUCCESS;
                } else {
                    return LoginResult.NOTFOUND;
                }
            } catch (IOException e) {
                return LoginResult.NOINTERNET;
            }
        }

        /**
         * runs to notify the user or start MainActivity after the user has
         * been downloaded
         * @param result the LoginResult telling us what to do
         */
        @Override
        protected void onPostExecute(LoginResult result) {
            if (result == LoginResult.SUCCESS) {
                session.setUser(this.user, LoginActivity.this);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }

            if (result == LoginResult.NOINTERNET) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content),
                        R.string.no_internet, Snackbar.LENGTH_LONG);
                snack.setAction("Action", null);
                snack.show();
                return;
            }

            if (result == LoginResult.NOTFOUND) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content),
                        R.string.login_fail, Snackbar.LENGTH_LONG);
                snack.setAction("Action", null);
                snack.show();
                return;
            }
        }
    }
}
