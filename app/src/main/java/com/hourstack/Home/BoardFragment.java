/*
 * Copyright 2014 Magnus Woxblom
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hourstack.Home;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hourstack.Activities.ActivityAccount;
import com.hourstack.Activities.ActivityAllLabelsListing;
import com.hourstack.Activities.ActivityAllProjectsListing;
import com.hourstack.Activities.ActivityCreateEntry;
import com.hourstack.Activities.ActivityEditEntry;
import com.hourstack.Activities.ActivitySplashScreen;
import com.hourstack.Activities.ActivityWelcome;
import com.hourstack.Model.EntriesListResponse;
import com.hourstack.Model.EntriesMainResponse;
import com.hourstack.Model.NewProjectMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.Constants;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    private BoardView mBoardView;
    private int mColumns;
    View view;
    ProgressBar progressBar, progressBardefault;
    ImageView iv_setting;
    LinearLayout ll_projects, ll_label, ll_today;
    TextView txt_projectname, txt_labelname, txt_add_entry, header_text;
    ImageView iv_label, iv_project;
    List<EntriesListResponse> entriesListResponses;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    String today_date = "";
    public static ArrayList<String> datelist;
    public static ArrayList<String> eventslistsdate;
    int counter = 0;
    RelativeLayout rl_main_bg;
    SessionManager sessionManager;
    public static String referesh = "no";
    String drageventid="";
    Double latitude = 0.0, longitude = 0.0;
    private int CAM_PERMISSION_CODE = 24;
   SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        idMappping();
//        if(isLaLongAllowed()){
//            getLatAndLong();
//
//        }
//        else {
//            requestLatLong();
//        }
        setOnclick();
        getCurrentDate();


        return view;
    }


    private void idMappping() {
        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.column_item));
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        mBoardView.setVisibility(View.GONE);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setColorSchemeResources(
//                R.color.colorAccent,
//                R.color.bg_splash,
//                R.color.colorAccent);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        iv_setting.setVisibility(View.VISIBLE);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        progressBardefault = (ProgressBar) view.findViewById(R.id.progressBardefault);
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);
        ll_projects = (LinearLayout) view.findViewById(R.id.ll_projects);
        ll_label = (LinearLayout) view.findViewById(R.id.ll_label);
        ll_today = (LinearLayout) view.findViewById(R.id.ll_today);
        // txt_add_entry = (TextView)findViewById(R.id.txt_add_entry);

        txt_labelname = (TextView) view.findViewById(R.id.txt_labelname);
        txt_projectname = (TextView) view.findViewById(R.id.txt_projectname);
        iv_project = (ImageView) view.findViewById(R.id.iv_project);
        iv_label = (ImageView) view.findViewById(R.id.iv_label);
        txt_add_entry = (TextView) view.findViewById(R.id.txt_add_entry);
        rl_main_bg = (RelativeLayout) view.findViewById(R.id.rl_main_bg);
        rl_main_bg.setBackgroundColor(getResources().getColor(R.color.theme_grey));
    }

    private void getLatAndLong() {


        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


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
            if(latitude !=null && longitude !=null)
            {
                sessionManager.setKeyTimerlat(latitude);
                sessionManager.setKeyTimerlong(longitude);
            }
            Log.e("latitude",""+latitude);
            Log.e("longitude",""+longitude);

        }




    }
    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
               getActivity());

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage("GPS is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       getActivity().startActivity(intent);
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
    /////////////////////get all entries////////////////////////////////////////
    private void getEntries(final String scroll) {
        referesh = "no";
        UIUtil.hideKeyboard(getActivity());
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
                        for (int i = 0; i < datelist.size(); i++) {
                            addColumnList("" + datelist.get(i), i);
                        }
                        if(scroll.equalsIgnoreCase("yes")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBoardView.scrollToColumn(30, true);
                                }
                            }, 100);
                        }

                    } else {
                        for (int i = 0; i < datelist.size(); i++) {
                            addColumnList("" + datelist.get(i), i);
                        }
                        if(scroll.equalsIgnoreCase("yes")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBoardView.scrollToColumn(30, true);
                                }
                            }, 100);
                        }


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

                        AppUtil.Toast(getActivity(), "Your session has expired please re login again to continue.");
                    }
                }
            }

            @Override
            public void onFailure(Call<EntriesMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(getActivity(), "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });


    }


    /////////////////////show date list in viewpager////////////////////////////////////////

    private void getCurrentDate() {
        today_date = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
        datelist = new ArrayList<String>();

        for (int i = 30; i >= 1; i--) {

            String dt = today_date;  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, -i);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String output = sdf1.format(c.getTime());
            datelist.add(output);
        }

        datelist.add(today_date);

        Log.e("siz", "" + datelist.size());
        for (int i = 1; i <= 30; i++) {

            String dt = today_date;  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, i);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String output = sdf1.format(c.getTime());
            datelist.add(output);
        }
        getEntries("yes");

    }


    /////////////////////progressbar////////////////////////////////////////
    private void startProgrees() {
        progressBar.setVisibility(View.VISIBLE);
        progressBardefault.setVisibility(View.GONE);

    }

    private void endProgress() {
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

    }


    private void setOnclick() {
        ll_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoardView.scrollToColumn(30, true);


            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBoardView.setVisibility(View.GONE);
                mBoardView.clearBoard();
                 getEntries("no");


                referesh = "no";
                for (int i = 0; i < datelist.size(); i++) {
                    addColumnList("" + datelist.get(i), i);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                mBoardView.clearBoard();
//                // getEntries();
//
//
//                referesh = "no";
//                for (int i = 0; i < datelist.size(); i++) {
//                    addColumnList("" + datelist.get(i), i);
//                }
//            }
//        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityAccount.class);
                startActivity(i);
            }
        });
        txt_add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




               Intent i = new Intent(getActivity(), ActivityCreateEntry.class);
                startActivity(i);
            }
        });
        ll_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ActivityAllProjectsListing.class);
                i.putExtra("edit", "no");
                startActivity(i);
            }
        });
        ll_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ActivityAllLabelsListing.class);
                i.putExtra("edit", "no");
                startActivity(i);


            }
        });

        mBoardView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("g", "" + header_text.getText().toString());


            }
        });

        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {

                //      Toast.makeText(mBoardView.getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                //      Toast.makeText(mBoardView.getContext(), "Position changed - column: " + newColumn + " row: " + newRow, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.txt_day);
                //itemCount1.setText("" + mBoardView.getAdapter(oldColumn).getItemCount());
                Log.e("header", "" + itemCount1.getText().toString());

                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.txt_day);
                // itemCount2.setText("" + mBoardView.getAdapter(newColumn).getItemCount());


            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn,  int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {

                    TextView itemCount1 = (TextView) mBoardView.getHeaderView(fromColumn).findViewById(R.id.txt_day);
                    //itemCount1.setText("" + mBoardView.getAdapter(oldColumn).getItemCount());
                    Log.e("header", "" + itemCount1.getText().toString());
                     TextView itemCount2 = (TextView) mBoardView.getHeaderView(toColumn).findViewById(R.id.txt_day);

                    if(Constants.drageventid .length()>1){
                        callReorderApi(itemCount2.getText().toString(),toRow);
                    }



                    //Toast.makeText(mBoardView.getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBoardView.setBoardCallback(new BoardView.BoardCallback() {
            @Override
            public boolean canDragItemAtPosition(int column, int dragPosition) {
                // Add logic here to prevent an item to be dragged
                return true;
            }

            @Override
            public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                // Add logic here to prevent an item to be dropped

                return true;
            }
        });




    }

    private void callReorderApi(String headerdate, int toRow) {
        headerdate = UIUtil.convertDateTime("EEE,MMM dd yyyy", "yyyy-MM-dd 00:00:00", headerdate);

        int torow = toRow+1;
        Log.e("re_headerdate",""+headerdate);
        Log.e("re_order",""+torow);
        Log.e("id",""+Constants.drageventid);

        Log.e("api","yes");
        HashMap<String, Object> map = new HashMap<>();
        map.put("order",torow);
        map.put("date",headerdate);

        Log.e("map",""+map.toString());

        

        startProgrees();
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.editEntry(sessionManager.getKeyAuthtoken(),map,Constants.drageventid);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {

                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null)
                    {
                       
                        Constants.drageventid ="";

                    } else {
                        
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
                        // AppUtil.Toast(getActivity(),""+error);

                       // AppUtil.Toast(getActivity(),"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(getActivity(),"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);

        if (Constants.label_selectedid.length() > 0 || Constants.project_selectedid.length() > 0) {

            if (referesh.equalsIgnoreCase("yes")) {
                mBoardView.setVisibility(View.GONE);
                mBoardView.clearBoard();
               // getEntries();


                referesh = "no";
                for (int i = 0; i < datelist.size(); i++) {
                    addColumnList("" + datelist.get(i), i);
                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mBoardView.scrollToColumn(60, true);
//                    }
//                }, 100);
//

            }


        }
        try {
            if (Constants.label_selectedcolor != null) {
                if (Constants.label_selectedcolor.length() > 0) {
                    int color = 0;
                    txt_labelname.setText(Constants.label_selectedname);
                    color = Color.parseColor("#" + Constants.label_selectedcolor);
                    iv_label.setColorFilter(color);
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
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /////////////////////add columns as per date list size////////////////////////////////////////
    private void addColumnList(String date, int num) {


        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        final int column = mColumns;


        ArrayList<String> eventsfoundlist;
        eventsfoundlist = new ArrayList<String>();


        ///////////check date  have events or not//////////


        if (eventslistsdate != null) {
            int index = 0;

            for (int i = 0; i < eventslistsdate.size(); i++) {

                if (eventslistsdate.get(i).equalsIgnoreCase(date)) {
                    String eventname = "eventname", eventdate = "eventdate", eventid = "eventid", allocated_seconds = "allocated_seconds",
                            allocated_hours = "allocated_hours", actual_seconds = "actual_seconds", actual_hours = "actual_hours",
                            complete = "complete", project_id = "project_id", label_id = "label_id", order = "order", notes = "notes",
                            timing = "timing", date_last_timed = "date_last_timed", task_source = "task_source", user_id = "user_id",
                            project_name = "project_name", project_color = "project_color", project_archived = "project_archived",
                            label_name = "label_name", label_color = "label_color", label_archived = "label_archived",
                            user_name = "user_name", team_name = "team_name",updatedat = "updatedat";
                    Log.e("in", "" + i);
                    eventsfoundlist.add("" + i);

                    long id = sCreatedItems++;
                    if (entriesListResponses.get(i).getName() != null) {

                        if (entriesListResponses.get(i).getName().length() > 0) {
                            eventname = "" + entriesListResponses.get(i).getName();
                        }
                    }
                    if (entriesListResponses.get(i).getDate() != null) {

                        if (entriesListResponses.get(i).getDate().length() > 0) {
                            eventdate = "" + entriesListResponses.get(i).getDate();
                        }
                    }
                    if (entriesListResponses.get(i).getId() != null) {

                        if (entriesListResponses.get(i).getId().toString().length() > 0) {
                            eventid = "" + entriesListResponses.get(i).getId();
                        }
                    }

                    if (entriesListResponses.get(i).getAllocatedSeconds() != null) {

                        if (entriesListResponses.get(i).getAllocatedSeconds().toString().length() > 0) {
                            allocated_seconds = "" + entriesListResponses.get(i).getAllocatedSeconds();
                        }
                    }
                    if (entriesListResponses.get(i).getAllocatedHours() != null) {

                        if (entriesListResponses.get(i).getAllocatedHours().toString().length() > 0) {
                            allocated_hours = "" + entriesListResponses.get(i).getAllocatedHours();
                        }
                    }
                    if (entriesListResponses.get(i).getActualSeconds() != null) {

                        if (entriesListResponses.get(i).getActualSeconds().toString().length() > 0) {
                            actual_seconds = "" + entriesListResponses.get(i).getActualSeconds();
                        }
                    }
                    if (entriesListResponses.get(i).getActualHours() != null) {

                        if (entriesListResponses.get(i).getActualHours().toString().length() > 0) {
                            actual_hours = "" + entriesListResponses.get(i).getActualHours();
                        }
                    }
                    if (entriesListResponses.get(i).getComplete() != null) {

                        if (entriesListResponses.get(i).getComplete().toString().length() > 0) {
                            complete = "" + entriesListResponses.get(i).getComplete();
                        }
                    }
                    if (entriesListResponses.get(i).getProjectId() != null) {
                        if (entriesListResponses.get(i).getProjectId().toString().length() > 0) {
                            project_id = "" + entriesListResponses.get(i).getProjectId();
                        }
                    }
                    if (entriesListResponses.get(i).getLabelId() != null) {

                        if (entriesListResponses.get(i).getLabelId().toString().length() > 0) {
                            label_id = "" + entriesListResponses.get(i).getLabelId();
                        }
                    }
                    if (entriesListResponses.get(i).getOrder() != null) {

                        if (entriesListResponses.get(i).getOrder().toString().length() > 0) {
                            order = "" + entriesListResponses.get(i).getOrder();
                        }
                    }
                    if (entriesListResponses.get(i).getNotes() != null) {

                        if (entriesListResponses.get(i).getNotes().toString().length() > 0) {
                            notes = "" + entriesListResponses.get(i).getNotes();
                        }
                    }
                    if (entriesListResponses.get(i).getTiming() != null) {

                        if (entriesListResponses.get(i).getTiming().toString().length() > 0) {
                            timing = "" + entriesListResponses.get(i).getTiming();
                        }
                    }
                    if (entriesListResponses.get(i).getDateLastTimed() != null) {

                        if (entriesListResponses.get(i).getDateLastTimed().toString().length() > 0) {
                            date_last_timed = "" + entriesListResponses.get(i).getDateLastTimed();
                        }
                    }
                    if (entriesListResponses.get(i).getTaskSource() != null) {

                        if (entriesListResponses.get(i).getTaskSource().toString().length() > 0) {
                            task_source = "" + entriesListResponses.get(i).getTaskSource();
                        }
                    }
                    if (entriesListResponses.get(i).getUserId() != null) {

                        if (entriesListResponses.get(i).getUserId().toString().length() > 0) {
                            user_id = "" + entriesListResponses.get(i).getUserId();
                        }
                    }

                    if (entriesListResponses.get(i).getProjectName() != null) {

                        if (entriesListResponses.get(i).getProjectName().toString().length() > 0) {
                            project_name = "" + entriesListResponses.get(i).getProjectName();
                        }
                    }
                    if (entriesListResponses.get(i).getProjectColor() != null) {

                        if (entriesListResponses.get(i).getProjectColor().toString().length() > 0) {
                            project_color = "" + entriesListResponses.get(i).getProjectColor();
                        }
                    }
                    if (entriesListResponses.get(i).getProjectArchived() != null) {

                        if (entriesListResponses.get(i).getProjectArchived().toString().length() > 0) {
                            project_archived = "" + entriesListResponses.get(i).getProjectArchived();
                        }
                    }
                    if (entriesListResponses.get(i).getLabelName() != null) {

                        if (entriesListResponses.get(i).getLabelName().toString().length() > 0) {
                            label_name = "" + entriesListResponses.get(i).getLabelName();
                        }
                    }
                    if (entriesListResponses.get(i).getLabelColor() != null) {

                        if (entriesListResponses.get(i).getLabelColor().toString().length() > 0) {
                            label_color = "" + entriesListResponses.get(i).getLabelColor();
                        }
                    }

                    if (entriesListResponses.get(i).getLabelArchived() != null) {

                        if (entriesListResponses.get(i).getLabelArchived().toString().length() > 0) {
                            label_archived = "" + entriesListResponses.get(i).getLabelArchived();
                        }
                    }
                    if (entriesListResponses.get(i).getUserName() != null) {

                        if (entriesListResponses.get(i).getUserName().toString().length() > 0) {
                            user_name = "" + entriesListResponses.get(i).getUserName();
                        }
                    }
                    if (entriesListResponses.get(i).getTeamName() != null) {

                        if (entriesListResponses.get(i).getTeamName().toString().length() > 0) {
                            team_name = "" + entriesListResponses.get(i).getTeamName();
                        }
                    }
                    if (entriesListResponses.get(i).getUpdatedAt() != null) {

                        if (entriesListResponses.get(i).getUpdatedAt().toString().length() > 0) {
                            updatedat = "" + entriesListResponses.get(i).getUpdatedAt();
                        }
                    }
                    if (!label_id.equalsIgnoreCase("label_id")) {
                        if (Constants.label_selectedid.length() > 0 && Constants.project_selectedid.length() > 0) {
                            if (Constants.label_selectedid.equals("" + label_id) && Constants.project_selectedid.equals("" + project_id)) {

                                String value = "" + eventname + "@@" +
                                        eventdate + "@@" +
                                        eventid + "@@" +
                                        allocated_seconds + "@@" +
                                        allocated_hours + "@@" +
                                        actual_seconds + "@@" +
                                        actual_hours + "@@" +
                                        complete + "@@" +
                                        project_id + "@@" +
                                        label_id + "@@" +
                                        order + "@@" +
                                        notes + "@@" +
                                        timing + "@@" +
                                        date_last_timed + "@@" +
                                        task_source + "@@" +
                                        user_id + "@@" +
                                        project_name + "@@" +
                                        project_color + "@@" +
                                        project_archived + "@@" +
                                        label_name + "@@" +
                                        label_color + "@@" +
                                        label_archived + "@@" +
                                        user_name + "@@" +
                                        team_name +"@@"+
                                        updatedat;
                                mItemArray.add(new Pair<>(id, " " + value));

                            } else {


                            }
                        } else if (Constants.label_selectedid.length() > 0 || Constants.project_selectedid.length() > 0) {
                            if (Constants.label_selectedid.equals("" + label_id) || Constants.project_selectedid.equals("" + project_id)) {

                                String value = "" + eventname + "@@" +
                                        eventdate + "@@" +
                                        eventid + "@@" +
                                        allocated_seconds + "@@" +
                                        allocated_hours + "@@" +
                                        actual_seconds + "@@" +
                                        actual_hours + "@@" +
                                        complete + "@@" +
                                        project_id + "@@" +
                                        label_id + "@@" +
                                        order + "@@" +
                                        notes + "@@" +
                                        timing + "@@" +
                                        date_last_timed + "@@" +
                                        task_source + "@@" +
                                        user_id + "@@" +
                                        project_name + "@@" +
                                        project_color + "@@" +
                                        project_archived + "@@" +
                                        label_name + "@@" +
                                        label_color + "@@" +
                                        label_archived + "@@" +
                                        user_name + "@@" +
                                        team_name +"@@"+
                                        updatedat;
                                mItemArray.add(new Pair<>(id, " " + value));

                            } else {


                            }
                        } else {
                            String value = "" + eventname + "@@" +
                                    eventdate + "@@" +
                                    eventid + "@@" +
                                    allocated_seconds + "@@" +
                                    allocated_hours + "@@" +
                                    actual_seconds + "@@" +
                                    actual_hours + "@@" +
                                    complete + "@@" +
                                    project_id + "@@" +
                                    label_id + "@@" +
                                    order + "@@" +
                                    notes + "@@" +
                                    timing + "@@" +
                                    date_last_timed + "@@" +
                                    task_source + "@@" +
                                    user_id + "@@" +
                                    project_name + "@@" +
                                    project_color + "@@" +
                                    project_archived + "@@" +
                                    label_name + "@@" +
                                    label_color + "@@" +
                                    label_archived + "@@" +
                                    user_name + "@@" +
                                    team_name +"@@"+
                                    updatedat;
                            mItemArray.add(new Pair<>(id, " " + value));

                        }

                    }


                } else {


                }


            }

        }


        final ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.column_item, R.id.item_layout, true, getActivity(), mColumns);
        final View header = View.inflate(getActivity(), R.layout.column_header, null);

//        ((TextView) header.findViewById(R.id.text)).setText("Column " + (mColumns + 1));
//        ((TextView) header.findViewById(R.id.item_count)).setText("" + addItems);

        header_text = (TextView) header.findViewById(R.id.txt_day);
        LinearLayout ll_blocked = (LinearLayout) header.findViewById(R.id.ll_blocked);
        if (date.equalsIgnoreCase(today_date)) {

            header_text.setTextColor(getResources().getColor(R.color.colorPrimary));


        } else {

            header_text.setTextColor(getResources().getColor(R.color.black));
        }

        String day = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEEE", date);


        if (day.equalsIgnoreCase("Monday"))
        {
            if(sessionManager.getKeyFirstday() !=null){

                if(!sessionManager.getKeyFirstday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }


        else  if(day.equalsIgnoreCase("Tuesday")){
            if(sessionManager.getKeySecondday() !=null){

                if(!sessionManager.getKeySecondday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
        else  if(day.equalsIgnoreCase("Wednesday")){
            if(sessionManager.getKeyThirdday()!=null){

                if(!sessionManager.getKeyThirdday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
        else  if(day.equalsIgnoreCase("Thursday")){
            if(sessionManager.getKeyFourthday() !=null){

                if(!sessionManager.getKeyFourthday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
        else  if(day.equalsIgnoreCase("Friday")){
            if(sessionManager.getKeyFifthday() !=null){

                if(!sessionManager.getKeyFifthday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
        else  if(day.equalsIgnoreCase("Saturday")){
            if(sessionManager.getKeySixthtday()!=null){

                if(!sessionManager.getKeySixthtday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
         else  if(day.equalsIgnoreCase("Sunday")){
            if(sessionManager.getKeySeventhday()!=null){

                if(!sessionManager.getKeySeventhday().equalsIgnoreCase("0")){
                    ll_blocked.setVisibility(View.GONE);
                }
                else{
                    ll_blocked.setVisibility(View.VISIBLE);
                }
            }
        }
        date = UIUtil.convertDateTime("yyyy-MM-dd 00:00:00", "EEE,MMM dd yyyy", date);


        header_text.setText("" + date);


        Log.e("WHERE", "" + column);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("WHERE", "" + column);
            }
        });

        mBoardView.addColumnList(listAdapter, header, false);
        mBoardView.setPivotX(80);
        mColumns++;

        mBoardView.setVisibility(View.VISIBLE);
    }


    /////////////////////Drag item////////////////////////////////////////
    private static class MyDragItem extends DragItem {


        MyDragItem(Context context, int layoutId) {

            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));
            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            TextView txt = ((TextView) dragView.findViewById(R.id.text));

           Log.e("dragid", "" + txt.getText().toString());



           Constants.drageventid= txt.getText().toString();

            Log.e("Constants.drageventid", "" + Constants.drageventid);

            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }
    private boolean isLaLongAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }




    private void requestLatLong(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CAM_PERMISSION_CODE);
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
                Toast.makeText(getActivity(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
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





}
