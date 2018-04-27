package com.lesforest.apps.showpic.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lesforest.apps.showpic.R;
import com.lesforest.apps.showpic.ui.adapters.AdapterImages;
import com.lesforest.apps.showpic.model.Img;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by root on 20.11.17.
 */

public class PhotoAlbumViewer extends RelativeLayout {

  Context context;
  View rootView;

  @BindView(R.id.view_pager)
  HackyViewPager imageViewerPager;
  @BindView(R.id.viewPagerDots) LinearLayout pagerIndicator;
  @BindView(R.id.arrow_back) ImageView arrowBack;
//  @BindView(R.id.delete_image) ImageView deleteImage;
  @BindView(R.id.btn_prev) ImageView prevImage;
  @BindView(R.id.btn_next) ImageView nextImage;

//  private ImageView[] dots;
  private int currentImagePosition;

  private List<Img> photoLinksData;
  private AdapterImages adapterForViewPager;
  private AlbumPresenter albumPresenter;


  public PhotoAlbumViewer(Context context) {
    super(context);
    this.context = context;
    init(context);
  }

  public PhotoAlbumViewer(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init(context);
  }

  private void init(Context context){
    rootView = inflate(context,R.layout.photo_album_images_viewer,this);
    ButterKnife.bind(this);

    prevImage.setOnClickListener(this::prevImgBtn);
    nextImage.setOnClickListener(this::nextImgBtn);
    arrowBack.setOnClickListener(this::closeViewPager);
//    deleteImage.setOnClickListener(this::deleteImage);

  }

  public void setPresenter(AlbumPresenter albumPresenter) {
    this.albumPresenter = albumPresenter;
  }

  public void setImageViewerPager(HackyViewPager imageViewerPager) {
    this.imageViewerPager = imageViewerPager;
  }



  public void nextImgBtn(View view) {
    imageViewerPager.setCurrentItem(
        imageViewerPager.getCurrentItem() % photoLinksData.size() + 1);
  }

  public void prevImgBtn(View __) {
    imageViewerPager.setCurrentItem(
        imageViewerPager.getCurrentItem() % photoLinksData.size() - 1);
  }


  public void closeViewPager(View __) {
    albumPresenter.onBackPressed();
  }


  public void openInImageViewer(View view) {

    adapterForViewPager.setData(photoLinksData);

    int index = Integer.parseInt((String) view.getTag());

    if (index < photoLinksData.size()) {

      imageViewerPager.setCurrentItem(index);
    }
  }

  public void setViewPagerController() {

    adapterForViewPager = new AdapterImages(photoLinksData);

    imageViewerPager.setAdapter(adapterForViewPager);

    imageViewerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

      @Override public void onPageSelected(int position) {
        currentImagePosition = position;
      }

      @Override public void onPageScrollStateChanged(int state) {}
    });


  }

  public void setData(List<Img> data) {
    photoLinksData = data;
    setViewPagerController();
  }

  public HackyViewPager getImageViewerPager() {
    return imageViewerPager;
  }

  public int getPosition() {
    return currentImagePosition;
  }
}
