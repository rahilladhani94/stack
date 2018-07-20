package com.hourstack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hourstack.Adapters.AdpterAllLabels;
import com.hourstack.Adapters.AdpterAllWorkspace;
import com.hourstack.Model.LabelsListResponse;
import com.hourstack.Model.LabelsMainResponse;
import com.hourstack.Model.UserMainResponse;
import com.hourstack.Model.WorkspaceListResponse;
import com.hourstack.Model.WorkspaceMainResponse;
import com.hourstack.R;
import com.hourstack.Utils.AppUtil;
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



public class ActivityWorkspaceListing extends AppCompatActivity {

    private  ImageView iv_add;
    private TextView txt_back;
    private AdpterAllWorkspace adpterAllWorkspace;
    private SessionManager sessionManager;
    private List<WorkspaceListResponse> allWorkspaceListResponses;
    private ListView lv_workspaces;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspacelisting);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(ActivityWorkspaceListing.this);
        idMapping();
        setonClick();
        if (!UIUtil.checkNetwork(ActivityWorkspaceListing.this)) {

            UIUtil.showCustomDialog("Alert", "There seems to be some problem with your internet connection. Please check.", ActivityWorkspaceListing.this);
            return;
        }
        getAllWorkspaces();
    }

    private void idMapping()
    {

        txt_back= (TextView)findViewById(R.id.txt_back);
        lv_workspaces = (ListView)findViewById(R.id.lv_workspaces);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }



    private void setonClick() {

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        lv_workspaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calApi(allWorkspaceListResponses.get(position).getId());

            }
        });


    }

    private void calApi(Integer id)
    {
        UIUtil.showDialog(ActivityWorkspaceListing.this);


        HashMap<String, Object> map = new HashMap<>();

        map.put("last_workspace_id", ""+id);


        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<UserMainResponse> call = apiService.updateWorkspace(sessionManager.getKeyAuthtoken(),map);

        call.enqueue(new Callback<UserMainResponse>() {
            @Override
            public void onResponse(Call<UserMainResponse> call, Response<UserMainResponse> response) {

                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    UserMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null)
                    {

                        SessionManager sessionManager = new SessionManager(ActivityWorkspaceListing.this);

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



                        AppUtil.Toast(ActivityWorkspaceListing.this,"Workspace changed successfully.");
                        finish();

                    } else {
                        AppUtil.Toast(ActivityWorkspaceListing.this,"Something went wrong,Please try again.");
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

                        AppUtil.Toast(ActivityWorkspaceListing.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityWorkspaceListing.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getAllWorkspaces() {
        UIUtil.showDialog(ActivityWorkspaceListing.this);
        ServiceGenerator.ApiInterface apiService =
                ServiceGenerator.createService(ServiceGenerator.ApiInterface.class);
        Call<WorkspaceMainResponse> call = apiService.getAllWorkspace(sessionManager.getKeyAuthtoken());
        call.enqueue(new Callback<WorkspaceMainResponse>() {
            @Override
            public void onResponse(Call<WorkspaceMainResponse> call, Response<WorkspaceMainResponse> response) {
                UIUtil.dismissDialog();
                if (response.isSuccessful()) {
                    WorkspaceMainResponse registrationResponse = response.body();
                    if (registrationResponse.getData() != null && !registrationResponse.getData().isEmpty()) {


                        allWorkspaceListResponses = registrationResponse.getData();
                        adpterAllWorkspace = new AdpterAllWorkspace(ActivityWorkspaceListing.this,allWorkspaceListResponses);
                        lv_workspaces.setAdapter(adpterAllWorkspace);
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
                        // AppUtil.Toast(ActivityWorkspaceListing.this,""+error);

                        AppUtil.Toast(ActivityWorkspaceListing.this,"Your session has expired please re login again to continue.");

                    }
                }
            }

            @Override
            public void onFailure(Call<WorkspaceMainResponse> call, Throwable t) {
                UIUtil.dismissDialog();
                AppUtil.Toast(ActivityWorkspaceListing.this,"Something went wrong,Please try again.");
                Log.e("TAG", t.toString());
            }
        });
    }

}
