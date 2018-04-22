package com.lesforest.apps.showpic;

import android.app.Application;
import android.content.Context;

import com.lesforest.apps.showpic.network.MainApi;
import com.lesforest.apps.showpic.utils.Helpers;

import timber.log.Timber;

/**
 * Created by vladimir on 08.06.17.
 */

public class ThisApp extends Application {

    public static MainApi api;


    private String currentUser;
    private String currentUserDisplayName;
    private String currentPwd;

    private int currentRouteIndex;


    private boolean testMode;
    private String currentDefSiteId;
    private int currentPprIndex;

    public static ThisApp get(Context ctx) {
        return (ThisApp) ctx.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        api = Helpers.createApi(this);

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
    }

    public static MainApi getApi(Context ctx) {
        if (null == api) {
            api = Helpers.createApi(ctx);
        }
        return api;    }

    public int getCurrentRouteIndex() {
        return currentRouteIndex;
    }

    public void setCurrentRouteIndex(int currentRouteIndex) {
        this.currentRouteIndex = currentRouteIndex;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserDisplayName() {
        return currentUserDisplayName;
    }

    public String getCurrentPwd() {
        return currentPwd;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentUserDisplayName(String currentUserDisplayName) {
        this.currentUserDisplayName = currentUserDisplayName;
    }

    public void setCurrentPwd(String currentPwd) {
        this.currentPwd = currentPwd;
    }

    public void refreshMaximoApi() {
        api = Helpers.createApi(this);
    }

    public String getCurrentDefSiteId() {
        return currentDefSiteId;
    }

    public void setCurrentDefSiteId(String currentDefSiteId) {
        this.currentDefSiteId = currentDefSiteId;
    }

    public int getCurrentPprIndex() {
        return currentPprIndex;
    }

    public void setCurrentPprIndex(int currentPprIndex) {
        this.currentPprIndex = currentPprIndex;
    }
}
