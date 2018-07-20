package com.hourstack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hourstack.Adapters.AdpterAllProjects;
import com.hourstack.Home.BoardFragment;
import com.hourstack.Home.MainActivity;
import com.hourstack.Model.LoginResponse;
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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ActivityAllProjectsListing extends AppCompatActivity {

    private ImageView iv_add;
    private TextView txt_back;
    private ListView lv_projects;
    private SessionManager sessionManager;
    List<ProjectListResponse> projectListResponses;
    AdpterAllProjects adpterAllProjects;
    public static String refresh = "no";
    private String edit ="no";
    private LinearLayout ll_all;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectslisting);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sessionManager = new SessionManager(ActivityAllProjectsListing.this);
        idMapping();
        getData();
        setonClick();

        if (!UIUtil.checkNetwork(ActivityAllProjectsListing.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityAllProjectsListing.this);
            return;
        }
        getAllProjects();

    }

    private void getData() {

        Intent intent = getIntent();
        Bundle extas = intent.getExtras();
        if(extas!= null) {
            edit = extas.getString("edit");

        }
    }
    private void idMapping()
    {
        iv_add = (ImageView)findViewById(R.id.iv_add);
        txt_back= (TextView)findViewById(R.id.txt_back);
        lv_projects = (ListView)findViewById(R.id.lv_projects);
        ll_all =(LinearLayout)findViewById(R.id.ll_all);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(refresh.equalsIgnoreCase("yes")){
            getAllProjects();
        }

    }



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
                BoardFragment.referesh ="yes";
                Constants.project_selectedcolor="";
                Constants.project_selectedid="";
                Constants.project_selectedname="";

                Intent i = new Intent(ActivityAllProjectsListing.this,MainActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityAllProjectsListing.this,ActivityCreateNewProjects.class);
                i.putExtra("project","new");
                startActivity(i);
            }
        });

        lv_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                if (edit.equalsIgnoreCase("yes")){


                    Intent intent = new Intent();
                    intent.putExtra("project_selectedcolor", projectListResponses.get(position).getColor());
                    intent.putExtra("project_selectedname", projectListResponses.get(position).getName());
                    intent.putExtra("project_selectedid", ""+projectListResponses.get(position).getId());

                    setResult(RESULT_OK, intent);

                    finish();
                    return;
                }
                else if(edit.equalsIgnoreCase("create")){

                    Intent intent = new Intent();
                    intent.putExtra("project_selectedcolor", projectListResponses.get(position).getColor());
                    intent.putExtra("project_selectedname", projectListResponses.get(position).getName());
                    intent.putExtra("project_selectedid", ""+projectListResponses.get(position).getId());

                    setResult(RESULT_OK, intent);

                    finish();
                    return;
                }
                else {
                    BoardFragment.referesh ="yes";
                    Constants.project_selectedcolor=projectListResponses.get(position).getColor();
                    Constants.project_selectedname=projectListResponses.get(position).getName();
                    Constants.project_selectedid = ""+projectListResponses.get(position).getId();
                    finish();
                }




            }
        });

    }


    /////////all projects api////////////
    private void getAllProjects() {
        refresh = "no";
        UIUtil.hideKeyboard(ActivityAllProjectsListing.this);

        UIUtil.showDialog(ActivityAllProjectsListing.this);

        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<ProjectsMainResponse> call = apiService.getAllProjects(sessionManager.getKeyAuthtoken());
        call.enqueue(new Callback<ProjectsMainResponse>() {
            @Override
            public void onResponse(Call<ProjectsMainResponse> call, Response<ProjectsMainResponse> response) {

                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    ProjectsMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null && !registrationResponse.getData().isEmpty()) {


                        projectListResponses = registrationResponse.getData();
                        adpterAllProjects = new AdpterAllProjects(ActivityAllProjectsListing.this,projectListResponses,edit);
                        lv_projects.setAdapter(adpterAllProjects);
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

                        AppUtil.Toast(ActivityAllProjectsListing.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<ProjectsMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityAllProjectsListing.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }
}
