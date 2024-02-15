package com.goldentechinfo.insectimageupload;

import com.goldentechinfo.insectimageupload.model.InsectRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InsectApi {

    @Multipart
    @POST("AIImageDetection")
//    @POST("uploadImageTest")
    Call<InsectRes> UploadInsect1(
            @Part("mobile_number") RequestBody mobile_number,
            @Part("farmer_name") RequestBody farmer_name,
            @Part("farmer_id") RequestBody farmer_id,
            @Part("state_id") RequestBody state_id,
            @Part("block_id") RequestBody block_id,
            @Part("district_id") RequestBody district_id,
            @Part("insertFrom") RequestBody insertFrom,
            @Part MultipartBody.Part files1);


    @Multipart
    @POST("AIImageDetection")
    Call<ResponseBody> UploadInsect2(
            @Part("mobile_number") RequestBody mobile_number,
            @Part("farmer_name") RequestBody farmer_name,
            @Part("farmer_id") RequestBody farmer_id,
            @Part("state_id") RequestBody state_id,
            @Part("block_id") RequestBody block_id,
            @Part("district_id") RequestBody district_id,
            @Part("insertFrom") RequestBody insertFrom,
            @Part MultipartBody.Part files1,
            @Part MultipartBody.Part files2);

    @Multipart
    @POST("AIImageDetection")
    Call<ResponseBody> UploadInsect3(
            @Part("mobile_number") RequestBody mobile_number,
            @Part("farmer_name") RequestBody farmer_name,
            @Part("farmer_id") RequestBody farmer_id,
            @Part("state_id") RequestBody state_id,
            @Part("block_id") RequestBody block_id,
            @Part("district_id") RequestBody district_id,
            @Part("insertFrom") RequestBody insertFrom,
            @Part MultipartBody.Part files1,
            @Part MultipartBody.Part files2,
            @Part MultipartBody.Part files3);
//            @Part List<MultipartBody.Part> files);


}

