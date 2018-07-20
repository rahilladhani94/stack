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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hourstack.Model.LoginResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by abc on 20-02-2017.
 */
public class ActivitySignupCopy extends AppCompatActivity {

    private TextView txt_signup,txt_forgotpwd;
    Button btn_Signup;
    private EditText edt_name, edt_Email,edt_Password,edt_companyname;
    private LinearLayout ll_terms,ll_privacy;
    String timezone = "";
    ProgressBar progressBar;
    private CountDownTimer timer;
    int counter = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        idMapping();
        setonClick();




    }

    private void idMapping()
    {

        edt_name = (EditText)findViewById(R.id.edt_name) ;
        edt_Email = (EditText)findViewById(R.id.edt_Email) ;
        edt_Password = (EditText)findViewById(R.id.edt_Password) ;
        edt_companyname = (EditText)findViewById(R.id.edt_companyname) ;

        ll_terms = (LinearLayout)findViewById(R.id.ll_terms);
        ll_privacy = (LinearLayout)findViewById(R.id.ll_privacy);
        btn_Signup = (Button)findViewById(R.id.btn_Signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        try {

            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            timezone = tz.getDisplayName();
            Log.e("Time zone","="+tz.getDisplayName());


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    private void startProgrees() {
        timer = new CountDownTimer(1, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                try {
                    seprogress();
                    timer.start();
                } catch (Exception e) {
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
    private void endProgress() {
        timer.cancel();
        progressBar.setProgress(100);
    }
    private void seprogress() {
        counter++;
        if(counter>70){
            counter=0;
        }
        progressBar.setProgress(counter);
    }



    private void setonClick()
    {

        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hourstack.io/terms-of-service"));
                startActivity(intent);

            }
        });
        ll_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hourstack.io/privacy"));
                startActivity(intent);

            }
        });

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_name.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Please enter your Full Name");
                    return;
                }
                if (edt_Email.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Please enter your  Email Id");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        edt_Email.getText().toString()).matches()) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Please enter valid  Email Id");
                    return;

                }
                if (edt_Password.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Please enter Password");
                    return;
                }
                if (edt_Password.getText().toString().trim().length() < 6) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Password must be at least 6 of characters");
                    return;
                }
                if (edt_companyname.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivitySignupCopy.this,"Please enter Company Name");
                    return;
                }
                if (!UIUtil.checkNetwork(ActivitySignupCopy.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivitySignupCopy.this);
                    return;
                }
                callSignupAPI();

            }
        });

    }

    private void callSignupAPI() {

        UIUtil.hideKeyboard(ActivitySignupCopy.this);

        startProgrees();
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", edt_Email.getText().toString().trim());
        map.put("password", edt_Password.getText().toString().trim());
        map.put("email", edt_Email.getText().toString().trim());
        map.put("password", edt_Password.getText().toString().trim());
        map.put("workspace", edt_companyname.getText().toString().trim());
        map.put("timezone", edt_companyname.getText().toString().trim());


        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<LoginResponse> call = apiService.signup(map);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                endProgress();
                if (response.isSuccessful()) {
                    LoginResponse registrationResponse = response.body();
                    if (registrationResponse.getToken() != null) {

                        AppUtil.Toast(ActivitySignupCopy.this,"You are successfully sign up");
                        finish();

                    } else {
                        AppUtil.Toast(ActivitySignupCopy.this,""+registrationResponse.getError());
                    }
                } else {
                    if (response.code() == 500) {
                        InputStream in = response.errorBody().byteStream();
                        String json = AppUtil.getStringRequestBody(in);

                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                           JSONObject errorjsonObject=jsonObject.getJSONObject("errors");
                            Log.e("js",""+errorjsonObject);

                           String error =  errorjsonObject.getJSONArray("email").getString(0);

                            Log.e("error",""+error);
                            AppUtil.Toast(ActivitySignupCopy.this,""+error);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                AppUtil.Toast(ActivitySignupCopy.this,"Something went wrong,Please try again.");
                endProgress();
            }
        });
    }


}
