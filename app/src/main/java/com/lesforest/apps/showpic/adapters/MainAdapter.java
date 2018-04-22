package com.lesforest.apps.showpic.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lesforest.apps.showpic.ui.MainActivity;
import com.lesforest.apps.showpic.utils.OnLoadMoreListener;
import com.lesforest.apps.showpic.R;
import com.lesforest.apps.showpic.model.Entry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;
    private final MainActivity activity;
    private final RecyclerView recyclerView;
    List<Entry> data = new ArrayList<>();
    LayoutInflater layoutInflater;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int visibleThreshold = 5;
    private final Picasso picasso;


    public MainAdapter(MainActivity activity, RecyclerView recyclerView) {
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

        picasso = Picasso.with(activity);

    }

    public void setData(List<Entry> data) {
        this.data = data;
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

            vh = new ViewHolder(view);

        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder h = (ViewHolder) holder;

            ImageView imageView = h.imageView;

            Entry entry = data.get(position);

            String href = entry.img.m.href;

            picasso.load(href)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

            imageView.setOnClickListener(v -> activity.openImage(position));


        } else {
            ((MainAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
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
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
