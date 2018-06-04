package com.trehub.wallpapermaster.di.components;

import com.trehub.wallpapermaster.di.modules.InfoPresenterModule;
import com.trehub.wallpapermaster.di.modules.YouTubeListAdapterModule;
import com.trehub.wallpapermaster.view.InfoActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {InfoPresenterModule.class, YouTubeListAdapterModule.class})
public interface InfoViewComponent {
    void inject(InfoActivity infoActivity);
}
