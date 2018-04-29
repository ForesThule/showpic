package com.lesforest.apps.showpic.network;

import com.lesforest.apps.showpic.model.Feed;
import com.lesforest.apps.showpic.utils.Cv;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface  MainApi {

    @Headers({"Accept: application/json"})
    @GET(Cv.ENDPOINT+"/{pth}")
     Single<Feed> recent(@Path("pth") String pth);


    @Headers({"Accept: application/json"})
    @GET("podhistory")
    Call<String> podhistory();
}
