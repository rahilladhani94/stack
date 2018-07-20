package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EntriesMainResponse {
    @SerializedName("data")
    @Expose
    private List<EntriesListResponse> data = null;

    public List<EntriesListResponse> getData() {
        return data;
    }

    public void setData(List<EntriesListResponse> data) {
        this.data = data;
    }
}
