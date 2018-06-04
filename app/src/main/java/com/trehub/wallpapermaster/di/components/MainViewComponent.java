package com.trehub.wallpapermaster.di.components;

import com.trehub.wallpapermaster.di.modules.MainPresenterModule;
import com.trehub.wallpapermaster.di.modules.RealmAdapterModule;
import com.trehub.wallpapermaster.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainPresenterModule.class, RealmAdapterModule.class})
public interface MainViewComponent {
    void inject(MainActivity mainActivity);
}

