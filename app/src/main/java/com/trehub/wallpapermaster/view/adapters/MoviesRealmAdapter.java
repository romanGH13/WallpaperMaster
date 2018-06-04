package com.trehub.wallpapermaster.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.primaryList.Cache;
import com.trehub.wallpapermaster.model.pojo.primaryList.Result;
import com.trehub.wallpapermaster.utils.interfaces.IdItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

import static com.trehub.wallpapermaster.utils.Constants.ANIMATION_DURATION;
import static com.trehub.wallpapermaster.utils.Constants.CARD_ITEM;
import static com.trehub.wallpapermaster.utils.Constants.IMG_URL;
import static com.trehub.wallpapermaster.utils.Constants.PROGRESS_ITEM;

public class MoviesRealmAdapter
        extends RealmRecyclerViewAdapter<Cache, MoviesRealmAdapter.ViewHolder> {

    private Context context;
    private IdItemClickListener itemClickListener;

    private RealmResults<Cache> realmResults;
    private ArrayList<Result> moviesList = new ArrayList<Result>();

    private int listId = 1;
    private int currentPage = 0;
    private int totalPages = 0;

    public MoviesRealmAdapter(RealmResults<Cache> realmResults) {
        super(realmResults, true);
    }

    public void init(Context context, RealmResults<Cache> realmResults, IdItemClickListener itemClickListener) {
        this.context = context;
        this.realmResults = realmResults;
        this.itemClickListener = itemClickListener;
    }

    public void setRealmResults(RealmResults<Cache> realmResults) {
        this.realmResults = realmResults;
        setList();
    }

    public void setList() {
        moviesList.clear();
        if (realmResults != null && !realmResults.isEmpty()) {
            for (Cache cache : realmResults)
                if (cache.getListId() == listId) {
                    moviesList.addAll(cache.getResults());
                    currentPage = cache.getPage();
                    totalPages = cache.getTotalPages();
                }
            notifyDataSetChanged();
        }
    }

    public void addProgressItem() {
        if (!moviesList.isEmpty() && moviesList.get(moviesList.size() - 1) != null) {
            if (getCurrentPage() < getTotalPages()) {
                moviesList.add(null);
                notifyDataSetChanged();
            }
        }
    }

    public void removeProgressItem() {
        if (!moviesList.isEmpty() && getItemViewType(getItemCount() - 1) == 1) {
            moviesList.remove(moviesList.size() - 1);
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
        setList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;

        if (viewType == CARD_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_layout, parent, false);
            viewHolder = new ViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_layout, parent, false);
            viewHolder = new ViewHolder(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == CARD_ITEM) {
            Picasso.with(context)
                    .load(IMG_URL +
                            moviesList.get(position).getPosterPath())
                    .placeholder(R.drawable.the_movie_db)
                    .error(R.drawable.the_movie_db)
                    .into(holder.poster);

            holder.title.setText(moviesList.get(position).getTitle().toString());
            holder.description.setText(context.getResources().getString(R.string.short_item_description,
                    moviesList.get(position).getVoteAverage(),
                    moviesList.get(position).getVoteCount(),
                    moviesList.get(position).getReleaseDate()));
            holder.overview.setText(moviesList.get(position).getOverview());
        }
        setAnimation(holder.itemView);
    }

    public void setAnimation(View view) {
        ScaleAnimation animation = new ScaleAnimation(0.85f, 1.0f, 0.85f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
    }

    @Override
    public int getItemViewType(int position) {
        return moviesList.get(position) != null ? CARD_ITEM : PROGRESS_ITEM;
    }

    @Override
    public int getItemCount() {
        return moviesList != null ? moviesList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView title;
        private TextView description;
        private TextView overview;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            overview = (TextView) itemView.findViewById(R.id.overview);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null && moviesList.get(getAdapterPosition()) != null) {
                itemClickListener.onClickItem(moviesList.get(getAdapterPosition()).getId());
            }
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
