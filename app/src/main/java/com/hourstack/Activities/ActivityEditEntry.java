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
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


public class ActivityEditEntry extends AppCompatActivity {
    private ProgressBar progressBar, progressBardefault;
    private LinearLayout ll_more, ll_bootom;
    private PopupWindow popup_window_filter;
    private ImageView iv_start, iv_complete, iv_project, iv_label, iv_clockallocated;
    private TextView txt_back, txt_start, txt_complete, txt_labelname, txt_projectname;
    private LinearLayout ll_start, ll_pause, ll_complete, ll_resume, ll_project, ll_label;
    View view_top;
    Animation startRotateAnimation;
    public static String edit = "yes";
    private String label_selectedcolor = "";
    private String label_selectedname = "";
    public static String label_selectedid = "";

    private String project_selectedcolor = "";
    private String project_selectedname = "";
    public static String project_selectedid = "";
    private SessionManager sessionManager;
    private String event = "";
    private EditText edt_description, edt_actual_hh, edt_actual_mm, edt_allocated_hh, edt_allocated_mm, edt_date, edt_notes, edt_actual_hh_timer, edt_actual_mm_timer, edt_actual_ss_timer;

    private Button btn_Save;
    private String allocatedtime = "", actualtime = "";
    private String timer = "0", complete = "0", event_id = "", event_date = "";
    public static ArrayList<String> datelist;
    public static ArrayList<String> eventslistsdate;
    List<EntriesListResponse> entriesListResponses;
    int counter = 1;
    String datestatus = "";
    String startbutton = "", pausbutton = "", completebutton = "", resumebutton = "";
    Double latitude = 0.0, longitude = 0.0;
    private int CAM_PERMISSION_CODE = 24;
    LinearLayout ll_actual, ll_timer;
    public static CountDownTimer timer_c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivityEditEntry.this);
        idMapping();
        if (isLaLongAllowed()) {
            getLatAndLong();

        } else {
            requestLatLong();
        }

        label_selectedcolor = "";
        label_selectedname = "";
        label_selectedid = "";

        project_selectedcolor = "";
        project_selectedname = "";
        project_selectedid = "";
        getEntries();
        getData();
        setonClick();

    }

    private void getData() {

        Intent intent = getIntent();
        Bundle extas = intent.getExtras();
        if (extas != null) {
            event = extas.getString("event");


            String[] parts = event.split("@@"); // escape .
            String eventname = parts[0];
            String eventdate = parts[1];
            String eventid = parts[2];
            String allocated_seconds = parts[3];
            String allocated_hours = parts[4];
            String actual_seconds = parts[5];
            String actual_hours = parts[6];
            String complete = parts[7];
            String project_id = parts[8];
            String label_id = parts[9];
            String order = parts[10];
            String notes = parts[11];
            String timing = parts[12];
            String date_last_timed = parts[13];
            String task_source = parts[14];
            String user_id = parts[15];
            String project_name = parts[16];
            String project_color = parts[17];
            String project_archived = parts[18];
            String label_name = parts[19];
            String label_color = parts[20];
            String label_archived = parts[21];
            String user_name = parts[22];
            String team_name = parts[23];

            Log.e("project_id", "" + project_id);

            if (!eventname.equalsIgnoreCase("eventname")) {
                edt_description.setText(eventname);


            }
            if (!notes.equalsIgnoreCase("notes")) {
                edt_notes.setText(notes);


            }

            if (!eventid.equalsIgnoreCase("eventid")) {
                event_id = eventid;

            }
            if (!eventdate.equalsIgnoreCase("eventdate")) {

                event_date = eventdate;
                eventdate = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEE,MMM dd", eventdate);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                String today = sdf.format(c.getTime());
                if (today.equalsIgnoreCase(event_date)) {
                    Log.e("today", "today");
                    checkDateDifference(event_date, "yes");
                } else {
                    checkDateDifference(event_date, "no");
                }

                edt_date.setText(eventdate);
            }
            if (!project_name.equalsIgnoreCase("project_name")) {
                txt_projectname.setText(project_name);
            }

            if (!label_name.equalsIgnoreCase("label_name")) {
                txt_labelname.setText(label_name);
            }
            if (!project_id.equalsIgnoreCase("project_id")) {
                project_selectedid = project_id;
            }
            if (!label_id.equalsIgnoreCase("label_id")) {
                label_selectedid = label_id;
            }


            String secondsapi = "";

            if (!actual_seconds.equalsIgnoreCase("actual_seconds")) {

                secondsapi = actual_seconds;
            } else {
                secondsapi = "00";
            }

            if (timing.equalsIgnoreCase("1")) {

                startbutton = "no";
                pausbutton = "yes";
                completebutton = "no";
                resumebutton = "no";
                secondsapi = UIUtil.convertDateTime("ss", "HH:mm:ss", secondsapi);


                ll_pause.setVisibility(View.VISIBLE);
                ll_complete.setVisibility(View.VISIBLE);
                ll_start.setVisibility(View.GONE);


                ll_timer.setVisibility(View.VISIBLE);
                ll_actual.setVisibility(View.GONE);


                String hours = extas.getString("hours");
                String mins = extas.getString("mins");
                String seconds = extas.getString("seconds");


                edt_actual_hh_timer.setText(hours);
                edt_actual_mm_timer.setText(mins);
                edt_actual_ss_timer.setText(seconds);


                edt_allocated_hh.setFocusable(false);
                edt_allocated_hh.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                edt_allocated_hh.setClickable(false);

                edt_allocated_mm.setFocusable(false);
                edt_allocated_mm.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                edt_allocated_mm.setClickable(false);


                timer_c = new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        try {


                            int tep = 0, tepmin = 0, tephours = 0;
                            tep = Integer.parseInt(edt_actual_ss_timer.getText().toString());
                            tepmin = Integer.parseInt(edt_actual_mm_timer.getText().toString());
                            tephours = Integer.parseInt(edt_actual_hh_timer.getText().toString());
                            tep++;

                            if (tepmin <= 9) {
                                edt_actual_mm_timer.setText("0" + tepmin);
                            } else {
                                edt_actual_mm_timer.setText("" + tepmin);
                            }

                            if (tep <= 9) {
                                edt_actual_ss_timer.setText("0" + tep);
                            } else {
                                edt_actual_ss_timer.setText("" + tep);
                            }

                            if (tephours <= 9) {
                                edt_actual_hh_timer.setText("0" + tephours);
                            } else {
                                edt_actual_hh_timer.setText("" + tephours);
                            }


                            if (tep > 60) {

                                tepmin++;
                                tep = tep - 60;
                                tep++;

                                if (tepmin >= 60) {
                                    tepmin = tepmin - 60;
                                    tephours++;

                                }
                                edt_actual_ss_timer.setText("00");
                                if (tepmin <= 9) {
                                    edt_actual_mm_timer.setText("0" + tepmin);
                                } else {
                                    edt_actual_mm_timer.setText("" + tepmin);
                                }

                                if (tep <= 9) {
                                    edt_actual_ss_timer.setText("0" + tep);
                                } else {
                                    edt_actual_ss_timer.setText("" + tep);
                                }

                                if (tephours <= 9) {
                                    edt_actual_hh_timer.setText("0" + tephours);
                                } else {
                                    edt_actual_hh_timer.setText("" + tephours);
                                }


                            }


                            timer_c.start();
                        } catch (Exception e) {
                            Log.e("Error", "Error: " + e.toString());
                        }
                    }
                }.start();

            } else if (timing.equalsIgnoreCase("0")) {
                startbutton = "yes";
                pausbutton = "no";
                completebutton = "yes";
                resumebutton = "no";


                ll_pause.setVisibility(View.GONE);
                ll_start.setVisibility(View.VISIBLE);
                ll_timer.setVisibility(View.GONE);
                ll_actual.setVisibility(View.VISIBLE);

                startbutton = "yes";
                pausbutton = "no";
                completebutton = "yes";
                resumebutton = "no";


            }


            if (!complete.equalsIgnoreCase("complete")) {

                if (complete.equalsIgnoreCase("1")) {


                    if (timing.equalsIgnoreCase("1")) {
                        ll_start.setVisibility(View.GONE);
                    } else {
                        ll_start.setVisibility(View.VISIBLE);
                    }

                    ll_resume.setVisibility(View.VISIBLE);

                    ll_complete.setVisibility(View.GONE);


                    startbutton = "no";
                    pausbutton = "no";
                    completebutton = "no";
                    resumebutton = "yes";


                } else if (complete.equalsIgnoreCase("0")) {

                    if (timing.equalsIgnoreCase("1")) {
                        ll_start.setVisibility(View.GONE);
                    } else {
                        ll_start.setVisibility(View.VISIBLE);
                    }


                    ll_resume.setVisibility(View.GONE);

                    ll_complete.setVisibility(View.VISIBLE);

                    startbutton = "yes";
                    pausbutton = "no";
                    completebutton = "yes";
                    resumebutton = "no";

                }
            }

            if (!actual_seconds.equalsIgnoreCase("actual_seconds")) {

                String actual = "";
                actual = UIUtil.convertDateTime("ss", "HH:mm", actual_seconds);


                String[] partsactual = actual.split(":"); // escape .
                String hh = partsactual[0];
                String mm = partsactual[1];

                edt_actual_hh.setText("" + hh);
                edt_actual_mm.setText("" + mm);

            }
            if (!allocated_seconds.equalsIgnoreCase("allocated_seconds")) {


                String allocated = "";
                allocated = UIUtil.convertDateTime("ss", "HH:mm", allocated_seconds);


                String[] partsallocated = allocated.split(":"); // escape .
                String hh = partsallocated[0];
                String mm = partsallocated[1];

                edt_allocated_hh.setText("" + hh);
                edt_allocated_mm.setText("" + mm);

            }

            if (project_color != null && !project_color.equalsIgnoreCase("project_color")) {
                int color = 0;


                if (project_color != null) {

                    try {
                        String f = "#" + project_color;
                        iv_project.setColorFilter(Color.parseColor(f));
                    } catch (Exception e) {

                    }
                }

            }


            if (label_color != null && !label_color.equalsIgnoreCase("label_color")) {
                int color = 0;


                if (label_color != null) {

                    try {
                        String f = "#" + label_color;
                        iv_label.setColorFilter(Color.parseColor(f));
                        view_top.setBackgroundColor(Color.parseColor(f));
                    } catch (Exception e) {

                    }
                }

            }


        }
    }



    ///////id mapping///////////
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

        ll_actual = (LinearLayout) findViewById(R.id.ll_actual);
        ll_timer = (LinearLayout) findViewById(R.id.ll_timer);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_actual_hh = (EditText) findViewById(R.id.edt_actual_hh);
        edt_allocated_hh = (EditText) findViewById(R.id.edt_allocated_hh);
        edt_actual_mm = (EditText) findViewById(R.id.edt_actual_mm);
        edt_allocated_mm = (EditText) findViewById(R.id.edt_allocated_mm);
        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_notes = (EditText) findViewById(R.id.edt_notes);
        btn_Save = (Button) findViewById(R.id.btn_Save);

        edt_date.setFocusable(false);
        edt_date.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edt_date.setClickable(false);

        edt_actual_hh_timer = (EditText) findViewById(R.id.edt_actual_hh_timer);
        edt_actual_mm_timer = (EditText) findViewById(R.id.edt_actual_mm_timer);
        edt_actual_ss_timer = (EditText) findViewById(R.id.edt_actual_ss_timer);

    }




    ///////////entries api/////////////
    private void getEntries() {

        UIUtil.hideKeyboard(ActivityEditEntry.this);
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

                        // AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");
                    }
                }
            }

            @Override
            public void onFailure(Call<EntriesMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }




    ////////////get current lat&long///////////////
    private void getLatAndLong() {

        LocationManager locationManager = (LocationManager) ActivityEditEntry.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ActivityEditEntry.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityEditEntry.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


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
            Log.e("latitude", "" + latitude);
            Log.e("longitude", "" + longitude);


        }


    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ActivityEditEntry.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage("GPS is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ActivityEditEntry.this.startActivity(intent);
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

    private void startProgrees() {

        progressBar.setVisibility(View.VISIBLE);
        progressBardefault.setVisibility(View.GONE);

    }

    private void endProgress() {
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

    }


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
                showMorePopup();
            }
        });
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datestatus.equalsIgnoreCase("F")) {
                    if (startbutton.equalsIgnoreCase("yes")) {
                        setStartTask();

//                    startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
//                    iv_clockallocated.startAnimation(startRotateAnimation);
                    }

                }


            }
        });
        ll_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datestatus.equalsIgnoreCase("F")) {
                    if (completebutton.equalsIgnoreCase("yes")) {
                        setEndTask();


//                    startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
//                    iv_clockallocated.startAnimation(startRotateAnimation);
                    }
                }
                // iv_clockallocated.clearAnimation();
            }
        });


        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityEditEntry.this, ActivityAllProjectsListing.class);
                i.putExtra("edit", "yes");
                startActivityForResult(i, 202);
            }
        });
        ll_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityEditEntry.this, ActivityAllLabelsListing.class);
                i.putExtra("edit", "yes");
                startActivityForResult(i, 201);
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDatepicker();
            }
        });


        ll_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPauseApi();

            }
        });

        ll_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callResumeApi();

            }
        });
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_description.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please enter description");
                    return;
                }

                if (edt_date.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please select date");
                    return;
                }
                if (edt_allocated_hh.getText().toString().trim().length() == 0 || edt_allocated_hh.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please enter allocated time");
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
                EditEntrey();
            }
        });
    }


    ////////resume api////////
    private void callResumeApi() {
        sessionManager.setKeyCurrentseconds("");
        sessionManager.setKeyRunningeventid("");
        sessionManager.setKeyStartedseconds("");
        sessionManager.setKeyAllocatedseconds("");
        Intent myService = new Intent(ActivityEditEntry.this, service.class);
        stopService(myService);


        HashMap<String, Object> map = new HashMap<>();
//
//


        map.put("complete", "0");

        Log.e("map", "" + map.toString());

        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.editEntry(sessionManager.getKeyAuthtoken(), map, event_id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {

                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {
                        AppUtil.Toast(ActivityEditEntry.this, "Task successfully resumed.");

                        Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    } else {
                        AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


    ///////////puase api////////////
    private void callPauseApi() {
        sessionManager.setKeyCurrentseconds("");
        sessionManager.setKeyRunningeventid("");
        sessionManager.setKeyStartedseconds("");
        sessionManager.setKeyAllocatedseconds("");

        Intent myService = new Intent(ActivityEditEntry.this, service.class);
        stopService(myService);

        UIUtil.hideKeyboard(ActivityEditEntry.this);

        UIUtil.showDialog(ActivityEditEntry.this);


        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.pauseTask(sessionManager.getKeyAuthtoken());
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    AppUtil.Toast(ActivityEditEntry.this, "Task successfully stopped");

                    Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });


    }

    private void EditEntrey() {


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
        map.put("order", counter);
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

        } else {
            map.put("notes", "");
        }

        Log.e("map", "" + map.toString());

        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.editEntry(sessionManager.getKeyAuthtoken(), map, event_id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {

                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {
                        AppUtil.Toast(ActivityEditEntry.this, "Task successfully updated.");

                        Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    } else {
                        AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


    private void duplicateEntrey() {


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
        map.put("order", counter);
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

        } else {
            map.put("notes", "");
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
                        AppUtil.Toast(ActivityEditEntry.this, "Task successfully updated.");

                        Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    } else {
                        AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


    private void openDatepicker() {
        counter = 1;
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(ActivityEditEntry.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String convertedDateTime = "";
                        convertedDateTime = UIUtil.convertDateTime("yyyy-M-d", "yyyy-MM-dd 00:00:00", (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                        event_date = convertedDateTime;

                        convertedDateTime = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEE,MMM dd", convertedDateTime);
                        edt_date.setText(convertedDateTime);

                        String day = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEEE", event_date);


                        if (day.equalsIgnoreCase("Monday")) {
                            if (sessionManager.getKeyFirstday() != null) {

                                if (!sessionManager.getKeyFirstday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeyFirstday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }


                            }
                        } else if (day.equalsIgnoreCase("Tuesday")) {
                            if (sessionManager.getKeySecondday() != null) {


                                if (!sessionManager.getKeySecondday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeySecondday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }


                            }
                        } else if (day.equalsIgnoreCase("Wednesday")) {
                            if (sessionManager.getKeyThirdday() != null) {

                                if (!sessionManager.getKeyThirdday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeyThirdday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }

                            }
                        } else if (day.equalsIgnoreCase("Thursday")) {
                            if (sessionManager.getKeyFourthday() != null) {

                                if (!sessionManager.getKeyFourthday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeyFourthday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }
                            }
                        } else if (day.equalsIgnoreCase("Friday")) {
                            if (sessionManager.getKeyFifthday() != null) {

                                if (!sessionManager.getKeyFifthday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeyFifthday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }
                            }
                        } else if (day.equalsIgnoreCase("Saturday")) {
                            if (sessionManager.getKeySixthtday() != null) {


                                if (!sessionManager.getKeySixthtday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeySixthtday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }
                            }
                        } else if (day.equalsIgnoreCase("Sunday")) {
                            if (sessionManager.getKeySeventhday() != null) {

                                if (!sessionManager.getKeySeventhday().equalsIgnoreCase("0")) {
                                    String temp = UIUtil.convertDateTime("hh", "hh:mm", sessionManager.getKeySeventhday());
                                    String[] partsallocated = temp.split(":"); // escape .
                                    String hh = partsallocated[0];
                                    String mm = partsallocated[1];

                                    edt_allocated_hh.setText("" + hh);
                                    edt_allocated_mm.setText("" + mm);

                                } else {
                                    edt_allocated_hh.setText("08");
                                    edt_allocated_mm.setText("00");
                                }
                            }
                        }


                        checkOrderOfDate();
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
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


    private void setStartTask() {

        if (latitude != null && longitude != null) {
            sessionManager.setKeyTimerlat(latitude);
            sessionManager.setKeyTimerlong(longitude);
        }
        calllStartApi();


        timer = "1";
        complete = "0";


        txt_start.setTextColor(getResources().getColor(R.color.textdarkgrey));
        txt_complete.setTextColor(getResources().getColor(R.color.grey));
        iv_start.setImageResource(R.drawable.play_ac);
        iv_complete.setImageResource(R.drawable.checked_de);
    }

    private void calllStartApi() {
        sessionManager.setKeyCurrentseconds("");
        sessionManager.setKeyRunningeventid("");
        sessionManager.setKeyStartedseconds("");
        sessionManager.setKeyAllocatedseconds("");

        Intent myService = new Intent(ActivityEditEntry.this, service.class);
        stopService(myService);


        UIUtil.hideKeyboard(ActivityEditEntry.this);

        UIUtil.showDialog(ActivityEditEntry.this);
        HashMap<String, Object> map = new HashMap<>();
        map.put("timing", "1");

        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.editEntry(sessionManager.getKeyAuthtoken(), map, event_id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    AppUtil.Toast(ActivityEditEntry.this, "Task successfully started");

                    Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });


    }

    private void calllCompleteApi() {
        sessionManager.setKeyCurrentseconds("");
        sessionManager.setKeyRunningeventid("");
        sessionManager.setKeyStartedseconds("");
        sessionManager.setKeyAllocatedseconds("");
        Intent myService = new Intent(ActivityEditEntry.this, service.class);
        stopService(myService);
        UIUtil.hideKeyboard(ActivityEditEntry.this);

        UIUtil.showDialog(ActivityEditEntry.this);
        HashMap<String, Object> map = new HashMap<>();
        map.put("complete", "1");

        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.editEntry(sessionManager.getKeyAuthtoken(), map, event_id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    AppUtil.Toast(ActivityEditEntry.this, "Task successfully completed");

                    Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });


    }


    private void setEndTask() {
        calllCompleteApi();
        timer = "0";
        complete = "1";
        txt_complete.setTextColor(getResources().getColor(R.color.textdarkgrey));
        txt_start.setTextColor(getResources().getColor(R.color.grey));
        iv_start.setImageResource(R.drawable.play_de);
        iv_complete.setImageResource(R.drawable.checked_ac);
    }


    private void showMorePopup() {
        LayoutInflater layoutInflater = (LayoutInflater) ActivityEditEntry.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.more_entry_dialog,
                null, true);


        if (popup_window_filter != null && popup_window_filter.isShowing()) {
            popup_window_filter.dismiss();
        }


        TextView txt_cancel = (TextView) popupView.findViewById(R.id.txt_cancel);
        TextView txt_delete = (TextView) popupView.findViewById(R.id.txt_delete);
        TextView rollover = (TextView) popupView.findViewById(R.id.rollover);
        TextView txt_duplicate = (TextView) popupView.findViewById(R.id.txt_duplicate);

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UIUtil.checkNetwork(ActivityEditEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityEditEntry.this);
                    return;
                }
                popup_window_filter.dismiss();
                deleteEntry();
            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup_window_filter.dismiss();
            }
        });

        rollover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UIUtil.checkNetwork(ActivityEditEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityEditEntry.this);
                    return;
                }
                popup_window_filter.dismiss();
                callRolleverAPI();
            }
        });

        txt_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UIUtil.checkNetwork(ActivityEditEntry.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityEditEntry.this);
                    return;
                }
                popup_window_filter.dismiss();
                if (edt_description.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please enter description");
                    return;
                }

                if (edt_date.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please select date");
                    return;
                }
                if (edt_allocated_hh.getText().toString().trim().length() == 0 || edt_allocated_hh.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityEditEntry.this, "Please enter allocated time");
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
                duplicateEntrey();
            }
        });


        popup_window_filter = null;
        popup_window_filter = new PopupWindow(popupView,
                (int) (UIUtil.get_device_width(ActivityEditEntry.this) / 1),
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

    private void checkDateDifference(String event_date, String today) {


        if (!today.equalsIgnoreCase("yes")) {
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

                    datestatus = "P";
                    Log.e("firs", "first");


                } else {
                    datestatus = "F";
                    Log.e("second", "second");

                }

            } catch (ParseException e) {

                e.printStackTrace();
            }


        } else {
            datestatus = "T";
        }


        setButton();

    }

    private void setButton() {

        if (datestatus.equalsIgnoreCase("F")) {

            timer = "0";
            complete = "0";

            startbutton = "no";
            pausbutton = "no";
            completebutton = "no";
            resumebutton = "no";

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

        } else if (datestatus.equalsIgnoreCase("T")) {

            startbutton = "yes";
            pausbutton = "yes";
            completebutton = "yes";
            resumebutton = "yes";

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

        } else if (datestatus.equalsIgnoreCase("P")) {

            startbutton = "no";
            pausbutton = "no";
            completebutton = "no";
            resumebutton = "no";

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

    private void callRolleverAPI() {


        HashMap<String, Object> map = new HashMap<>();


        map.put("entry_id", event_id);

        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.rolloverTasks(sessionManager.getKeyAuthtoken(), map);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {

                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {
                        AppUtil.Toast(ActivityEditEntry.this, "Task successfully rollover.");

                        Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    } else {
                        AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });

    }

    private void deleteEntry() {
        UIUtil.hideKeyboard(ActivityEditEntry.this);

        UIUtil.showDialog(ActivityEditEntry.this);


        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.deleteEntry(sessionManager.getKeyAuthtoken(), event_id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    AppUtil.Toast(ActivityEditEntry.this, "Task successfully deleted");

                    Intent i = new Intent(ActivityEditEntry.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
                        // AppUtil.Toast(ActivityEditEntry.this,""+error);

                        AppUtil.Toast(ActivityEditEntry.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityEditEntry.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

    private boolean isLaLongAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(ActivityEditEntry.this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    private void requestLatLong() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityEditEntry.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(ActivityEditEntry.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CAM_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == CAM_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast


            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(ActivityEditEntry.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {


        if (timer_c != null) {
            timer_c.cancel();
        }


        super.onBackPressed();
        return;


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK ){

            if( requestCode==201){
                label_selectedcolor = data.getExtras().getString("label_selectedcolor");
                label_selectedname = data.getExtras().getString("label_selectedname");
                label_selectedid = data.getExtras().getString("label_selectedid");

                Log.e("label_selectedid",""+label_selectedid);


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



