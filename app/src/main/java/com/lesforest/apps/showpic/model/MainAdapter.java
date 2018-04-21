package com.lesforest.apps.showpic.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.lesforest.apps.showpic.GlideApp;
import com.lesforest.apps.showpic.Helpers;
import com.lesforest.apps.showpic.MainActivity;
import com.lesforest.apps.showpic.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.glide.transformations.CropSquareTransformation;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final MainActivity activity;
    Feed data;
    LayoutInflater layoutInflater;

    public MainAdapter(MainActivity activity, Feed data) {
        this.activity = activity;

        this.data = data;

        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = layoutInflater.inflate(R.layout.image_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ImageView imageView = holder.imageView;

        Entry entry = data.entries.get(position);


        GlideApp.with(activity)
                .load(entry.img.m.href)
                .placeholder(R.drawable.ic_launcher_background)
                .centerInside()
                .thumbnail(0.5f)
//                .centerCrop()
                .transform(new CropSquareTransformation())
                .override(900,900)
//                .s
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .onlyRetrieveFromCache(true)
                .into(imageView);

        imageView.setOnClickListener(v -> {
            activity.openImage(position);
        });



//        Glide.with(ctx)
//                .load(entry.img.m.href)
//                .
////                .into(imageView)
//
//        Picasso.with(ctx)
//
//                .load(entry.img.m.href)
//
//                //.placeholder(R.drawable.ic_loop)
//                .into(imageView, new Callback() {
//                    @Override public void onSuccess() {
//
//                    }
//
//                    @Override public void onError() {
//                    }
//                });

//        Worklog worklog = worklogList.get(position);




//        Picasso.with(ctx)
//                .load(new File(Uri.decode(attachments.get(position))))
//                .placeholder(R.drawable.ic_file_image_black_24dp)
//                .into(imageView);
//        holder.mTextView.setText(mDataset[position]);

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.iv_item);
        }
    }
}
