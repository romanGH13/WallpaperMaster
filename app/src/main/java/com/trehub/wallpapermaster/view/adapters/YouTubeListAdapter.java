package com.trehub.wallpapermaster.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.info.Result;
import com.trehub.wallpapermaster.utils.interfaces.KeyItemClickListener;

import java.util.List;

import static com.trehub.wallpapermaster.utils.Constants.ANIMATION_DURATION;

public class YouTubeListAdapter extends RecyclerView.Adapter<YouTubeListAdapter.ViewHolder> {

    private List<Result> list;
    private KeyItemClickListener itemClickListener;
    private Context context;

    public void init(List<Result> list, KeyItemClickListener itemClickListener, Context context) {
        this.list = list;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @Override
    public YouTubeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YouTubeListAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(context.getResources().getString(R.string.watch_video, position + 1));
        setAnimation(holder.itemView);
    }

    public void setAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(list.get(getAdapterPosition()).getKey());
            }
        }
    }
}
