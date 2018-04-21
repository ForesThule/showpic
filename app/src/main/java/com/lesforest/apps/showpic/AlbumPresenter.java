package com.lesforest.apps.showpic;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.View;

import com.lesforest.apps.showpic.model.Img;
import java.util.List;

public class AlbumPresenter {
  private Uri currentPhotoUri;
  private MainActivity activity;
  public AlertDialog photoChooserDialog;
  List<Img> data;

  public AlbumPresenter(MainActivity act,   List<Img> data) {
    activity = act;
    this.data = data;
  }

//  public void openInImageViewer(View view) {
//
//    PhotoAlbumViewer viewer = activity.getAlbumViewer();
//    viewer.setPresenter(this);
//    viewer.setData(activity.getImgList());
//
////    int index = Integer.parseInt((String) view.getTag());
//
//    if (index < data.size()) {
//      viewer.getImageViewerPager().setCurrentItem(index);
//      viewer.setVisibility(View.VISIBLE);
//
//      activity.hideActionBar();
//
//    } else {
////      showTakePhotoChooser();
//    }
//  }


  public void onBackPressed() {
    activity.onBackPressed();
  }

  public void openInImageViewer(int position) {
    PhotoAlbumViewer viewer = activity.getAlbumViewer();
    viewer.setPresenter(this);
    viewer.setData(activity.getImgList());

//    int index = Integer.parseInt((String) view.getTag());


      viewer.getImageViewerPager().setCurrentItem(position);
      viewer.setVisibility(View.VISIBLE);

      activity.hideActionBar();

  }
}
