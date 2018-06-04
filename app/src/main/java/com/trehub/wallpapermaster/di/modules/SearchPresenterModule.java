package com.trehub.wallpapermaster.di.modules;

import com.trehub.wallpapermaster.presenter.SearchPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchPresenterModule {

    @Provides
    @Singleton
    public SearchPresenter getSearchPresenter() {
        SearchPresenter searchPresenter = new SearchPresenter();
        return searchPresenter;
    }
}
