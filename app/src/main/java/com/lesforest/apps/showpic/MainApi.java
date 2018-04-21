package com.lesforest.apps.showpic;

import com.lesforest.apps.showpic.model.Feed;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

interface  MainApi {

    @Headers({"Accept: application/json"})
//    @GET("recent/updated;2017-07-12T14:59:24Z,567023,31779780/")
    @GET("recent/updated;2017-07-12T14:59:24Z,1000,10/?limit=50")
    Single<Feed> recent();

    @Headers({"Accept: application/json"})
    @GET("podhistory")
    Call<String> podhistory();
}
