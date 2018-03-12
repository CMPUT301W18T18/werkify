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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static ca.ualberta.cs.wrkify.LoginActivity.EXTRA_SESSION_USER;

public class RegisterActivity extends Activity {
    private EditText registerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_TransparentActionBar);
        setContentView(R.layout.activity_register);

        getActionBar().setTitle("Sign up");

        this.registerField = findViewById(R.id.registerField);
        Button registerButton = findViewById(R.id.registerButton);

        // pressing the register button finishes the activity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRegisterAndFinish(registerField.getText().toString());
            }
        });

        // submitting the username field finishes the activity
        registerField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tryRegisterAndFinish(registerField.getText().toString());
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
     */
    private void tryRegisterAndFinish(String username) {
        // TODO actually add user on server
        User user = new ConcreteUser(username, "testuser@example.com", "0000000000");
        getIntent().putExtra(EXTRA_SESSION_USER, user);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
