package com.trehub.wallpapermaster.di.modules;

import com.trehub.wallpapermaster.view.adapters.MoviesRecyclerAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RecyclerAdapterModule {

    @Provides
    @Singleton
    public MoviesRecyclerAdapter getMoviesRecyclerAdapter() {
        MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter();
        return moviesRecyclerAdapter;
    }
}
