package com.lesforest.apps.showpic.network;

import com.lesforest.apps.showpic.model.Feed;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface  MainApi {

    @Headers({"Accept: application/json"})
//    @GET("recent/updated;2017-07-12T14:59:24Z,567023,31779780/")
    @GET("recent/{p}")
     Single<Feed> recent(@Path("p") String p);


    @Headers({"Accept: application/json"})
    @GET("podhistory")
    Call<String> podhistory();
}
