package com.lesforest.apps.showpic.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lesforest.apps.showpic.R;
import com.lesforest.apps.showpic.ThisApp;
import com.lesforest.apps.showpic.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PicsumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;
    private final MainActivity activity;
    private final RecyclerView recyclerView;
    List<String> data = new ArrayList<>();
    LayoutInflater layoutInflater;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int visibleThreshold = 5;
    private final Picasso picasso;


    public PicsumAdapter(MainActivity activity, RecyclerView recyclerView) {
        this.activity = activity;


        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView
                .getLayoutManager();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });

        this.recyclerView = recyclerView;

        picasso = ThisApp.get(activity).getPicasso();

    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {

            View view = layoutInflater.inflate(R.layout.image_item, parent, false);

            vh = new ViewHolder(activity,view);

        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder h = (ViewHolder) holder;

            ImageView imageView = h.imageView;

            String entry = data.get(position);


            picasso.load(entry)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

//            Action action = () ->
//            Completable.fromAction(action)
//                    .delay(5, TimeUnit.SECONDS)
//                    .subscribe(() -> Timber.i("onBindViewHolder: %s","ddd"));


            imageView.setOnClickListener(v -> activity.openImage(position));


        } else {
            ((PicsumAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.iv_item);


        }

        public ViewHolder(MainActivity activity, View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_item);

            Display display = activity.getWindowManager().getDefaultDisplay();
//            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, activity.getResources().getDisplayMetrics());
            int width = display.getWidth()/ 2;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,width);
            imageView.setLayoutParams(parms);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
