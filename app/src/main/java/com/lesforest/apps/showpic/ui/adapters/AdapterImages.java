package com.lesforest.apps.showpic.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.lesforest.apps.showpic.ThisApp;
import com.lesforest.apps.showpic.model.Img;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class AdapterImages extends PagerAdapter {

  private List<Img> data;

  public AdapterImages(List<Img> data) {
    super();
    this.data = data;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    PhotoView photoView = new PhotoView(container.getContext());

//    final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

    String currentLink = data.get(position).m.href;

    Timber.i(currentLink);

    ThisApp.get(container.getContext())
            .getPicasso()
            .with(container.getContext())
        .load(currentLink)
        .into(photoView);

    container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    return photoView;
  }

  @Override public int getCount() {
    return null == data ? 0 : data.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  public void setData(List<Img> data) {
    this.data = data;
    notifyDataSetChanged();
  }
}