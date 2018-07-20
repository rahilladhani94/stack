package com.hourstack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.hourstack.Adapters.AdpterChooseColor;
import com.hourstack.Model.NewProjectMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityCreateNewLabel extends AppCompatActivity {


    private TextView txt_back,txt_save;
    private GridView grid_view_color;
    public static ArrayList<String> color;
    private AdpterChooseColor adpterChooseColor;
    public static  String selected_color="";
    private EditText edt_labelname;
    private SessionManager sessionManager;
    private  String label ="",id="",name="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewlabel);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        selected_color="";
        sessionManager = new SessionManager(ActivityCreateNewLabel.this);
        idMapping();
        getData();
        setonClick();

    }


    //////////id mapping///////////
    private void idMapping()
    {
        txt_save = (TextView)findViewById(R.id.txt_save);
        txt_back= (TextView)findViewById(R.id.txt_back);
        grid_view_color = (GridView)findViewById(R.id.grid_view_color);
        edt_labelname = (EditText)findViewById(R.id.edt_labelname);
        color = new ArrayList<String>();
        color.add("74b427");
        color.add("ffae00");
        color.add("c12602");
        color.add("ce2a83");
        color.add("7f00d0");
        color.add("3424b1");
        color.add("0074c5");
        color.add("28c8c2");
        color.add("ffea00");
        color.add("bad138");
        color.add("b7b7b7");
        color.add("434343");


        adpterChooseColor = new AdpterChooseColor(ActivityCreateNewLabel.this,color,"label");
        grid_view_color.setAdapter(adpterChooseColor);

    }



    private void getData() {

        Intent intent = getIntent();
        Bundle extas = intent.getExtras();
        if(extas!= null){
            label=extas.getString("label");
            if(label.equalsIgnoreCase("new")){
                return;
            }
            else{
                id=extas.getString("id");
                name=extas.getString("name");
                selected_color=extas.getString("color");
                edt_labelname.setText(name);

            }

        }

    }


    ///////////seton click///////////////


    private void setonClick() {

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_labelname.getText().toString().trim().length() == 0) {

                    AppUtil.Toast(ActivityCreateNewLabel.this,"Please enter label name");
                    return;
                }
                if(selected_color.length()<1){
                    AppUtil.Toast(ActivityCreateNewLabel.this,"Please select color");
                    return;
                }
                if (!UIUtil.checkNetwork(ActivityCreateNewLabel.this)) {

                    UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityCreateNewLabel.this);
                    return;
                }
                if(label.equalsIgnoreCase("new")){
                    callApi();
                }
                else{
                    callUpdateApi();
                }


            }
        });
    }
    private void callApi() {
        UIUtil.hideKeyboard(ActivityCreateNewLabel.this);

        UIUtil.showDialog(ActivityCreateNewLabel.this);
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", edt_labelname.getText().toString().trim());
        map.put("color", selected_color);
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.newLabel(sessionManager.getKeyAuthtoken(),map);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null)
                    {
                        AppUtil.Toast(ActivityCreateNewLabel.this,"Label successfully created");
                        ActivityAllLabelsListing.refresh = "yes";
                        finish();
                    } else {
                        AppUtil.Toast(ActivityCreateNewLabel.this,"Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityCreateNewLabel.this,""+error);

                        AppUtil.Toast(ActivityCreateNewLabel.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityCreateNewLabel.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }




    //////update api///////////
    private void callUpdateApi() {
        UIUtil.hideKeyboard(ActivityCreateNewLabel.this);

        UIUtil.showDialog(ActivityCreateNewLabel.this);
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", edt_labelname.getText().toString().trim());
        map.put("color", selected_color);
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<NewProjectMainResponse> call = apiService.updateLabel(sessionManager.getKeyAuthtoken(),map,id);
        call.enqueue(new Callback<NewProjectMainResponse>() {
            @Override
            public void onResponse(Call<NewProjectMainResponse> call, Response<NewProjectMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    NewProjectMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null)
                    {
                        AppUtil.Toast(ActivityCreateNewLabel.this,"Label successfully updated");
                        ActivityAllLabelsListing.refresh = "yes";
                        finish();
                    } else {
                        AppUtil.Toast(ActivityCreateNewLabel.this,"Something went wrong,Please try again.");
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
                        // AppUtil.Toast(ActivityCreateNewLabel.this,""+error);

                        AppUtil.Toast(ActivityCreateNewLabel.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<NewProjectMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityCreateNewLabel.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

}
