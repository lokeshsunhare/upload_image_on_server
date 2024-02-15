
package com.goldentechinfo.insectimageupload.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("ImageAfterDetect")
    @Expose
    private List<ImageAfterDetect> imageAfterDetect;
    @SerializedName("AIResult")
    @Expose
    private List<AIResult> aIResult;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ImageAfterDetect> getImageAfterDetect() {
        return imageAfterDetect;
    }

    public void setImageAfterDetect(List<ImageAfterDetect> imageAfterDetect) {
        this.imageAfterDetect = imageAfterDetect;
    }

    public List<AIResult> getAIResult() {
        return aIResult;
    }

    public void setAIResult(List<AIResult> aIResult) {
        this.aIResult = aIResult;
    }

}
