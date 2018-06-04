package com.trehub.wallpapermaster.presenter;

import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.model.DataManager;
import com.trehub.wallpapermaster.model.pojo.primaryList.Cache;
import com.trehub.wallpapermaster.model.pojo.primaryList.MoviesList;
import com.trehub.wallpapermaster.model.pojo.primaryList.Result;
import com.trehub.wallpapermaster.model.pojo.secondaryList.SecondaryList;
import com.trehub.wallpapermaster.utils.network.ApiInterface;
import com.trehub.wallpapermaster.view.MainActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.trehub.wallpapermaster.utils.Constants.ERROR;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_NET;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_NOW_PLAYING;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_POPULAR;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_TOP;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_UPCOMING;
import static com.trehub.wallpapermaster.utils.Constants.NOW_PLAYING;
import static com.trehub.wallpapermaster.utils.Constants.POPULAR;
import static com.trehub.wallpapermaster.utils.Constants.TOP;
import static com.trehub.wallpapermaster.utils.Constants.UPCOMING;

public class MainPresenter extends BasePresenter<MainActivity> {

    private DataManager dataManager;
    private final int startPage = 1;

    @Inject
    ApiInterface apiInterface;

    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        dataManager.initRealm();

        MyApplication.getNetComponent().inject(this);
    }

    public void startLoading() {
        if (dataManager.isRealmEmpty()) {
            getView().showProgress();
        }

        if (isNetworkAvailable(getView().getContext())) {
            getView().setIsInited(false);
            getTopRatedList(startPage);
            getPopularList(startPage);
            getNowPlayingList(startPage);
            getUpcomingList(startPage);
        } else {
            getView().showMessage(ERROR_NET);
            getView().hideProgress();
        }
    }

    public void getTopRatedList(int page) {
        getCompositeDisposable().add(apiInterface.getTopRated(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MoviesList>() {
                    @Override
                    public void onNext(@NonNull MoviesList moviesList) {
                        dataManager.updateRealm(makeNewCache(TOP, moviesList.getResults(),
                                moviesList.getPage(), moviesList.getTotalPages()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                        getView().showMessage(ERROR_TOP);
                        getView().hideProgressInRecycler();
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                    }
                }));
    }

    public void getPopularList(int page) {
        getCompositeDisposable().add(apiInterface.getPopular(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MoviesList>() {
                    @Override
                    public void onNext(@NonNull MoviesList moviesList) {
                        dataManager.updateRealm(makeNewCache(POPULAR, moviesList.getResults(),
                                moviesList.getPage(), moviesList.getTotalPages()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                        getView().showMessage(ERROR_POPULAR);
                        getView().hideProgressInRecycler();
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                    }
                }));
    }

    public void getUpcomingList(int page) {
        getCompositeDisposable().add(apiInterface.getUpcoming(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SecondaryList>() {
                    @Override
                    public void onNext(SecondaryList secondaryList) {
                        dataManager.updateRealm(makeNewCache(UPCOMING, secondaryList.getResults(),
                                secondaryList.getPage(), secondaryList.getTotalPages()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                        getView().showMessage(ERROR_UPCOMING);
                        getView().hideProgressInRecycler();
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                    }
                }));
    }

    public void getNowPlayingList(int page) {
        getCompositeDisposable().add(apiInterface.getNowPlaying(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SecondaryList>() {
                    @Override
                    public void onNext(SecondaryList secondaryList) {
                        dataManager.updateRealm(makeNewCache(NOW_PLAYING, secondaryList.getResults(),
                                secondaryList.getPage(), secondaryList.getTotalPages()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                        getView().showMessage(ERROR_NOW_PLAYING);
                        getView().hideProgressInRecycler();
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgress();
                        getView().setIsLoading(false);
                    }
                }));
    }

    public void setAutoUpdateList() {
        getCompositeDisposable().add((Disposable) dataManager.getRealmDataFlowable()
                .subscribeWith(new DisposableSubscriber<RealmResults<Cache>>() {
                    @Override
                    public void onNext(RealmResults<Cache> realmResults) {
                        if (realmResults != null && !realmResults.isEmpty() && realmResults.isValid()) {
                            getView().initMoviesListAdapter(realmResults);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        getView().showMessage(ERROR);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }


    public Cache makeNewCache(int listId, RealmList<Result> results, int page, int totalPages) {
        Cache cache = new Cache();
        cache.setPage(page);
        cache.setTotalPages(totalPages);
        cache.setListId(listId);
        cache.setResults(results);
        return cache;
    }

    public void onDetach() {
        super.onDetach();
        dataManager.closeRealm();
    }
}
