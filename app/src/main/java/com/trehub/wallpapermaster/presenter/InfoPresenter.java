package com.trehub.wallpapermaster.presenter;

import com.trehub.wallpapermaster.MyApplication;
import com.trehub.wallpapermaster.model.pojo.info.MovieInfo;
import com.trehub.wallpapermaster.model.pojo.info.VideosResponse;
import com.trehub.wallpapermaster.utils.network.ApiInterface;
import com.trehub.wallpapermaster.view.InfoActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.trehub.wallpapermaster.utils.Constants.ERROR;
import static com.trehub.wallpapermaster.utils.Constants.ERROR_NET;

public class InfoPresenter extends BasePresenter<InfoActivity> {

    @Inject
    ApiInterface apiInterface;

    public InfoPresenter() {
        MyApplication.getNetComponent().inject(this);
    }

    public void getMovieInfo(int id) {
        if (id != 0) {
            if (isNetworkAvailable(getView().getContext())) {
                getCompositeDisposable().add(apiInterface.getMovieInfo(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieInfo>() {
                            @Override
                            public void onNext(@NonNull MovieInfo movieInfo) {
                                if (movieInfo.getPosterPath() != null) {
                                    getView().setPoster(movieInfo.getPosterPath().toString());
                                } else {
                                    getView().setPoster("");
                                }
                                getView().setDescription(movieInfo);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showMessage(ERROR);
                                getView().hideProgress();
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgress();
                                getVideos(getView().getMovieId());
                            }
                        }));
            } else {
                getView().showMessage(ERROR_NET);
                getView().hideProgress();
            }
        } else {
            getView().showMessage(ERROR);
            getView().hideProgress();
        }
    }

    public void getVideos(int id) {
        if (isNetworkAvailable(getView().getContext())) {
            getCompositeDisposable().add(apiInterface.getVideos(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<VideosResponse>() {
                        @Override
                        public void onNext(@NonNull VideosResponse videosResponse) {
                            if (videosResponse.getResults() != null && videosResponse.getResults().size() > 0) {
                                getView().showTrailersList(videosResponse.getResults());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().showMessage(ERROR);
                        }

                        @Override
                        public void onComplete() {
                        }
                    }));
        } else {
            getView().showMessage(ERROR_NET);
        }
    }
}
