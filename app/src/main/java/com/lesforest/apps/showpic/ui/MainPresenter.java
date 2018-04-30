package com.lesforest.apps.showpic.ui;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.lesforest.apps.showpic.ThisApp;
import com.lesforest.apps.showpic.network.MainApi;
import com.lesforest.apps.showpic.network.PicsumApi;
import com.lesforest.apps.showpic.network.UnsplashApi;
import com.lesforest.apps.showpic.utils.Cv;
import com.lesforest.apps.showpic.utils.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter {

    private final MainActivity activity;
    private final MainApi api;
    private final UnsplashApi unsplashApi;

    @SuppressLint("CheckResult")
    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        api = ThisApp.getApi(activity);
        unsplashApi = ThisApp.get(activity).unsplashApi;

    }


    public List<String> generatePicsumData() {
        List<String> result = new ArrayList<>();
        Random random = new Random();
//        random.setSeed(34);
//        random.nextInt()
        for (int i = 0; i < 100; i++) {

            if (i%10==0){

                int i1 = random.nextInt(400);
                String link = String.format("https://picsum.photos/500/?image=%d",i1);
                result.add(link);
            }
        }
        return result;
    }

    @SuppressLint("CheckResult")
    public void getPicsumList(){

        PicsumApi picsumApi = Helpers.createPicsumApi(activity);
        picsumApi.list()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(picsumModels -> io.reactivex.Observable.fromIterable(picsumModels)
                        .map(picsumModel -> picsumModel.post_url)
                        .toList())
                .subscribe(picsumModels -> activity.showPicsumReq(picsumModels));
    }

    @SuppressLint("CheckResult")
    public void getYandexRecentPhotos(String path) {


            activity.showProgress();

            api.recent(path)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(feed -> {

                        activity.entryList = feed.entries;

                        activity.feed = feed;

                        activity.showMainPhotos();

                        activity.hideProgress();



                        if (null==feed.entries || feed.entries.size()==0){
                            Toast.makeText(activity, "К сожалению нам нечего вам показать. Попробуйте выбрать другую дату.", Toast.LENGTH_LONG).show();
                        }

                    });
    }

    @SuppressLint("CheckResult")
    public void loadMore() {

        activity.showProgress();

        String next = activity.feed.links.next;

        if (null!=next){
            String[] split = next.split(Cv.ENDPOINT+"/");
            String nextPath = split[1];

            api.recent(nextPath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(feed1 -> {
                        activity.entryList.addAll(feed1.entries);
                        return feed1;
                    })
                    .subscribe(feed -> {

                        activity.feed = feed;
                        activity.addPhotoYandex();

                        activity.hideProgress();

                    }, Throwable::printStackTrace);
        } else {
            activity.hideProgress();
        }
    }

    @SuppressLint("CheckResult")
    public void getPhotosOnDate(String date) {

        String format = String.format(Cv.INIT_PHOTOS_ENDPOINT, date);

        activity.showProgress();

        activity.mainAdapter.clearData();

        api.recent(format)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feed -> {

                    Timber.i("getPhotosOnDate: %s",feed);

                    activity.entryList = feed.entries;

                    activity.feed = feed;

                    activity.showMainPhotos();

                    activity.hideProgress();

                    if (null==feed.entries || feed.entries.size()==0){
                        Toast.makeText(activity, "К сожалению нам нечего вам показать. Попробуйте выбрать другую дату.", Toast.LENGTH_LONG).show();
                    }

                });
    }
}
