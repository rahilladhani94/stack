package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class UserMainResponse {

    @SerializedName("data")
    @Expose
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
