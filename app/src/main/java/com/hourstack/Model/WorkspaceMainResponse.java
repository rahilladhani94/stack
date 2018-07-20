package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class WorkspaceMainResponse {
    @SerializedName("data")
    @Expose
    private List<WorkspaceListResponse> data = null;

    public List<WorkspaceListResponse> getData() {
        return data;
    }

    public void setData(List<WorkspaceListResponse> data) {
        this.data = data;
    }

}
