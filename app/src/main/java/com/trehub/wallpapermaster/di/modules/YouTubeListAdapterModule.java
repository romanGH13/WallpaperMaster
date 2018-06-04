package com.trehub.wallpapermaster.di.modules;

import com.trehub.wallpapermaster.view.adapters.YouTubeListAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class YouTubeListAdapterModule {
    @Provides
    @Singleton
    public YouTubeListAdapter getYouTubeListAdapter() {
        YouTubeListAdapter youTubeListAdapter = new YouTubeListAdapter();
        return youTubeListAdapter;
    }
}
