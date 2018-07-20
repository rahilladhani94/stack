package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LabelsMainResponse {
    @SerializedName("data")
    @Expose
    private List<LabelsListResponse> data = null;

    public List<LabelsListResponse> getData() {
        return data;
    }

    public void setData(List<LabelsListResponse> data) {
        this.data = data;
    }

}
