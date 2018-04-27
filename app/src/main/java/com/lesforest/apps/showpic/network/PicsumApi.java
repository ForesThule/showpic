package com.lesforest.apps.showpic.network;

import com.lesforest.apps.showpic.model.Feed;
import com.lesforest.apps.showpic.model.PicsumModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PicsumApi {

    @GET("https://picsum.photos/{pth}")
    Single<String> universApi(@Path("pth") String pth);

    @GET("list")
    Single<List<PicsumModel>> list();
}
