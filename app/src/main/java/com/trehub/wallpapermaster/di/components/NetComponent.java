package com.trehub.wallpapermaster.di.components;

import com.trehub.wallpapermaster.di.modules.NetModule;
import com.trehub.wallpapermaster.presenter.InfoPresenter;
import com.trehub.wallpapermaster.presenter.MainPresenter;
import com.trehub.wallpapermaster.presenter.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class})
public interface NetComponent {
    void inject(MainPresenter moviesListPresenter);

    void inject(SearchPresenter searchPresenter);

    void inject(InfoPresenter movieInfoPresenter);
}
