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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by peter on 18/03/18.
 */

public class Session {
    private static final String FILENAME = "ca.ualberta.cs.wrkify.Session";
    private static Session instance;

    private User user;

    private Session() {}

    public static Session getInstance(Context context, RemoteClient client) {
        if (instance == null) {
            instance = new Session();
        }

        if (instance.user == null) {
            instance.load(context, client);
        }

        return instance;
    }

    public static Session getInstance(Context context) {
        return getInstance(context, WrkifyClient.getInstance());
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user, Context context) {
        this.user = user;
        save(context);
    }

    public void logout() {
        this.user = null;
        //TODO clear savefile;
    }

    private void save(Context context) {
        // this is adapted from the lab 26-01-2018
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(this.user.getId(), out);

            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void load(Context context, RemoteClient client) {
        // this is adapted from the lab 26-01-2018
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            String id = gson.fromJson(in, String.class);

            this.user = client.download(id, User.class);
        } catch (FileNotFoundException e) {
            this.user = null;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
