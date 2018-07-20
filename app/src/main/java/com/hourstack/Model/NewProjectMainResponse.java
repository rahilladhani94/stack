package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rahil on 7/10/17.
 */

public class NewProjectMainResponse {
    @SerializedName("data")
    @Expose
    private NewProjectList data;

    public NewProjectList getData() {
        return data;
    }

    public void setData(NewProjectList data) {
        this.data = data;
    }
}
