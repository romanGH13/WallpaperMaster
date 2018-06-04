package com.trehub.wallpapermaster.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.primaryList.Result;
import com.trehub.wallpapermaster.utils.interfaces.IdItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.trehub.wallpapermaster.utils.Constants.ANIMATION_DURATION;
import static com.trehub.wallpapermaster.utils.Constants.IMG_URL;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Result> moviesList;
    private IdItemClickListener itemClickListener;

    public MoviesRecyclerAdapter() {
    }

    public void init(Context context, List<Result> moviesList, IdItemClickListener itemClickListener) {
        this.context = context;
        this.moviesList = moviesList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MoviesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerAdapter.ViewHolder holder, int position) {
        Picasso.with(context)
                .load(IMG_URL + moviesList.get(position).getPosterPath())
                .placeholder(R.drawable.the_movie_db)
                .error(R.drawable.the_movie_db)
                .into(holder.poster);

        holder.title.setText(moviesList.get(position).getTitle().toString());
        holder.description.setText(context.getResources().getString(R.string.short_item_description,
                moviesList.get(position).getVoteAverage(),
                moviesList.get(position).getVoteCount(),
                moviesList.get(position).getReleaseDate()));
        holder.overview.setText(moviesList.get(position).getOverview());

        setAnimation(holder.itemView);
    }

    public void setAnimation(View view) {
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
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
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            overview = (TextView) itemView.findViewById(R.id.overview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(moviesList.get(getAdapterPosition()).getId());
            }
        }
    }
}
