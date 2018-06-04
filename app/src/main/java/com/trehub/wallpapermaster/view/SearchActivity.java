package com.trehub.wallpapermaster.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.R;
import com.trehub.wallpapermaster.model.pojo.primaryList.Result;
import com.trehub.wallpapermaster.presenter.SearchPresenter;
import com.trehub.wallpapermaster.utils.interfaces.IdItemClickListener;
import com.trehub.wallpapermaster.view.adapters.MoviesRecyclerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.trehub.wallpapermaster.utils.Constants.MOVIE_ID;

public class SearchActivity extends BaseView implements IdItemClickListener {

    private EditText search;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Inject
    SearchPresenter searchPresenter;

    @Inject
    MoviesRecyclerAdapter moviesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        MyApplication.getSearchViewComponent().inject(this);
        searchPresenter.onAttach(this);
        bindView();
    }

    @Override
    public void bindView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.search);
        searchPresenter.setSearchTextListener();

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.moviesList);
        recyclerView.setLayoutManager(layoutManager);
    }

    public Observable<CharSequence> getTextChangeObservable() {
        Observable<CharSequence> searchChacngeObservable = RxTextView.textChanges(search)
                .debounce(300, TimeUnit.MILLISECONDS);
        return searchChacngeObservable;
    }

    public void setMoviesListAdapter(List<Result> resultList) {
        moviesRecyclerAdapter.init(this, resultList, this);
        recyclerView.setAdapter(moviesRecyclerAdapter);
    }

    public void clearList() {
        recyclerView.setAdapter(null);
    }

    @Override
    public void onClickItem(int id) {
        startMovieInfoActivity(id);
    }

    public void startMovieInfoActivity(int id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(MOVIE_ID, id);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        searchPresenter.onDispose();
        searchPresenter.onDetach();
    }
}
