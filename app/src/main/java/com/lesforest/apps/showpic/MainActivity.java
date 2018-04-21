package com.lesforest.apps.showpic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.rv_main)RecyclerView recyclerView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        MainApi api = ThisApp.getApi(this);

        api.recent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feed -> {
                    Timber.i("feed: %s",feed);
                });


    }
//
//    public void requestMultiplePermissions() {
//        ActivityCompat.requestPermissions(this,
//                new String[] {
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_SMS
//                },
//                PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 2) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showExtDirFilesCount();
//            }
//            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                showUnreadSmsCount();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
}
