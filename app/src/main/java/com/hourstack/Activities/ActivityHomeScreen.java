package com.hourstack.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.hourstack.R;
import com.hourstack.Utils.Constants;

import java.util.TimeZone;



public class ActivityHomeScreen extends AppCompatActivity {
    ProgressBar progressBar,progressBardefault;
    ImageView iv_setting;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout ll_projects,ll_label,ll_today;
    TextView txt_projectname,txt_labelname,txt_add_entry;
    ImageView iv_label,iv_project;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        idMapping();
        setonClick();

    }



    ////////////ID MAPPING//////////

    private void idMapping()
    {
        iv_setting = (ImageView)findViewById(R.id.iv_setting);
        iv_setting.setVisibility(View.VISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBardefault = (ProgressBar) findViewById(R.id.progressBardefault);
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

        ll_projects = (LinearLayout)findViewById(R.id.ll_projects);
        ll_label = (LinearLayout)findViewById(R.id.ll_label);
        ll_today = (LinearLayout)findViewById(R.id.ll_today);
        txt_add_entry = (TextView)findViewById(R.id.txt_add_entry);

        txt_labelname = (TextView)findViewById(R.id.txt_labelname);
        txt_projectname = (TextView)findViewById(R.id.txt_projectname);
        iv_project = (ImageView)findViewById(R.id.iv_project);
        iv_label = (ImageView)findViewById(R.id.iv_label);

    }
    private void startProgrees() {

        progressBar.setVisibility(View.VISIBLE);
        progressBardefault.setVisibility(View.GONE);

    }
    private void endProgress() {
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            if(Constants.label_selectedcolor !=null){
                if(Constants.label_selectedcolor.length()>0){
                    int color = 0;
                    txt_labelname.setText(Constants.label_selectedname);
                    color = Color.parseColor("#"+Constants.label_selectedcolor);
                    iv_label.setColorFilter(color);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            if(Constants.project_selectedcolor !=null){
                if(Constants.project_selectedcolor.length()>0){
                    int color = 0;
                    txt_projectname.setText(Constants.project_selectedname);

                    color = Color.parseColor("#"+Constants.project_selectedcolor);

                    iv_project.setColorFilter(color);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    /////////SETON CLICK///////////////

    private void setonClick() {
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityHomeScreen.this,ActivityAccount.class);
                startActivity(i);
            }
        });
        ll_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityHomeScreen.this,ActivityAllProjectsListing.class);
                i.putExtra("edit","no");
                startActivity(i);
            }
        });
        ll_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityHomeScreen.this,ActivityAllLabelsListing.class);
                i.putExtra("edit","no");
                startActivity(i);
            }
        });

        txt_add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityHomeScreen.this,ActivityCreateEntry.class);

                startActivity(i);

            }
        });
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(ActivityHomeScreen.this,"Please click BACK again to exit",Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
