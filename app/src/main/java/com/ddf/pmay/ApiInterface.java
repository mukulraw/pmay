package com.ddf.pmay;

import com.ddf.pmay.loginPOJO.loginBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("pmay/api/mobile_signin.php")
    Call<loginBean> login
            (
                    @Part("phone") String phone,
                    @Part("password") String password,
                    @Part("lat") String lat,
                    @Part("lng") String lng
            );

    @Multipart
    @POST("pmay/api/signup.php")
    Call<String> signup
            (
                    @Part("name") String name,
                    @Part("phone") String phone,
                    @Part("password") String password,
                    @Part("area_id") String areaId,
                    @Part("lat") String lat,
                    @Part("lng") String lng
            );

    @Multipart
    @POST("pmay/api/peding_get.php")
    Call<List<pendingBean>> pendingList(
            @Part("uid") String userId,
            @Part("lat") String lat,
            @Part("lng") String lng
    );

    @Multipart
    @POST("pmay/api/notifications.php")
    Call<List<notificationBean>> notifications(
            @Part("uid") String userId,
            @Part("lat") String lat,
            @Part("lng") String lng
    );

    @GET("pmay/api/areasGet.php")
    Call<List<areasBean>> areas();

    @Multipart
    @POST("pmay/api/visited_get.php")
    Call<List<jobListBean>> visitedList(
            @Part("uid") String userId,
            @Part("lat") String lat,
            @Part("lng") String lng
    );

    @Multipart
    @POST("pmay/api/updateJob.php")
    Call<String> updateJob
            (
                    @Part("id") String id,
                    @Part("iid") String iid,
                    @Part("stage") String stage,
                    @Part("payment") String payment,
                    @Part("sd") String sd,
                    @Part("ed") String ed,
                    @Part("lat") String lat,
                    @Part("lng") String lng
            );

    @Multipart
    @POST("pmay/api/finishJob.php")
    Call<String> finishJob
            (
                    @Part("id") String id,
                    @Part("iid") String iid,
                    @Part("stage") String stage,
                    @Part("payment") String payment,
                    @Part("sd") String sd,
                    @Part("ed") String ed,
                    @Part("lat") String lat,
                    @Part("lng") String lng
            );


}
