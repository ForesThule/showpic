package com.lesforest.apps.showpic.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

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
    @BindView(R.id.date_picker)
    DatePicker datePicker;
    @BindView(R.id.tvdate)
    TextView tvDate;

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
    Calendar dateAndTime;
    private DatePickerDialog.OnDateSetListener d;
    private DatePickerDialog datePickerDialog;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dateAndTime = Calendar.getInstance();

        tvDate.setText(Helpers.getCurrentDateTimeInNiceFormat(dateAndTime));


        d= (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            setInitialDateTime();

            Timber.i("onCreate: ");
            mainPresenter.getPhotosOnDate(Helpers.getCurrentDateTimeInNiceFormat(dateAndTime));

            tvDate.setText(Helpers.getCurrentDate(dateAndTime));

        };

        datePickerDialog = new DatePickerDialog(MainActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));

        methodRequiresTwoPermission();

        albumPresenter = new AlbumPresenter(this);
        mainPresenter = new MainPresenter(this);

        setSupportActionBar(toolbar);

        initRecycler();

        initAdapter();

        getPhotosForShow();

    }

    private void getPhotosForShow() {

        mainPresenter.getYandexRecentPhotos(String.format(
                Cv.INIT_PHOTOS_ENDPOINT,
                Helpers.getCurrentDateTimeInNiceFormat()));
    }

    private void initAdapter() {

        mainAdapter = new MainAdapter(this, recyclerView);
        picsumAdapter = new PicsumAdapter(this, recyclerView);
        unsplashAdapter = new UnsplashAdapter(this, recyclerView);


        mainAdapter.setOnLoadMoreListener(() -> {

            mainPresenter.loadMore();
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
            String e = String.format("https://source.unsplash.com/collection/19072%d", i);
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

            case R.id.menu_select_date:

                showDatePicker();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {

//        datePicker.setVisibility(View.VISIBLE);



        datePickerDialog.show();


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

        private void checkPermissions() {

            if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, Cv.PERMISSIONS_ITR_REQUEST);
            }
        }

        public void showProgress() {

            progress.setVisibility(View.VISIBLE);

        }



        public void animatePogressShow() {

            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up);


            slide_up.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            progress.startAnimation(slide_up);


        }

        public void hideProgress() {
            animatePogresGone();

        }

        public void animatePogresGone() {
            Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down);


            slide_down.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            progress.startAnimation(slide_down);
        }
    }
