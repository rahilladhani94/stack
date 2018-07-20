/**
 * Copyright 2014 Magnus Woxblom
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hourstack.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hourstack.Activities.ActivityAccount;
import com.hourstack.Activities.ActivityAllLabelsListing;
import com.hourstack.Activities.ActivityAllProjectsListing;
import com.hourstack.Activities.ActivityCreateEntry;

import com.hourstack.R;
import com.hourstack.Utils.Constants;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    LinearLayout ll_projects, ll_label, ll_today;
    TextView txt_projectname, txt_labelname, txt_add_entry;
    ImageView iv_label, iv_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        idMapping();

        setonClick();

        if (savedInstanceState == null) {
            showFragment(BoardFragment.newInstance());
        }


    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }


    private void idMapping() {


    }


    private void setonClick() {


    }


    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {


            if (ItemAdapter.timer != null) {
                ItemAdapter.timer.cancel();


            }
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(MainActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
