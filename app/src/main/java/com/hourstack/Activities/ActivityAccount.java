package com.hourstack.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hourstack.R;
import com.hourstack.Utils.SessionManager;


public class ActivityAccount extends AppCompatActivity {

    private  TextView txt_done;
    private RelativeLayout rl_setting,rl_viewwebsite,rl_notification,rl_workspace,rl_hepl,rl_logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        idMapping();
        setonClick();

    }


    ///////////id mapping////////////////
    private void idMapping()
    {
        txt_done = (TextView)findViewById(R.id.txt_done);

        rl_setting = (RelativeLayout)findViewById(R.id.rl_setting);
        rl_viewwebsite = (RelativeLayout)findViewById(R.id.rl_viewwebsite);
        rl_notification = (RelativeLayout)findViewById(R.id.rl_notification);
        rl_workspace = (RelativeLayout)findViewById(R.id.rl_workspace);
        rl_hepl = (RelativeLayout)findViewById(R.id.rl_hepl);
        rl_logout = (RelativeLayout)findViewById(R.id.rl_logout);

    }




    //////////seton click////////////////
    private void setonClick() {

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityAccount.this,ActivitySettingScreen.class);
                startActivity(i);
            }
        });
        rl_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityAccount.this,ActivityNotifications.class);
                startActivity(i);
            }
        });

        rl_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityAccount.this,ActivityWorkspaceListing.class);
                startActivity(i);
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager session = new SessionManager(ActivityAccount.this);
                session.removeUserDetail();
                Intent i_logout = new Intent(ActivityAccount.this, ActivityWelcome.class);
                i_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i_logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i_logout);
            }
        });
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        rl_viewwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hourstack.io/"));
                startActivity(intent);

            }
        });
        rl_hepl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hourstack.io/contact"));
                startActivity(intent);

            }
        });
    }



}
