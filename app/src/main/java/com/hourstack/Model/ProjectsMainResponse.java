package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ProjectsMainResponse {
    @SerializedName("data")
    @Expose
    private List<ProjectListResponse> data = null;

    public List<ProjectListResponse> getData() {
        return data;
    }

    public void setData(List<ProjectListResponse> data) {
        this.data = data;
    }

}
