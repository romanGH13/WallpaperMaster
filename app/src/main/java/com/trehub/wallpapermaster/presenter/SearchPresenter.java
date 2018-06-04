package com.trehub.wallpapermaster.presenter;

import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.model.pojo.primaryList.MoviesList;
import com.trehub.wallpapermaster.utils.network.ApiInterface;
import com.trehub.wallpapermaster.view.SearchActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.trehub.wallpapermaster.utils.Constants.ERROR;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_NET;

public class SearchPresenter extends BasePresenter<SearchActivity> {

    @Inject
    ApiInterface apiInterface;

    public SearchPresenter() {
        MyApplication.getNetComponent().inject(this);
    }

    public void setSearchTextListener() {
        getCompositeDisposable().add(getView().getTextChangeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        startSearchMovie(charSequence.toString().trim());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showMessage(ERROR);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    public void startSearchMovie(String name) {
        if (isNetworkAvailable(getView().getContext())) {
            if (name.equals("")) {
                getView().clearList();
            } else {
                getView().showProgress();
                getCompositeDisposable().add(apiInterface.searchMovie(name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MoviesList>() {
                            @Override
                            public void onNext(@NonNull MoviesList moviesList) {
                                getView().setMoviesListAdapter(moviesList.getResults());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgress();
                                getView().showMessage(ERROR);
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgress();
                            }
                        }));
            }
        } else {
            getView().showMessage(ERROR_NET);
        }
    }
}