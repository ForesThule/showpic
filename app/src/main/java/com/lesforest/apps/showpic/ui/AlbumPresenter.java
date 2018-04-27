package com.lesforest.apps.showpic.ui;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.View;

public class AlbumPresenter {
    private Uri currentPhotoUri;
    private MainActivity activity;
    public AlertDialog photoChooserDialog;

    public AlbumPresenter(MainActivity act) {
        activity = act;
    }

    public void onBackPressed() {
        activity.onBackPressed();
    }

    public void openInImageViewer(int position) {
        PhotoAlbumViewer viewer = activity.getAlbumViewer();
        viewer.setPresenter(this);
        viewer.setData(activity.getImgList());

        viewer.getImageViewerPager().setCurrentItem(position);
        viewer.setVisibility(View.VISIBLE);

        activity.hideActionBar();

    }
}
