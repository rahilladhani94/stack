package com.hourstack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hourstack.Adapters.AdpterAllLabels;
import com.hourstack.Adapters.AdpterAllProjects;
import com.hourstack.Home.BoardFragment;
import com.hourstack.Home.MainActivity;
import com.hourstack.Model.LabelsListResponse;
import com.hourstack.Model.LabelsMainResponse;
import com.hourstack.Model.ProjectListResponse;
import com.hourstack.Model.ProjectsMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
import com.hourstack.Utils.Constants;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.WebServices.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAllLabelsListing extends AppCompatActivity {

    private ImageView iv_add;
    private TextView txt_back;
    private AdpterAllLabels adpterAllLabels;
    private SessionManager sessionManager;
    private List<LabelsListResponse> allLabelsListings;
    ListView lv_labels;
    public static String refresh = "no";
    private String edit = "no";
    private LinearLayout ll_all;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labelslisting);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivityAllLabelsListing.this);
        idMapping();
        getData();
        setonClick();
        if (!UIUtil.checkNetwork(ActivityAllLabelsListing.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityAllLabelsListing.this);
            return;
        }
        getAllLabels();
    }

    private void getData() {

        Intent intent = getIntent();
        Bundle extas = intent.getExtras();
        if (extas != null) {
            edit = extas.getString("edit");

        }
    }


    ////////id mapping///////////
    private void idMapping() {
        iv_add = (ImageView) findViewById(R.id.iv_add);
        txt_back = (TextView) findViewById(R.id.txt_back);
        lv_labels = (ListView) findViewById(R.id.lv_labels);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (refresh.equalsIgnoreCase("yes")) {
            getAllLabels();
        }

    }


    ////////seon click////////////
    private void setonClick() {

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardFragment.referesh = "yes";
                Constants.label_selectedcolor = "";
                Constants.label_selectedid = "";
                Constants.label_selectedname = "";

                Intent i = new Intent(ActivityAllLabelsListing.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityAllLabelsListing.this, ActivityCreateNewLabel.class);
                i.putExtra("label", "new");
                startActivity(i);
            }
        });

        lv_labels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (edit.equalsIgnoreCase("yes")) {

                    Intent intent = new Intent();

                    intent.putExtra("label_selectedcolor", allLabelsListings.get(position).getColor());
                    intent.putExtra("label_selectedname", allLabelsListings.get(position).getName());
                    intent.putExtra("label_selectedid",""+ allLabelsListings.get(position).getId());

                    setResult(RESULT_OK, intent);

                    finish();
                    return;
                } else if (edit.equalsIgnoreCase("create")) {
                    Intent intent = new Intent();

                    intent.putExtra("label_selectedcolor", allLabelsListings.get(position).getColor());
                    intent.putExtra("label_selectedname", allLabelsListings.get(position).getName());
                    intent.putExtra("label_selectedid",""+ allLabelsListings.get(position).getId());

                    setResult(RESULT_OK, intent);

                    finish();

                    return;
                } else {
                    BoardFragment.referesh = "yes";
                    Constants.label_selectedcolor = allLabelsListings.get(position).getColor();
                    Constants.label_selectedname = allLabelsListings.get(position).getName();
                    Constants.label_selectedid = "" + allLabelsListings.get(position).getId();
                    finish();
                }


            }
        });


    }


    /////////all labels api/////////

    private void getAllLabels() {
        refresh = "no";
        UIUtil.hideKeyboard(ActivityAllLabelsListing.this);

        UIUtil.showDialog(ActivityAllLabelsListing.this);


        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<LabelsMainResponse> call = apiService.getAllLabels(sessionManager.getKeyAuthtoken());
        call.enqueue(new Callback<LabelsMainResponse>() {
            @Override
            public void onResponse(Call<LabelsMainResponse> call, Response<LabelsMainResponse> response) {

                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    LabelsMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null && !registrationResponse.getData().isEmpty()) {


                        allLabelsListings = registrationResponse.getData();
                        adpterAllLabels = new AdpterAllLabels(ActivityAllLabelsListing.this, allLabelsListings, edit);
                        lv_labels.setAdapter(adpterAllLabels);
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

                        AppUtil.Toast(ActivityAllLabelsListing.this, "Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<LabelsMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityAllLabelsListing.this, "Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

}
