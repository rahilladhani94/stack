package com.hourstack.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hourstack.Adapters.ViewPagerAdapterSlider;
import com.hourstack.R;



public class ActivityWelcome extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager intro_images;
    private LinearLayout pager_indicator,bottom;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapterSlider mAdapter;
    TextView txt_main_title,txt_title;
    RelativeLayout main_bg;
    int Current_pos=0;

    private int[] mImageResources = {
            R.drawable.welcome_one,
            R.drawable.welcome_two,
            R.drawable.welcome_three, R.drawable.welcome_four, R.drawable.welcome_fifth};
    TextView txt_login,txt_signup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        idMapping();
        setReference();
        setonClick();




    }

    private void idMapping()
    {


        txt_main_title = (TextView)findViewById(R.id.txt_main_title);
        txt_title= (TextView)findViewById(R.id.txt_title);
        bottom =(LinearLayout)findViewById(R.id.bottom);
        main_bg = (RelativeLayout)findViewById(R.id.main_bg);
        txt_login= (TextView)findViewById(R.id.txt_login);
        txt_signup= (TextView)findViewById(R.id.txt_signup);

    }

    public void setReference() {
        //view = LayoutInflater.from(this).inflate(R.layout.activity_main, container);


        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mAdapter = new ViewPagerAdapterSlider(ActivityWelcome.this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(10, 0, 10, 0);
            params.setMargins(10, 0, 10, 0);


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        Log.e("p",""+position);

        if(position==0)
        {

            txt_main_title.setText("Welcome to HourStack");
            txt_title.setText("Make every Hour Count");
            txt_title.setVisibility(View.VISIBLE);

          //  main_bg.setBackground(getResources().getDrawable(R.drawable.welcome_first));
        }
        else if(position==1){

            txt_main_title.setText("Allocate time and resources\nto each day of the week");
            txt_title.setVisibility(View.INVISIBLE);

        }
        else if(position==2){

            txt_main_title.setText("Start timers and accurately\ntrack your time");
            txt_title.setVisibility(View.INVISIBLE);

        }
        else if(position==3){

            txt_main_title.setText("Compare the time spent\nagainst your estimate");
            txt_title.setVisibility(View.INVISIBLE);

        }
        else if(position==3){

            txt_main_title.setText("Group your entries\nby project and label");
            txt_title.setVisibility(View.INVISIBLE);

        }
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {

        } else {

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setonClick()
    {

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityWelcome.this,ActivityLogin.class);
                startActivity(i);
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityWelcome.this,ActivitySignup.class);
                startActivity(i);
            }
        });

    }

}
