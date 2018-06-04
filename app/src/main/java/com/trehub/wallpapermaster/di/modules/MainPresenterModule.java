package com.trehub.wallpapermaster.di.modules;

import android.content.Context;

import com.trehub.wallpapermaster.model.DataManager;
import com.trehub.wallpapermaster.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPresenterModule {

    private Context context;

    public MainPresenterModule(Context context) {
        this.context = context;
    }

    @Provides
    //@Singleton
    public MainPresenter getMainPresenter(DataManager dataManager) {
        MainPresenter mainPresenter = new MainPresenter(dataManager);
        return mainPresenter;
    }

    @Provides
    @Singleton
    public DataManager getDataManager(Context context) {
        DataManager dataManager = new DataManager(context);
        return dataManager;
    }

    @Provides
    @Singleton
    public Context getAppContext() {
        return context;
    }
}
