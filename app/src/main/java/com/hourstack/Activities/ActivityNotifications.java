package com.hourstack.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;



public class ActivityNotifications extends AppCompatActivity {

   private   TextView txt_save,txt_back;
    private ToggleButton togg_over_allocation,togg_timer_reminder,tog_proxi;
    private EditText edt_longer;
    private  SessionManager sessionManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivityNotifications.this);
        idMapping();
        setData();
        setonClick();

    }

    private void setData() {

        if(sessionManager.getKeyProximity().equalsIgnoreCase("yes")){

            tog_proxi.setChecked(true);
        }
        else {
            tog_proxi.setChecked(false);
        }
        if(sessionManager.getKeyTimerreminder().equalsIgnoreCase("yes")){

            togg_timer_reminder.setChecked(true);

            if(sessionManager.getKeyTimerremindertime().length()>0){
                int seconds = Integer.parseInt(sessionManager.getKeyTimerremindertime());
                int seconds_int = seconds/60;
                Log.e("se",""+seconds_int);

                edt_longer.setText(""+seconds_int);
            }

        }
        else {
            togg_timer_reminder.setChecked(false);
            edt_longer.setText("");
        }
        if(sessionManager.getKeyOverallocation().equalsIgnoreCase("yes")){

            togg_over_allocation.setChecked(true);
        }
        else {
            togg_over_allocation.setChecked(false);
        }


    }

    private void idMapping()
    {
        txt_back = (TextView)findViewById(R.id.txt_back);
        edt_longer  = (EditText)findViewById(R.id.edt_longer);
        togg_over_allocation = (ToggleButton)findViewById(R.id.togg_over_allocation);
        togg_timer_reminder = (ToggleButton)findViewById(R.id.togg_timer_reminder);
        tog_proxi = (ToggleButton)findViewById(R.id.tog_proxi);


    }

    private void setonClick() {
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(togg_timer_reminder.isChecked())
                {

                    if (edt_longer.getText().toString().trim().length() == 0) {

                        AppUtil.Toast(ActivityNotifications.this,"Please enter time");

                        return;
                    }


                }
                saveSettimg();


            }
        });

    }

    private void saveSettimg() {

        if(tog_proxi.isChecked()){
            sessionManager.setKeyProximity("yes");
        }
        else{
            sessionManager.setKeyProximity("no");
        }
        if(togg_timer_reminder.isChecked()){
            int seconds = Integer.parseInt(edt_longer.getText().toString().trim());
            int seconds_int = seconds*60;
            Log.e("se",""+seconds);

            sessionManager.setKeyTimerreminder("yes");
            sessionManager.setKeyTimerremindertime(""+seconds_int);
        }
        else{
            sessionManager.setKeyTimerreminder("no");
            sessionManager.setKeyTimerremindertime("900");
        }
        if(togg_over_allocation.isChecked()){
            sessionManager.setKeyOverallocation("yes");
        }
        else{
            sessionManager.setKeyOverallocation("no");
        }



        finish();
    }



     @Override
    public void onBackPressed() {



         if(togg_timer_reminder.isChecked())
         {

             if (edt_longer.getText().toString().trim().length() == 0) {

                 AppUtil.Toast(ActivityNotifications.this,"Please enter time");

                 return;
             }
             super.onBackPressed();

         }

         saveSettimg();



    }

}
