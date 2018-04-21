package com.lesforest.apps.showpic;

import com.lesforest.apps.showpic.model.Feed;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

interface  MainApi {

    @Headers({"Accept: application/json"})
    @GET("recent/updated;2012-07-12T14:59:24Z,567023,31779780/?limit=3")
    Single<Feed> recent();

    @Headers({"Accept: application/json"})
    @GET("podhistory")
    Call<String> podhistory();
}
