package com.hourstack.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hourstack.Home.MainActivity;
import com.hourstack.Model.EntriesListResponse;
import com.hourstack.Model.EntriesMainResponse;
import com.hourstack.Model.NewProjectMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.Constants;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;
import com.hourstack.service.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityCreateEntry extends AppCompatActivity {
    private ProgressBar progressBar, progressBardefault;
    private LinearLayout ll_more, ll_bootom;
    private PopupWindow popup_window_filter;
    private ImageView iv_start, iv_complete, iv_project, iv_label, iv_clockallocated;
    private TextView txt_back, txt_start, txt_complete, txt_labelname, txt_projectname;
    private LinearLayout ll_start,ll_pause, ll_complete,ll_resume, ll_project, ll_label;
    View view_top;
    Animation startRotateAnimation;
    private EditText edt_description, edt_actual_hh, edt_actual_mm, edt_allocated_hh, edt_allocated_mm, edt_date, edt_notes;

    private String label_selectedcolor = "";
    private String label_selectedname = "";
    private String label_selectedid = "";

    private String project_selectedcolor = "";
    private String project_selectedname = "";
    private String project_selectedid = "";
    SessionManager sessionManager;

    public static ArrayList<String> datelist;
    public static ArrayList<String> eventslistsdate;
    private List<EntriesListResponse> entriesListResponses;
    String timer = "0",complete="0", event_id = "", event_date = "";
    int counter = 1;
    private Button btn_Save;
    private String allocatedtime = "", actualtime = "";
    private String datestatus="";
    private  String startbutton="",pausbutton="",completebutton="",resumebutton="";
    private Double latitude = 0.0, longitude = 0.0;
    private int CAM_PERMISSION_CODE = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivityCreateEntry.this);
        idMapping();
        if(isLaLongAllowed()){
            getLatAndLong();

        }
        else {
            requestLatLong();
        }

        label_selectedcolor = "";
        label_selectedname = "";
        label_selectedid = "";

        project_selectedcolor = "";
        project_selectedname = "";
        project_selectedid = "";
        getData();
        if (!UIUtil.checkNetwork(ActivityCreateEntry.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityCreateEntry.this);
            return;
        }
        getEntries();
        setonClick();

    }

    private void getData() {

        try {
            if (Constants.label_selectedcolor != null) {
                if (Constants.label_selectedcolor.length() > 0) {
                    int color = 0;
                    txt_labelname.setText(Constants.label_selectedname);
                    color = Color.parseColor("#" + Constants.label_selectedcolor);
                    iv_label.setColorFilter(color);
                    view_top.setBackgroundColor(color);

                    label_selectedcolor = Constants.label_selectedcolor;
                    label_selectedname = Constants.label_selectedname;
                    label_selectedid = Constants.label_selectedid;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            if (Constants.project_selectedcolor != null) {
                if (Constants.project_selectedcolor.length() > 0) {
                    int color = 0;
                    txt_projectname.setText(Constants.project_selectedname);

                    color = Color.parseColor("#" + Constants.project_selectedcolor);

                    iv_project.setColorFilter(color);

                    project_selectedcolor = Constants.project_selectedcolor;
                    project_selectedname = Constants.project_selectedname;
                    project_selectedid = Constants.project_selectedid;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    ////////id mapping////////////

    private void idMapping() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBardefault = (ProgressBar) findViewById(R.id.progressBardefault);
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);
        txt_back = (TextView) findViewById(R.id.txt_back);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        ll_bootom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_start = (LinearLayout) findViewById(R.id.ll_start);
        ll_pause = (LinearLayout) findViewById(R.id.ll_pause);
        ll_resume = (LinearLayout) findViewById(R.id.ll_resume);
        ll_complete = (LinearLayout) findViewById(R.id.ll_complete);
        ll_project = (LinearLayout) findViewById(R.id.ll_project);
        ll_label = (LinearLayout) findViewById(R.id.ll_label);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        iv_complete = (ImageView) findViewById(R.id.iv_complete);
        iv_clockallocated = (ImageView) findViewById(R.id.iv_clockallocated);
        txt_start = (TextView) findViewById(R.id.txt_start);
        txt_complete = (TextView) findViewById(R.id.txt_complete);


        txt_labelname = (TextView) findViewById(R.id.txt_labelname);
        txt_projectname = (TextView) findViewById(R.id.txt_projectname);
        iv_project = (ImageView) findViewById(R.id.iv_project);
        iv_label = (ImageView) findViewById(R.id.iv_label);
        view_top = (View) findViewById(R.id.view_top);


        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_actual_hh = (EditText) findViewById(R.id.edt_actual_hh);
        edt_allocated_hh = (EditText) findViewById(R.id.edt_allocated_hh);
        edt_actual_mm = (EditText) findViewById(R.id.edt_actual_mm);
        edt_allocated_mm = (EditText) findViewById(R.id.edt_allocated_mm);
        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_notes = (EditText) findViewById(R.id.edt_notes);
        btn_Save = (Button) findViewById(R.id.btn_Save);

        ll_more.setVisibility(View.GONE);
    }



    //////get entries/////////
    private void getEntries() {

        UIUtil.hideKeyboard(ActivityCreateEntry.this);
        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<EntriesMainResponse> call = apiService.getEntries(sessionManager.getKeyAuthtoken());
        call.enqueue(new Callback<EntriesMainResponse>() {
            @Override
            public void onResponse(Call<EntriesMainResponse> call, Response<EntriesMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {
                    EntriesMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null && !registrationResponse.getData().isEmpty()) {
                        entriesListResponses = registrationResponse.getData();
                        Log.e("totalentries", "" + entriesListResponses.size());

                        eventslistsdate = new ArrayList<String>();

                        for (int i = 0; i < entriesListResponses.size(); i++) {

                            eventslistsdate.add(entriesListResponses.get(i).getDate());
                        }

                        Log.e("eventsdate", "" + eventslistsdate.size());


                    }
                } else {
                    if (response.code() == 400) {
                        InputStream in = response.errorBody().byteStream();
                        String error = AppUtil.getStringRequestBody(in);

                        JSONObject jresponse = null;
                        try {
                            jresponse = new JSONObject(error);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            error = jresponse.getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // AppUtil.Toast(ActivityLogin.this,""+error);

                        // AppUtil.Toast(ActivityCreateEntry.this, "Your session has expired please re login again to continue.");
                    }
                }
            }

            @Override
            public void onFailure(Call<EntriesMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityCreateEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (label_selectedcolor != null) {
                if (label_selectedcolor.length() > 0) {
                    int color = 0;
                    txt_labelname.setText(label_selectedname);
                    color = Color.parseColor("#" + label_selectedcolor);
                    iv_label.setColorFilter(color);
                    view_top.setBackgroundColor(color);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            if (project_selectedcolor != null) {
                if (project_selectedcolor.length() > 0) {
                    int color = 0;
                    txt_projectname.setText(project_selectedname);

                    color = Color.parseColor("#" + project_selectedcolor);

                    iv_project.setColorFilter(color);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //////current latlong////////////
    private void getLatAndLong() {


        LocationManager locationManager = (LocationManager) ActivityCreateEntry.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ActivityCreateEntry.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityCreateEntry.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (myLocation == null) {
            showSettingsAlert("NETWORK");
            return;


        } else {


            longitude = myLocation.getLongitude();
            latitude = myLocation.getLatitude();
            Log.e("latitude",""+latitude);
            Log.e("longitude",""+longitude);

        }


        

    }
    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ActivityCreateEntry.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage("GPS is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ActivityCreateEntry.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void startProgrees() {

        progressBar.setVisibility(View.VISIBLE);
        progressBardefault.setVisibility(View.GONE);

    }

    private void endProgress() {
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

    }






    ///seton click////////////
    private void setonClick() {
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cc", "" + counter);
                showMorePopup();
            }
        });
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtil.checkNetwork(ActivityCreateEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityCreateEntry.this);
                    return;
                }
                if(startbutton.equalsIgnoreCase("yes")){
                    setStartTask();

//                    startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
//                    iv_clockallocated.startAnimation(startRotateAnimation);
                }


            }
        });
        ll_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtil.checkNetwork(ActivityCreateEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityCreateEntry.this);
                    return;
                }
                if(completebutton.equalsIgnoreCase("yes")){
                    setEndTask();


//                    startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
//                    iv_clockallocated.startAnimation(startRotateAnimation);
                }
               // iv_clockallocated.clearAnimation();
            }
        });
        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCreateEntry.this, ActivityAllProjectsListing.class);
                i.putExtra("edit", "create");
                startActivityForResult(i, 202);
            }
        });
        ll_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCreateEntry.this, ActivityAllLabelsListing.class);
                i.putExtra("edit", "create");
                startActivityForResult(i, 201);
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDatepicker();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtil.checkNetwork(ActivityCreateEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityCreateEntry.this);
                    return;
                }
                if (edt_description.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityCreateEntry.this, "Please enter description");
                    return;
                }

                if (edt_date.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityCreateEntry.this, "Please select date");
                    return;
                }
                if (edt_allocated_hh.getText().toString().trim().length() == 0 || edt_allocated_hh.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityCreateEntry.this, "Please enter allocated time");
                    return;
                }


                String allocatedhh = "00", allocatedmm = "00", allocatedss = "00";
                ;
                if (edt_allocated_hh.getText().toString().trim().length() != 0) {
                    allocatedhh = edt_allocated_hh.getText().toString().trim();

                }
                if (edt_allocated_mm.getText().toString().trim().length() != 0) {
                    allocatedmm = edt_allocated_mm.getText().toString().trim();

                }
                String allocatedfinal = allocatedhh + ":" + allocatedmm + ":" + "00";
                allocatedtime = allocatedfinal;


                String actualhh = "00", actualmm = "00", actualss = "00";
                if (edt_actual_hh.getText().toString().trim().length() != 0) {
                    actualhh = edt_actual_hh.getText().toString().trim();

                }
                if (edt_actual_mm.getText().toString().trim().length() != 0) {
                    actualmm = edt_actual_mm.getText().toString().trim();

                }
                String actualfinal = actualhh + ":" + actualmm + ":" + "00";
                actualtime = actualfinal;
                createEntry();
            }
        });
    }

    private void openDatepicker() {
        counter = 1;
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(ActivityCreateEntry.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String convertedDateTime = "";
                        convertedDateTime = UIUtil.convertDateTime("yyyy-M-d", "yyyy-MM-dd 00:00:00", (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                        event_date = convertedDateTime;

                        convertedDateTime = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEE,MMM dd", convertedDateTime);
                        edt_date.setText(convertedDateTime);


                        String temp = UIUtil.convertDateTime("ss", "hh:mm", sessionManager.getKeyDefaultallocation());
                        String[] partsallocated = temp.split(":"); // escape .
                        String hh = partsallocated[0];
                        String mm = partsallocated[1];

                        edt_allocated_hh.setText("" + hh);
                        edt_allocated_mm.setText("" + mm);



                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                        String today = sdf.format(c.getTime());
                        if(today.equalsIgnoreCase(event_date)){
                            Log.e("today","today");
                            checkDateDifference(event_date,"yes");
                        }
                        else{
                            checkDateDifference(event_date,"no");
                        }
                        checkOrderOfDate();

                        String day = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEEE", event_date);




                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    private void checkDateDifference(String event_date,String today) {

        if(!today.equalsIgnoreCase("yes")){
            String toyBornTime = event_date;
            SimpleDateFormat dateFormat_diff = new SimpleDateFormat(
                    "yyyy-MM-dd 00:00:00");

            try {

                Date oldDate = null;
                try {
                    oldDate = dateFormat_diff.parse(toyBornTime);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(oldDate);

                Date currentDate = new Date();
                //     Log.e("cureent_date",""+currentDate);

                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;



                if (oldDate.before(currentDate)) {

                    datestatus="P";
                    Log.e("firs","first");

                }

                else {
                    datestatus="F";
                    Log.e("second","second");

                }

            } catch (ParseException e) {

                e.printStackTrace();
            }


        }
        else{
            datestatus="T";
        }



        setButton();

    }

    private void setButton() {

        if(datestatus.equalsIgnoreCase("F")){

            timer="0";
            complete="0";

            startbutton = "no";
            pausbutton ="no";
            completebutton ="no";
            resumebutton ="no";

            edt_actual_mm.setFocusable(false);
            edt_actual_mm.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edt_actual_mm.setClickable(false);


            edt_actual_hh.setFocusable(false);
            edt_actual_hh.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edt_actual_hh.setClickable(false);

            edt_allocated_hh.setFocusable(true);
            edt_allocated_hh.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_hh.setClickable(true);


            edt_allocated_mm.setFocusable(true);
            edt_allocated_mm.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_mm.setClickable(true);

            ll_start.setVisibility(View.VISIBLE);
            ll_pause.setVisibility(View.GONE);
            ll_resume.setVisibility(View.GONE);
            ll_complete.setVisibility(View.VISIBLE);

        }

       else if(datestatus.equalsIgnoreCase("T")){

            startbutton = "yes";
            pausbutton ="yes";
            completebutton ="yes";
            resumebutton ="yes";

            edt_actual_mm.setFocusable(true);
            edt_actual_mm.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_actual_mm.setClickable(true);


            edt_actual_hh.setFocusable(true);
            edt_actual_hh.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_actual_hh.setClickable(true);

            edt_allocated_hh.setFocusable(true);
            edt_allocated_hh.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_hh.setClickable(true);


            edt_allocated_mm.setFocusable(true);
            edt_allocated_mm.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_mm.setClickable(true);

            ll_start.setVisibility(View.VISIBLE);
            ll_pause.setVisibility(View.GONE);
            ll_resume.setVisibility(View.GONE);
            ll_complete.setVisibility(View.VISIBLE);

        }



        else if(datestatus.equalsIgnoreCase("P")){

            startbutton = "no";
            pausbutton ="no";
            completebutton ="no";
            resumebutton ="no";

            edt_actual_mm.setFocusable(true);
            edt_actual_mm.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_actual_mm.setClickable(true);


            edt_actual_hh.setFocusable(true);
            edt_actual_hh.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_actual_hh.setClickable(true);

            edt_allocated_hh.setFocusable(false);
            edt_allocated_hh.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_hh.setClickable(true);


            edt_allocated_mm.setFocusable(true);
            edt_allocated_mm.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            edt_allocated_mm.setClickable(true);

            ll_start.setVisibility(View.VISIBLE);
            ll_pause.setVisibility(View.GONE);
            ll_resume.setVisibility(View.GONE);
            ll_complete.setVisibility(View.VISIBLE);

        }
    }

    private void checkOrderOfDate() {


        if (eventslistsdate != null) {
            int index = 0;

            for (int i = 0; i < eventslistsdate.size(); i++) {

                if (eventslistsdate.get(i).equalsIgnoreCase(event_date)) {

                    counter++;

                } else {
                }


            }

        }
    }



    ////////////start task////////////
    private void setStartTask() {


        if(latitude !=null && longitude !=null)
        {
            sessionManager.setKeyTimerlat(latitude);
            sessionManager.setKeyTimerlong(longitude);
        }

        timer = "1";
        complete= "0";


        txt_start.setTextColor(getResources().getColor(R.color.textdarkgrey));
        txt_complete.setTextColor(getResources().getColor(R.color.grey));
        iv_start.setImageResource(R.drawable.play_ac);
        iv_complete.setImageResource(R.drawable.checked_de);
    }




    ///////create entry api/////////////
    private void createEntry() {
        sessionManager.setKeyCurrentseconds("");
        sessionManager.setKeyRunningeventid("");
        sessionManager.setKeyStartedseconds("");
        sessionManager.setKeyAllocatedseconds("");
        Intent myService = new Intent(ActivityCreateEntry.this, service.class);
        stopService(myService);

        HashMap<String, Object> map = new HashMap<>();
//
//
        String timestampStr_Actual = actualtime;
        String[] tokens_Actual = timestampStr_Actual.split(":");
        int hours_Actual = Integer.parseInt(tokens_Actual[0]);
        int minutes_Actual = Integer.parseInt(tokens_Actual[1]);
        int seconds_Actual = Integer.parseInt(tokens_Actual[2]);
        int duration_Actual = 3600 * hours_Actual + 60 * minutes_Actual + seconds_Actual;


        String timestampStr_Allocated = allocatedtime;
        String[] tokens_Allocated = timestampStr_Allocated.split(":");
        int hours_Allocated = Integer.parseInt(tokens_Allocated[0]);
        int minutes_Allocated = Integer.parseInt(tokens_Allocated[1]);
        int seconds_Allocated = Integer.parseInt(tokens_Allocated[2]);
        int duration_Allocated = 3600 * hours_Allocated + 60 * minutes_Allocated + seconds_Allocated;

        map.put("timing", timer);
        map.put("allocated_seconds", duration_Allocated);
        map.put("actual_seconds", duration_Actual);
        map.put("order",counter);
        map.put("name", "" + edt_description.getText().toString());
        map.put("date", event_date);

        if (project_selectedid.length() > 0) {
            map.put("project_id", project_selectedid);
        } else {
            map.put("project_id", "");
        }
        if (label_selectedid.length() > 0) {
            map.put("label_id", label_selectedid);
        } else {
            map.put("label_id", "");
        }

        if (edt_notes.getText().toString().trim().length() != 0) {
            map.put("notes", "" + edt_notes.getText().toString());

        }
        Log.e("map", "" + map.toString());

        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.newEntry(sessionManager.getKeyAuthtoken(), map);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {

                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {
                        AppUtil.Toast(ActivityCreateEntry.this, "Task successfully created.");

                        Intent i = new Intent(ActivityCreateEntry.this, MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    } else {
                        AppUtil.Toast(ActivityCreateEntry.this, "Something went wrong,Please try again.");
                    }
                } else {
                    if (response.code() == 400) {
                        InputStream in = response.errorBody().byteStream();
                        String error = AppUtil.getStringRequestBody(in);

                        JSONObject jresponse = null;
                        try {
                            jresponse = new JSONObject(error);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            error = jresponse.getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // AppUtil.Toast(ActivityCreateEntry.this,""+error);

                        AppUtil.Toast(ActivityCreateEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityCreateEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }





    ///////////end task//////////////
    private void setEndTask() {

        timer = "0";
        complete= "1";
        txt_complete.setTextColor(getResources().getColor(R.color.textdarkgrey));
        txt_start.setTextColor(getResources().getColor(R.color.grey));
        iv_start.setImageResource(R.drawable.play_de);
        iv_complete.setImageResource(R.drawable.checked_ac);
    }


    private void showMorePopup() {
        LayoutInflater layoutInflater = (LayoutInflater) ActivityCreateEntry.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.more_entry_dialog,
                null, true);


        if (popup_window_filter != null && popup_window_filter.isShowing()) {
            popup_window_filter.dismiss();
        }


        TextView txt_cancel = (TextView) popupView.findViewById(R.id.txt_cancel);


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup_window_filter.dismiss();
            }
        });


        popup_window_filter = null;
        popup_window_filter = new PopupWindow(popupView,
                (int) (UIUtil.get_device_width(ActivityCreateEntry.this) / 1),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popup_window_filter.setBackgroundDrawable(new BitmapDrawable());// must put

        popup_window_filter.setOutsideTouchable(false);
        //popup_window_filter.setAnimationStyle(android.R.anim.accelerate_decelerate_interpolator);

        popup_window_filter
                .showAtLocation(
                        ll_bootom,
                        Gravity.RIGHT | Gravity.BOTTOM,
                        0, 00);


    }





    /////////////6.0 above os permission////////////////
    private boolean isLaLongAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(ActivityCreateEntry.this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }




    private void requestLatLong(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityCreateEntry.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(ActivityCreateEntry.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CAM_PERMISSION_CODE);
    }
   
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == CAM_PERMISSION_CODE  ){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast

                
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(ActivityCreateEntry.this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK ){

            if( requestCode==201){
                label_selectedcolor = data.getExtras().getString("label_selectedcolor");
                label_selectedname = data.getExtras().getString("label_selectedname");
                label_selectedid = data.getExtras().getString("label_selectedid");


                try {
                    if (label_selectedcolor != null) {
                        if (label_selectedcolor.length() > 0) {
                            int color = 0;
                            txt_labelname.setText(label_selectedname);
                            color = Color.parseColor("#" + label_selectedcolor);
                            iv_label.setColorFilter(color);
                            view_top.setBackgroundColor(color);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            else if( requestCode==202){
                project_selectedcolor = data.getExtras().getString("project_selectedcolor");
                project_selectedname = data.getExtras().getString("project_selectedname");
                project_selectedid = data.getExtras().getString("project_selectedid");


                try {

                    if (project_selectedcolor != null) {
                        if (project_selectedcolor.length() > 0) {
                            int color = 0;
                            txt_projectname.setText(project_selectedname);

                            color = Color.parseColor("#" + project_selectedcolor);

                            iv_project.setColorFilter(color);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
