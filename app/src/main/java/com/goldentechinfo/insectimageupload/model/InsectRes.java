
package com.goldentechinfo.insectimageupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsectRes {

    @SerializedName("Response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
