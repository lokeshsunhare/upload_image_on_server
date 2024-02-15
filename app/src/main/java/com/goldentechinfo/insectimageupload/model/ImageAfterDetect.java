
package com.goldentechinfo.insectimageupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageAfterDetect {

    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("Accuracy")
    @Expose
    private String accuracy;
    @SerializedName("class_name")
    @Expose
    private String className;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
