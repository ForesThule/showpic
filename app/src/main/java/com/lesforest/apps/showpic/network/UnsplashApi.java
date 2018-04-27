package com.lesforest.apps.showpic.network;

import com.lesforest.apps.showpic.model.PicsumModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashApi {

    @GET("https://picsum.photos/{pth}")
    Single<String> universApi(@Path("pth") String pth);

    @POST("oauth/token")
    Single<AccessToken> token(
                              @Query("client_id") String client_id,
                              @Query("client_secret") String client_secret,
                              @Query("code") String code,
                              @Query("redirect_uri") String redirect_uri,
                              @Query("grant_type") String grant_type);
}
