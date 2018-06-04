package com.trehub.wallpapermaster.di.modules;

import com.trehub.wallpapermaster.presenter.InfoPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InfoPresenterModule {

    @Provides
    @Singleton
    public InfoPresenter getInfoPresenter() {
        InfoPresenter infoPresenter = new InfoPresenter();
        return infoPresenter;
    }
}
