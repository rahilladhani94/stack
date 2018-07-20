package com.hourstack.Activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hourstack.Home.MainActivity;
import com.hourstack.Model.LoginResponse;
import com.hourstack.Model.UserMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ActivityLogin extends AppCompatActivity {
    private Button btn_SignIn;
    private EditText edt_Email,edt_Password;
    private LinearLayout ll_forgot;
    private ProgressBar progressBar,progressBardefault;
    private CountDownTimer timer;
    private int counter = 0;
    private String authtoken = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        idMapping();
        setonClick();

    }

    private void idMapping()
    {

        edt_Email = (EditText)findViewById(R.id.edt_Email);
        edt_Password = (EditText)findViewById(R.id.edt_Password);
        btn_SignIn = (Button) findViewById(R.id.btn_SignIn);
        ll_forgot = (LinearLayout)findViewById(R.id.ll_forgot);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBardefault = (ProgressBar) findViewById(R.id.progressBardefault);
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);
    }
    private void startProgrees() {

        progressBar.setVisibility(View.VISIBLE);
        progressBardefault.setVisibility(View.GONE);

    }
    private void endProgress() {
        progressBar.setVisibility(View.GONE);
        progressBardefault.setVisibility(View.VISIBLE);

    }




    private void setonClick()
    {


        ll_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.hourstack.io/password/email"));
                startActivity(intent);

            }
        });

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_Email.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityLogin.this,"Please enter your  Email Id");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        edt_Email.getText().toString()).matches()) {

                    AppUtil.Toast(ActivityLogin.this,"Please enter valid  Email Id");
                    return;

                }
                if (edt_Password.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityLogin.this,"Please enter Password");

                    return;
                }
                if (!UIUtil.checkNetwork(ActivityLogin.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityLogin.this);
                    return;
                }
                callLoginAPI();
                
            }
        });
        
    }

    private void callLoginAPI() {
        UIUtil.hideKeyboard(ActivityLogin.this);

        startProgrees();
        HashMap<String, Object> map = new HashMap<>();

        map.put("email", edt_Email.getText().toString().trim());
        map.put("password", edt_Password.getText().toString().trim());
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<LoginResponse> call = apiService.login(map);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                endProgress();
                if (response.isSuccessful()) {
                    LoginResponse registrationResponse = response.body();
                    if (registrationResponse.getToken() != null) {
                        authtoken ="Bearer " + "" +registrationResponse.getToken();
                        getUserData();
//                        AppUtil.Toast(ActivityLogin.this,"You are successfully logged in");
//
//                        Intent i = new Intent(ActivityLogin.this,ActivityHomeScreen.class);
//
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);


                    } else {
                        AppUtil.Toast(ActivityLogin.this,""+registrationResponse.getError());
                    }
                } else {
                    if (response.code() == 401) {
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

                        AppUtil.Toast(ActivityLogin.this,"Invalid credentials.Please try again with valid credentials.");

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityLogin.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getUserData()
    {
        UIUtil.hideKeyboard(ActivityLogin.this);

        startProgrees();

        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<UserMainResponse> call = apiService.userdata(authtoken);
        call.enqueue(new Callback<UserMainResponse>() {
            @Override
            public void onResponse(Call<UserMainResponse> call, Response<UserMainResponse> response) {
                endProgress();
                if (response.isSuccessful()) {
                    UserMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null)
                    {

                        SessionManager sessionManager = new SessionManager(ActivityLogin.this);
                        sessionManager.setKeyAuthtoken(authtoken);
                        sessionManager.setKeyUserId(""+registrationResponse.getData().getId());
                        sessionManager.setKeyName(""+registrationResponse.getData().getName());
                        sessionManager.setKeyEmail(""+registrationResponse.getData().getEmail());
                        sessionManager.setKeyCurrentWorkspace(""+registrationResponse.getData().getLastWorkspaceId());
                        sessionManager.setKeyFirstday(""+registrationResponse.getData().getHoursInDay1());
                        sessionManager.setKeySecondday(""+registrationResponse.getData().getHoursInDay2());
                        sessionManager.setKeyThirdday(""+registrationResponse.getData().getHoursInDay3());
                        sessionManager.setKeyFourthday(""+registrationResponse.getData().getHoursInDay4());
                        sessionManager.setKeyFifthday(""+registrationResponse.getData().getHoursInDay5());
                        sessionManager.setKeySixthtday(""+registrationResponse.getData().getHoursInDay6());
                        sessionManager.setKeySeventhday(""+registrationResponse.getData().getHoursInDay7());
                        sessionManager.setKeyDefaultallocation(""+registrationResponse.getData().getDefaultAllocatedSeconds());

                        sessionManager.setKeyProximity("yes");
                        sessionManager.setKeyTimerreminder("yes");
                        sessionManager.setKeyTimerremindertime("900");
                        sessionManager.setKeyOverallocation("yes");

                        AppUtil.Toast(ActivityLogin.this,"You are successfully logged in");
                        Intent i = new Intent(ActivityLogin.this,MainActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);


                    } else {
                        AppUtil.Toast(ActivityLogin.this,"Something went wrong,Please try again.");
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

                        AppUtil.Toast(ActivityLogin.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserMainResponse> call, Throwable t) {
                endProgress();
                AppUtil.Toast(ActivityLogin.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }


}
