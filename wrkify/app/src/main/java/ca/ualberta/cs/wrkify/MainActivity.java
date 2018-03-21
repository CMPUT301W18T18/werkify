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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

/**
 * the entry point to the wrkify app, provides the bottom navigation
 * and delegates to fragments
 *
 * @see RequesterFragment
 * @see ProviderFragment
 * @see SearchFragment
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment frag;
            switch (item.getItemId()) {
                case R.id.navigation_posts:
                    frag = new RequesterFragment();
                    break;
                case R.id.navigation_tasks:
                    frag = new ProviderFragment();
                    break;
                case R.id.navigation_search:
                    frag = new SearchFragment();
                    break;
                default:
                    return false;
            }
            showFragment(frag);
            return true;
        }
    };

    /**
     * creates the main activity, using a bottom nav
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Session.getInstance(this).refreshCaches(WrkifyClient.getInstance());
        } catch (IOException e) {
            // TODO You are offline.
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new RequesterFragment());
    }

    /**
     * set the main fragment to the provided fragment
     * @param frag the fragment you want to display
     */
    protected void showFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();

        fragTransaction.replace(R.id.fragment_container, frag, null);
        fragTransaction.commit();
    }

}
