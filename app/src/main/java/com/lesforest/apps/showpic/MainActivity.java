package com.lesforest.apps.showpic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lesforest.apps.showpic.model.Entry;
import com.lesforest.apps.showpic.model.Feed;
import com.lesforest.apps.showpic.model.Img;
import com.lesforest.apps.showpic.model.MainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.rv_main)RecyclerView recyclerView;
    @BindView(R.id.viewer)PhotoAlbumViewer albumViewer;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private MainAdapter mainAdapter;
    private Feed feed;
    private AlbumPresenter albumPresenter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        MainApi api = ThisApp.getApi(this);

        api.recent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feed -> {

                    this.feed = feed;
                    mainAdapter = getMainAdapter(feed);

                    albumPresenter = new AlbumPresenter(this, getImgList());

                    showRecycler();
                });


    }

    @NonNull
    private MainAdapter getMainAdapter(Feed feed) {
        return new MainAdapter(this,feed);
    }

    private void showRecycler() {
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setHasFixedSize(true);

    }

    public PhotoAlbumViewer getAlbumViewer() {
        return albumViewer;
    }

    public List<Img> getImgList() {
        List<Img> result = new ArrayList<>();
        for (Entry entry : feed.entries) {
            result.add(entry.img);
        }

        return result;
    }

    public void openImage(View __){
//        albumPresenter.openInImageViewer(__);
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void openImage(int position) {
        albumPresenter.openInImageViewer(position);
    }

    @Override
    public void onBackPressed() {
        if (albumViewer.getVisibility()==View.VISIBLE){
            hideViewer();
        } else {
            super.onBackPressed();
        }

    }

    private void hideViewer() {
        albumViewer.setVisibility(View.GONE);
        getSupportActionBar().show();
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
