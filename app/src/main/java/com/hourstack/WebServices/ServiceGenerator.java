package com.hourstack.WebServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hourstack.Model.EntriesMainResponse;
import com.hourstack.Model.LabelsMainResponse;
import com.hourstack.Model.LoginResponse;
import com.hourstack.Model.NewProjectMainResponse;
import com.hourstack.Model.ProjectsMainResponse;
import com.hourstack.Model.UserMainResponse;
import com.hourstack.Model.WorkspaceMainResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public class ServiceGenerator {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    private static Builder builder = new Builder().
            baseUrl("https://app.hourstack.io").
            addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return builder.client(new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(12000, TimeUnit.SECONDS)
                .writeTimeout(12000, TimeUnit.SECONDS)
                .readTimeout(12000, TimeUnit.SECONDS)
                .build())
                .build().create(serviceClass);
    }

    public interface ApiInterface {


        @POST("/jwt/authorize")
        Call<LoginResponse> login(@Body HashMap<String, Object> map);

        @POST("/jwt/signup")
        Call<LoginResponse> signup(@Body HashMap<String, Object> map);


        @GET("/api/internal/user")
        Call<UserMainResponse> userdata(@Header("Authorization") String sessionIdAndToken);


        @GET("/api/internal/projects")
        Call<ProjectsMainResponse> getAllProjects(@Header("Authorization") String sessionIdAndToken);

        @GET("/api/internal/labels")
        Call<LabelsMainResponse> getAllLabels(@Header("Authorization") String sessionIdAndToken);



        @POST("/api/internal/projects")
        Call<NewProjectMainResponse> newProject(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map);

        @PUT("/api/internal/projects/{id}")
        Call<NewProjectMainResponse> updateProject(@Header("Authorization") String sessionIdAndToken, @Body HashMap<String, Object> map, @Path("id") String id);

        @POST("/api/internal/labels")
        Call<NewProjectMainResponse> newLabel(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map);

        @PUT("/api/internal/labels/{id}")
        Call<NewProjectMainResponse> updateLabel(@Header("Authorization") String sessionIdAndToken, @Body HashMap<String, Object> map, @Path("id") String id);

        @GET("/api/internal/workspaces")
        Call<WorkspaceMainResponse> getAllWorkspace(@Header("Authorization") String sessionIdAndToken);


        @PUT("/api/internal/user")
        Call<UserMainResponse> updateWorkspace(@Header("Authorization") String sessionIdAndToken, @Body HashMap<String, Object> map);



        @GET("/api/internal/entries")
        Call<EntriesMainResponse> getEntries(@Header("Authorization") String sessionIdAndToken);

        @DELETE("/api/internal/entries/{id}")
        Call<NewProjectMainResponse> deleteEntry(@Header("Authorization") String sessionIdAndToken,@Path("id") String id);



        @POST("/api/internal/entries")
        Call<NewProjectMainResponse> newEntry(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map);



        @PUT("/api/internal/entries/{id}")
        Call<NewProjectMainResponse> editEntry(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map, @Path("id") String id);



        @POST("/api/internal/entries/rollover")
        Call<NewProjectMainResponse> rolloverTasks(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map);



        @POST("/api/internal/entries/stop")
        Call<NewProjectMainResponse> pauseTask(@Header("Authorization") String sessionIdAndToken);



        @PUT("/api/internal/workspaces/{id}/users/{id2}")
        Call<UserMainResponse> updateuser(@Header("Authorization") String sessionIdAndToken,@Body HashMap<String, Object> map, @Path("id") String id,@Path("id2") String id2);


    }
}
