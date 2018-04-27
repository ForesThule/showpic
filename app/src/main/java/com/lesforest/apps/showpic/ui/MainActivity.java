package com.lesforest.apps.showpic.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lesforest.apps.showpic.R;
import com.lesforest.apps.showpic.model.Entry;
import com.lesforest.apps.showpic.model.Feed;
import com.lesforest.apps.showpic.model.Img;
import com.lesforest.apps.showpic.ui.adapters.MainAdapter;
import com.lesforest.apps.showpic.network.MainApi;
import com.lesforest.apps.showpic.ui.adapters.OnLoadMoreListener;
import com.lesforest.apps.showpic.ui.adapters.PicsumAdapter;
import com.lesforest.apps.showpic.ui.adapters.UnsplashAdapter;
import com.lesforest.apps.showpic.utils.Cv;
import com.lesforest.apps.showpic.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.rv_main)
    RecyclerView recyclerView;
    @BindView(R.id.viewer)
    PhotoAlbumViewer albumViewer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner)
    public ProgressBar progress;

    public MainAdapter mainAdapter;
    public List<Entry> entryList;
    private AlbumPresenter albumPresenter;
    private MainApi api;
    private GridLayoutManager layoutManager;
    public Feed feed;
    public PicsumAdapter picsumAdapter;
    private List<String> picsumList;
    private Menu menu;
    private MainPresenter mainPresenter;
    private UnsplashAdapter unsplashAdapter;
    private final int RC_INTERNET = 10;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        methodRequiresTwoPermission();

        albumPresenter = new AlbumPresenter(this);
        mainPresenter = new MainPresenter(this);

        setSupportActionBar(toolbar);

        initRecycler();

        initAdapter();

        mainPresenter.getYandexRecentPhotos(String.format(Cv.INIT_PHOTOS_ENDPOINT,Helpers.getCurrentDateTimeInNiceFormat()));

    }

    private void initAdapter() {

        mainAdapter = new MainAdapter(this, recyclerView);
        picsumAdapter = new PicsumAdapter(this, recyclerView);
        unsplashAdapter = new UnsplashAdapter(this, recyclerView);




        mainAdapter.setOnLoadMoreListener(() -> {
            //add null , so the adapter will check view_type and show progress bar at bottom

//            entryList.add(null);

//            mainAdapter.notifyItemInserted(entryList.size() - 1);


            mainPresenter.loadMore();


//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //   remove progress item
//                    studentList.remove(studentList.size() - 1);
//                    mAdapter.notifyItemRemoved(studentList.size());
//                    //add items one by one
//                    int start = studentList.size();
//                    int end = start + 20;
//
//                    for (int i = start + 1; i <= end; i++) {
//                        studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
//                        mAdapter.notifyItemInserted(studentList.size());
//                    }
//                    mAdapter.setLoaded();
//                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                }
//            }, 2000);

        });





        picsumList = mainPresenter.generatePicsumData();

        picsumAdapter.setData(picsumList);
        unsplashAdapter.setData(generateUnsplashData());

        recyclerView.setAdapter(mainAdapter);

    }

    private List<String> generateUnsplashData() {
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
//            String e = "https://source.unsplash.com/900x900/?nature,water";
            String e = String.format("https://source.unsplash.com/collection/19072%d",i);
            strings.add(e);
        }

        return strings;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                break;

            case R.id.menu_refresh:

                refreshPicsum();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPicsum() {

        picsumList = mainPresenter.generatePicsumData();
        picsumAdapter.setData(picsumList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
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
            Img i = entry.img;
            result.add(i);
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
        if (albumViewer.getVisibility() == View.VISIBLE) {
            hideViewer();
            int position = albumViewer.getPosition();

            recyclerView.smoothScrollToPosition(position);

        } else {
            super.onBackPressed();
        }

    }

    public void hideViewer() {
        albumViewer.setVisibility(View.GONE);
        getSupportActionBar().show();
    }

    public void showPicsumReq(List<String> picsumModels) {
        picsumAdapter.setData(picsumModels);
        recyclerView.setAdapter(picsumAdapter);
    }

    public void addPhotoYandex() {

        mainAdapter.notifyDataSetChanged();
        mainAdapter.setLoaded();
    }

    @AfterPermissionGranted(RC_INTERNET)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.INTERNET};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Разрешение на использование интернета",
                    RC_INTERNET, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void showMainPhotos() {

        mainAdapter.setData(entryList);
        mainAdapter.notifyDataSetChanged();

    }

    public void showProgress() {

        ProgressBarAnimation anim = new ProgressBarAnimation(progress, 10, 30);
        anim.setDuration(1000);
        progress.startAnimation(anim);
    }
}
