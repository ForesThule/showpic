package com.lesforest.apps.showpic.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lesforest.apps.showpic.R;
import com.lesforest.apps.showpic.ThisApp;
import com.lesforest.apps.showpic.model.Entry;
import com.lesforest.apps.showpic.model.Feed;
import com.lesforest.apps.showpic.model.Img;
import com.lesforest.apps.showpic.adapters.MainAdapter;
import com.lesforest.apps.showpic.network.MainApi;
import com.lesforest.apps.showpic.utils.Cv;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.rv_main)RecyclerView recyclerView;
    @BindView(R.id.viewer)PhotoAlbumViewer albumViewer;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private MainAdapter mainAdapter;
    private List<Entry> entryList;
    private AlbumPresenter albumPresenter;
    private MainApi api;
    private GridLayoutManager layoutManager;
    private Feed feed;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        api = ThisApp.getApi(this);


        initRecycler();

        initAdapter();


        albumPresenter = new AlbumPresenter(this);


        getPhotosfromServer(Cv.INIT_PHOTOS_ENDPOINT);

    }

    private void initAdapter() {

        mainAdapter = new MainAdapter(this,recyclerView);

        recyclerView.setAdapter(mainAdapter);


        mainAdapter.setOnLoadMoreListener(this::addPhotosFromServer);

    }

    @SuppressLint("CheckResult")
    private void addPhotosFromServer() {
        String next = feed.links.next;


        if (null!=next){
            String[] split = next.split("recent/");
            String nextPath = split[1];

            api.recent(nextPath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(feed1 -> {
                        entryList.addAll(feed1.entries);
                        return feed1;
                    })
                    .subscribe(feed -> {
                        Timber.i("entryList %s",feed);

                        this.feed = feed;

//                    mainAdapter.setData(entryList);

                        mainAdapter.notifyDataSetChanged();

                        mainAdapter.setLoaded();


                    });
        }
    }

    @SuppressLint("CheckResult")
    private void getPhotosfromServer(String s) {

        api.recent(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(feed1 -> {
                    entryList = feed1.entries;
                    return feed1;
                })
                .subscribe(feed -> {

                    Timber.i("entryList %s",feed);

                    this.feed = feed;

                    mainAdapter.setData(entryList);

                    mainAdapter.notifyDataSetChanged();

                });
    }



    private void initRecycler() {
//        mainAdapter.set
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }




    public PhotoAlbumViewer getAlbumViewer() {
        return albumViewer;
    }

    public List<Img> getImgList() {

        List<Img> result = new ArrayList<>();

        for (Entry entry : entryList) {
            result.add(entry.img);
        }

        return result;
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
            int position = albumViewer.getPosition();

            recyclerView.smoothScrollToPosition(position);

        } else {
            super.onBackPressed();
        }

    }

    private void hideViewer() {
        albumViewer.setVisibility(View.GONE);
        getSupportActionBar().show();
    }

}
