package com.hourstack.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hourstack.Model.UserMainResponse;
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
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivitySettingScreen extends AppCompatActivity {
   private EditText edt_name, edt_email, edt_Password;
    private SessionManager sessionManager;
    private TextView txt_save, txt_back,txt_version;
    private  EditText edt_first, edt_second, edt_third, edt_fouth, edt_fifth, edt_sixth, edt_seventh, edt_default;
    private ToggleButton tog_mon, tog_tue, tog_wed, tog_thu, tog_fri, tog_sat, tog_sun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivitySettingScreen.this);
        idMapping();

        setonClick();

    }



    ////////////ID MAPPING/////////////////
    private void idMapping() {


        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_Password = (EditText) findViewById(R.id.edt_Password);
        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_back = (TextView) findViewById(R.id.txt_back);
        edt_first = (EditText) findViewById(R.id.edt_first);
        edt_second = (EditText) findViewById(R.id.edt_second);
        edt_third = (EditText) findViewById(R.id.edt_third);
        edt_fouth = (EditText) findViewById(R.id.edt_fourth);
        edt_fifth = (EditText) findViewById(R.id.edt_fifth);
        edt_sixth = (EditText) findViewById(R.id.edt_sixth);
        edt_seventh = (EditText) findViewById(R.id.edt_seventh);
        edt_default = (EditText) findViewById(R.id.edt_default);

        tog_mon = (ToggleButton) findViewById(R.id.tog_mon);
        tog_tue = (ToggleButton) findViewById(R.id.tog_tue);
        tog_wed = (ToggleButton) findViewById(R.id.tog_wed);
        tog_thu = (ToggleButton) findViewById(R.id.tog_thu);
        tog_fri = (ToggleButton) findViewById(R.id.tog_fri);
        tog_sat = (ToggleButton) findViewById(R.id.tog_sat);
        tog_sun = (ToggleButton) findViewById(R.id.tog_sun);

        setData();

    }

    private void setData() {
        edt_name.setText(sessionManager.getKeyName());
        edt_email.setText(sessionManager.getKeyEmail());
        if (sessionManager.getKeyFirstday() != null) {

            if (!sessionManager.getKeyFirstday().equalsIgnoreCase("0")) {
                edt_first.setText(sessionManager.getKeyFirstday());
                tog_mon.setChecked(true);
            } else {
                edt_first.setText("-");
                tog_mon.setChecked(false);
            }
        }
        if (sessionManager.getKeySecondday() != null) {

            if (!sessionManager.getKeySecondday().equalsIgnoreCase("0")) {
                edt_second.setText(sessionManager.getKeySecondday());
                tog_tue.setChecked(true);
            } else {
                edt_second.setText("-");
                tog_tue.setChecked(false);
            }
        }
        if (sessionManager.getKeyThirdday() != null) {

            if (!sessionManager.getKeyThirdday().equalsIgnoreCase("0")) {
                edt_third.setText(sessionManager.getKeyThirdday());
                tog_wed.setChecked(true);
            } else {
                edt_third.setText("-");
                tog_wed.setChecked(false);
            }
        }
        if (sessionManager.getKeyFourthday() != null) {

            if (!sessionManager.getKeyFourthday().equalsIgnoreCase("0")) {
                edt_fouth.setText(sessionManager.getKeyFourthday());
                tog_thu.setChecked(true);
            } else {
                edt_fouth.setText("-");
                tog_thu.setChecked(false);
            }
        }
        if (sessionManager.getKeyFifthday() != null) {

            if (!sessionManager.getKeyFifthday().equalsIgnoreCase("0")) {

                edt_fifth.setText(sessionManager.getKeyFifthday());
                tog_fri.setChecked(true);
            } else {
                edt_fifth.setText("-");
                tog_fri.setChecked(false);
            }
        }
        if (sessionManager.getKeySixthtday() != null) {

            if (!sessionManager.getKeySixthtday().equalsIgnoreCase("0")) {
                edt_sixth.setText(sessionManager.getKeySixthtday());
                tog_sat.setChecked(true);
            } else {
                edt_sixth.setText("-");
                tog_sat.setChecked(false);
            }
        }
        if (sessionManager.getKeySeventhday() != null) {

            if (!sessionManager.getKeySeventhday().equalsIgnoreCase("0")) {
                edt_seventh.setText(sessionManager.getKeySeventhday());
                tog_sun.setChecked(true);
            } else {
                edt_seventh.setText("-");
                tog_sun.setChecked(false);
            }
        }


        String defaulthours = "";
        defaulthours = UIUtil.convertDateTime("ss", "HH:mm", sessionManager.getKeyDefaultallocation());
        edt_default.setText("" + defaulthours);
//        try {
//            double defaultseconds = Double.parseDouble(sessionManager.getKeyDefaultallocation());
//
//            double hours = defaultseconds/3600;
//            edt_default.setText(""+hours);
//        }
//        catch (Exception e){
//
//        }

    }




    //////////SETON CLICK//////////////
    private void setonClick() {
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                sessionManager.setKeyCurrentseconds("");
//                startService(new Intent(ActivitySettingScreen.this, service.class));

                if (!UIUtil.checkNetwork(ActivitySettingScreen.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivitySettingScreen.this);
                    return;
                }
                if (edt_Password.getText().toString().trim().length() > 0) {
                    if (edt_Password.getText().toString().trim().length() < 6) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Password must be at least 6 of characters");
                        return;
                    }
                }


                if (edt_name.getText().toString().trim().length() == 0) {
                    AppUtil.Toast(ActivitySettingScreen.this, "Please enter your name");
                    return;
                }
                if (edt_email.getText().toString().trim().length()==0) {
                    AppUtil.Toast(ActivitySettingScreen.this, "Please enter your  Email Id");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        edt_email.getText().toString()).matches()) {

                    AppUtil.Toast(ActivitySettingScreen.this, "Please enter valid  Email Id");
                    return;
                }




                if (edt_default.getText().toString().trim().length() == 0 || edt_default.getText().toString().trim().equalsIgnoreCase("-")) {
                    AppUtil.Toast(ActivitySettingScreen.this, "Enter default time");

                    return;
                }
                if (tog_mon.isChecked()) {
                    if (edt_first.getText().toString().trim().length() == 0 | edt_first.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Monday");

                        return;
                    }
                }
                if (tog_tue.isChecked()) {
                    if (edt_second.getText().toString().trim().length() == 0 | edt_second.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Tuesday");

                        return;
                    }
                }
                if (tog_wed.isChecked()) {
                    if (edt_third.getText().toString().trim().length() == 0 | edt_third.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Wednesday");

                        return;
                    }
                }
                if (tog_thu.isChecked()) {
                    if (edt_fouth.getText().toString().trim().length() == 0 | edt_fouth.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Thursday");

                        return;
                    }
                }
                if (tog_fri.isChecked()) {
                    if (edt_fifth.getText().toString().trim().length() == 0 | edt_fifth.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Friday");

                        return;
                    }
                }
                if (tog_sat.isChecked()) {
                    if (edt_sixth.getText().toString().trim().length() == 0 | edt_sixth.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Saturday");

                        return;
                    }
                }
                if (tog_sun.isChecked()) {
                    if (edt_seventh.getText().toString().trim().length() == 0 | edt_seventh.getText().toString().trim().equalsIgnoreCase("-")) {
                        AppUtil.Toast(ActivitySettingScreen.this, "Enter time for Sunday");

                        return;
                    }
                }


                changepasswordAPI();
            }
        });
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent myService = new Intent(ActivitySettingScreen.this, service.class);
//                stopService(myService);


                finish();
            }
        });

        tog_mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_mon.isChecked()) {
                    edt_first.setText("-");
                } else {
                    edt_first.setText("");
                }
            }
        });
        tog_tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_tue.isChecked()) {
                    edt_second.setText("-");
                } else {
                    edt_second.setText("");
                }
            }
        });
        tog_wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_wed.isChecked()) {
                    edt_third.setText("-");
                } else {
                    edt_third.setText("");
                }
            }
        });
        tog_thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_thu.isChecked()) {
                    edt_fouth.setText("-");
                } else {
                    edt_fouth.setText("");
                }
            }
        });
        tog_fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_fri.isChecked()) {
                    edt_fifth.setText("-");
                } else {
                    edt_fifth.setText("");
                }
            }
        });
        tog_sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_sat.isChecked()) {
                    edt_sixth.setText("-");
                } else {
                    edt_sixth.setText("");
                }
            }
        });
        tog_sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tog_sun.isChecked()) {
                    edt_seventh.setText("-");
                } else {
                    edt_seventh.setText("");
                }
            }
        });
    }

    private void callUpdateUser() {


        HashMap<String, Object> map = new HashMap<>();


        if (tog_mon.isChecked()) {
            if (edt_first.getText().toString().trim().length() > 0 && !edt_first.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day1", "" + edt_first.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day1", "-");

        }
        if (tog_tue.isChecked()) {
            if (edt_second.getText().toString().trim().length() > 0 && !edt_second.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day2", "" + edt_second.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day2", "-");

        }
        if (tog_wed.isChecked()) {
            if (edt_third.getText().toString().trim().length() > 0 && !edt_third.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day3", "" + edt_third.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day3", "-");

        }
        if (tog_thu.isChecked()) {
            if (edt_fouth.getText().toString().trim().length() > 0 && !edt_fouth.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day4", "" + edt_fouth.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day4", "-");

        }
        if (tog_fri.isChecked()) {
            if (edt_fifth.getText().toString().trim().length() > 0 && !edt_fifth.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day5", "" + edt_fifth.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day5", "-");

        }
        if (tog_sat.isChecked()) {
            if (edt_sixth.getText().toString().trim().length() > 0 && !edt_sixth.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day6", "" + edt_sixth.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day6", "-");

        }
        if (tog_sun.isChecked()) {
            if (edt_seventh.getText().toString().trim().length() > 0 && !edt_seventh.getText().toString().trim().equalsIgnoreCase("-")) {
                map.put("hours_in_day7", "" + edt_seventh.getText().toString().trim());
            }
        } else {
            map.put("hours_in_day7", "-");

        }

        if (edt_default.getText().toString().trim().length() > 0 && !edt_default.getText().toString().trim().equalsIgnoreCase("-")) {
            map.put("default_allocated_seconds", "" + edt_default.getText().toString().trim());
        }


//        map.put("permission_workspace", "0");
//        map.put("permission_workspace_users", "0");
//        map.put("permission_workspace_entries", "0");
//        map.put("permission_workspace_view", "0");
//        map.put("permission_own_entries", "0");
//        map.put("permission_past_entries", "0");
//
//        map.put("permission_templates", "0");
//
//        map.put("permission_projects", "0");
//
//        map.put("permission_labels", "0");
//
//        map.put("permission_teams", "0");
//
//        map.put("permission_reports", "0");
        Log.e("map", "" + map.toString());

        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<UserMainResponse> call = apiService.updateuser(sessionManager.getKeyAuthtoken(), map, sessionManager.getKeyCurrentWorkspace(), sessionManager.getUserId());

        call.enqueue(new Callback<UserMainResponse>() {
            @Override
            public void onResponse(Call<UserMainResponse> call, Response<UserMainResponse> response) {

                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    UserMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {

                        SessionManager sessionManager = new SessionManager(ActivitySettingScreen.this);

                        sessionManager.setKeyUserId("" + registrationResponse.getData().getId());
                        sessionManager.setKeyName("" + registrationResponse.getData().getName());
                        sessionManager.setKeyEmail("" + registrationResponse.getData().getEmail());
                        sessionManager.setKeyCurrentWorkspace("" + registrationResponse.getData().getLastWorkspaceId());
                        sessionManager.setKeyFirstday("" + registrationResponse.getData().getHoursInDay1());
                        sessionManager.setKeySecondday("" + registrationResponse.getData().getHoursInDay2());
                        sessionManager.setKeyThirdday("" + registrationResponse.getData().getHoursInDay3());
                        sessionManager.setKeyFourthday("" + registrationResponse.getData().getHoursInDay4());
                        sessionManager.setKeyFifthday("" + registrationResponse.getData().getHoursInDay5());
                        sessionManager.setKeySixthtday("" + registrationResponse.getData().getHoursInDay6());
                        sessionManager.setKeySeventhday("" + registrationResponse.getData().getHoursInDay7());
                        sessionManager.setKeyDefaultallocation("" + registrationResponse.getData().getDefaultAllocatedSeconds());


                        finish();

                    } else {
                        AppUtil.Toast(ActivitySettingScreen.this, "Something went wrong,Please try again.");
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

                        AppUtil.Toast(ActivitySettingScreen.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivitySettingScreen.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


    private void changepasswordAPI() {

        HashMap<String, Object> map = new HashMap<>();

        if (edt_Password.getText().toString().trim().length() > 0) {

            if (edt_Password.getText().toString().trim().length() < 6) {
                AppUtil.Toast(ActivitySettingScreen.this, "Password must be at least 6 of characters");
                return;
            }
            map.put("password", "" + edt_Password.getText().toString().trim());

        }

        map.put("name", "" + edt_name.getText().toString().trim());
        map.put("email", "" + edt_email.getText().toString().trim());


        UIUtil.showDialog(ActivitySettingScreen.this);
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<UserMainResponse> call = apiService.updateWorkspace(sessionManager.getKeyAuthtoken(), map);

        call.enqueue(new Callback<UserMainResponse>() {
            @Override
            public void onResponse(Call<UserMainResponse> call, Response<UserMainResponse> response) {



                if (response.isSuccessful()) {
                    UserMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null) {

                        callUpdateUser();

                    } else {
                        AppUtil.Toast(ActivitySettingScreen.this, "Something went wrong,Please try again.");
                    }
                } else {
                    UIUtil.dismissDialog();
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

                        AppUtil.Toast(ActivitySettingScreen.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivitySettingScreen.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }
}



