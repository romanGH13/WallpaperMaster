package com.trehub.wallpapermaster.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.primaryList.Cache;
import com.trehub.wallpapermaster.presenter.MainPresenter;
import com.trehub.wallpapermaster.utils.BottomNavigationViewHelper;
import com.trehub.wallpapermaster.utils.interfaces.IdItemClickListener;
import com.trehub.wallpapermaster.view.adapters.MoviesRealmAdapter;

import javax.inject.Inject;

import io.realm.RealmResults;

import static com.trehub.wallpapermaster.utils.Constants.ERROR_NET;
import static com.trehub.wallpapermaster.utils.Constants.MOVIE_ID;
import static com.trehub.wallpapermaster.utils.Constants.NOW_PLAYING;
import static com.trehub.wallpapermaster.utils.Constants.POPULAR;
import static com.trehub.wallpapermaster.utils.Constants.TOP;
import static com.trehub.wallpapermaster.utils.Constants.UPCOMING;

public class MainActivity extends BaseView
        implements IdItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private BottomNavigationView navigation;
    private FloatingActionButton fab;

    private boolean isLoading = false;
    private boolean isInited = false;

    @Inject
    MainPresenter mainPresenter;

    @Inject
    MoviesRealmAdapter moviesRealmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.getMainViewComponent().inject(this);
        mainPresenter.onAttach(this);
        bindView();

        mainPresenter.startLoading();
        mainPresenter.setAutoUpdateList();
    }

    @Override
    protected void bindView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.moviesList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.topScreen:
                    hideSwipeRefreshing();
                    moviesRealmAdapter.setListId(TOP);
                    recyclerView.scrollToPosition(0);
                    return true;
                case R.id.popularScreen:
                    hideSwipeRefreshing();
                    moviesRealmAdapter.setListId(POPULAR);
                    recyclerView.scrollToPosition(0);
                    return true;
                case R.id.upcomingScreen:
                    hideSwipeRefreshing();
                    moviesRealmAdapter.setListId(UPCOMING);
                    recyclerView.scrollToPosition(0);
                    return true;
                case R.id.nowPlayingScreen:
                    hideSwipeRefreshing();
                    moviesRealmAdapter.setListId(NOW_PLAYING);
                    recyclerView.scrollToPosition(0);
                    return true;
            }
            return false;
        }
    };

    public void initMoviesListAdapter(final RealmResults<Cache> results) {
        if (!isInited) {
            isInited = true;

            moviesRealmAdapter.init(this, results, this);
            recyclerView.setAdapter(moviesRealmAdapter);
            moviesRealmAdapter.setList();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                            if (moviesRealmAdapter.getCurrentPage() < moviesRealmAdapter.getTotalPages()) {
                                setIsLoading(true);
                                moviesRealmAdapter.addProgressItem();
                                paginationMovieList(moviesRealmAdapter.getListId(),
                                        moviesRealmAdapter.getCurrentPage() + 1);
                            }
                        }
                    }
                }
            });
        } else {
            moviesRealmAdapter.setRealmResults(results);
        }
    }

    public void paginationMovieList(int listId, int page) {
        if (mainPresenter.isNetworkAvailable(this)) {
            switch (listId) {
                case TOP:
                    mainPresenter.getTopRatedList(page);
                    break;
                case POPULAR:
                    mainPresenter.getPopularList(page);
                    break;
                case UPCOMING:
                    mainPresenter.getUpcomingList(page);
                    break;
                case NOW_PLAYING:
                    mainPresenter.getNowPlayingList(page);
                    break;
            }
        } else {
            moviesRealmAdapter.removeProgressItem();
            showMessage(ERROR_NET);
        }
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setIsInited(boolean isInited) {
        this.isInited = isInited;
    }

    @Override
    public void onRefresh() {
        mainPresenter.startLoading();
    }

    @Override
    public void onClickItem(int id) {
        startMovieInfoActivity(id);
    }

    public void hideSwipeRefreshing() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void hideProgressInRecycler() {
        if (moviesRealmAdapter.getItemCount() > 0) {
            moviesRealmAdapter.removeProgressItem();
        }
    }

    public void startMovieInfoActivity(int id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(MOVIE_ID, id);
        startActivity(intent);
    }

    public void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        mainPresenter.onDispose();
        mainPresenter.onDetach();
    }
}
