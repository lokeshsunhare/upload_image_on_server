
package com.goldentechinfo.insectimageupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AIResult {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("ClassName")
    @Expose
    private String className;
    @SerializedName("Accuracy")
    @Expose
    private String accuracy;
    @SerializedName("Message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
