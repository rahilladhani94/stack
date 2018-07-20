package com.hourstack.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.hourstack.Home.MainActivity;
import com.hourstack.R;
import com.hourstack.Utils.SessionManager;
import com.hourstack.service.service;


/**
 * Created by abc on 12-12-2016.
 */
public class ActivitySplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 100;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sessionManager = new SessionManager(ActivitySplashScreen.this);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {


                if (sessionManager.isLogin())
                {


                    Intent i = new Intent(ActivitySplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    return;


                } else {
                    Intent i = new Intent(ActivitySplashScreen.this, ActivityWelcome.class);
                    startActivity(i);
                    finish();
                    return;


                }



            }
        }, SPLASH_TIME_OUT);




    }


}
