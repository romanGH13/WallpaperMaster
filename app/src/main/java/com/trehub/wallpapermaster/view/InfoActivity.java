package com.trehub.wallpapermaster.view;

import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.info.MovieInfo;
import com.trehub.wallpapermaster.presenter.InfoPresenter;
import com.trehub.wallpapermaster.utils.interfaces.KeyItemClickListener;
import com.trehub.wallpapermaster.view.adapters.YouTubeListAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import static com.trehub.wallpapermaster.utils.Constants.ERROR;
import static com.trehub.wallpapermaster.utils.Constants.GOOGLE_API_KEY;
import static com.trehub.wallpapermaster.utils.Constants.IMG_URL;
import static com.trehub.wallpapermaster.utils.Constants.MOVIE_ID;

public class InfoActivity extends BaseView
        implements SwipeRefreshLayout.OnRefreshListener, KeyItemClickListener {

    private ImageView poster;
    private TextView title;
    private TextView genre;
    private TextView tagLine;
    private TextView voteAverage;
    private TextView releaseDate;
    private TextView runtime;
    private TextView budget;
    private TextView revenue;
    private TextView status;
    private TextView overview;

    private RelativeLayout youTubeContainer;
    private ScrollView scrollView;

    private RecyclerView youTubeList;
    private LinearLayoutManager layoutManager;
    private YouTubePlayerFragment youTubePlayerFragment;

    @Inject
    InfoPresenter infoPresenter;

    @Inject
    YouTubeListAdapter youTubeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        MyApplication.getInfoViewComponent().inject(this);
        infoPresenter.onAttach(this);
        bindView();

        infoPresenter.getMovieInfo(getMovieId());
    }

    @Override
    public void bindView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        poster = (ImageView) findViewById(R.id.infoPoster);

        title = (TextView) findViewById(R.id.infoTitle);
        genre = (TextView) findViewById(R.id.genre);
        tagLine = (TextView) findViewById(R.id.tagLine);
        voteAverage = (TextView) findViewById(R.id.voteAverage);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        runtime = (TextView) findViewById(R.id.runtime);
        budget = (TextView) findViewById(R.id.budget);
        revenue = (TextView) findViewById(R.id.revenue);
        status = (TextView) findViewById(R.id.status);
        overview = (TextView) findViewById(R.id.infoOverview);

        layoutManager = new LinearLayoutManager(this);
        youTubeContainer = (RelativeLayout) findViewById(R.id.youTubeContainer);
        youTubeList = (RecyclerView) findViewById(R.id.youTubeList);
        youTubeList.setLayoutManager(layoutManager);
    }

    public void showTrailersList(List list) {
        youTubeListAdapter.init(list, this, this);
        youTubeList.setAdapter(youTubeListAdapter);
    }

    public void showYouTubeFragment(final String key) {
        youTubePlayerFragment = YouTubePlayerFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction()
                .replace(R.id.youTubeContainer, youTubePlayerFragment);
        transaction.commit();

        youTubePlayerFragment.initialize(GOOGLE_API_KEY, new OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    setScrollViewDown();
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setShowFullscreenButton(false);
                    youTubePlayer.loadVideo(key);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                showMessage(ERROR);
            }
        });
    }

    public void setScrollViewDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void setDescription(MovieInfo movieInfo) {
        genre.setText("");
        Resources res = getResources();

        title.setText(res.getString(R.string.form_for_title, movieInfo.getTitle(), movieInfo.getOriginalTitle()));
        if (movieInfo.getGenres() != null && movieInfo.getGenres().size() > 0) {
            for (int i = 0; i < movieInfo.getGenres().size(); i++) {
                genre.append(movieInfo.getGenres().get(i).getName() + "\n");
            }
        }
        title.setText(res.getString(R.string.form_for_title, movieInfo.getTitle(), movieInfo.getOriginalTitle()));
        tagLine.setText(res.getString(R.string.tag_line, movieInfo.getTagline()));
        voteAverage.setText(res.getString(R.string.vote_average, movieInfo.getVoteAverage(), movieInfo.getVoteCount()));
        releaseDate.setText(res.getString(R.string.release_date, movieInfo.getReleaseDate()));
        runtime.setText(res.getString(R.string.runtime, movieInfo.getRuntime()));
        budget.setText(res.getString(R.string.budget, movieInfo.getBudget()));
        revenue.setText(res.getString(R.string.revenue, movieInfo.getRevenue()));
        status.setText(res.getString(R.string.status, movieInfo.getStatus()));
        overview.setText(movieInfo.getOverview());
    }

    public void setPoster(String path) {
        Picasso.with(this)
                .load(IMG_URL + path)
                .placeholder(R.drawable.the_movie_db)
                .error(R.drawable.the_movie_db)
                .into(poster);
    }

    public int getMovieId() {
        return getIntent().getIntExtra(MOVIE_ID, 0);
    }

    @Override
    public void onClickItem(String key) {
        showYouTubeFragment(key);
    }

    @Override
    public void onRefresh() {
        infoPresenter.getMovieInfo(getMovieId());
    }

    public void onDestroy() {
        super.onDestroy();
        infoPresenter.onDispose();
        infoPresenter.onDetach();
    }
}