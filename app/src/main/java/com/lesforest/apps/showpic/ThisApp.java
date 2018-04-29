package com.lesforest.apps.showpic;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.lesforest.apps.showpic.network.AccessToken;
import com.lesforest.apps.showpic.network.MainApi;
import com.lesforest.apps.showpic.network.UnsplashApi;
import com.lesforest.apps.showpic.utils.Cv;
import com.lesforest.apps.showpic.utils.Helpers;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by vladimir on 08.06.17.
 */

public class ThisApp extends Application {

    public static MainApi api;
    public  UnsplashApi unsplashApi;
    private Picasso picasso;
    public AccessToken authToken;

    public static ThisApp get(Context ctx) {
        return (ThisApp) ctx.getApplicationContext();
    }

    public Picasso getPicasso() {
        return picasso;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        api = Helpers.createApi(this);
//        unsplashApi = Helpers.createUnsplashApi(this);


        if (BuildConfig.DEBUG) {

            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) +
                            ":timber: line=" + element.getLineNumber() +
                            " method: " + element.getMethodName();
                }
            });
        }

        createPicasso();

    }

    private void createPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        OkHttp3Downloader downloader = new OkHttp3Downloader(this, Integer.MAX_VALUE);
        builder.downloader(downloader);
        picasso = builder.build();
//        picasso.setIndicatorsEnabled(true);
//        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }

    public static MainApi getApi(Context ctx) {
        if (null == api) {
            api = Helpers.createApi(ctx);
        }
        return api;    }


}
