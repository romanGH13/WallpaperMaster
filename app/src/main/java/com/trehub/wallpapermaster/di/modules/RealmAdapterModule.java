package com.trehub.wallpapermaster.di.modules;

import com.trehub.wallpapermaster.model.pojo.primaryList.Cache;
import com.trehub.wallpapermaster.view.adapters.MoviesRealmAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmResults;

@Module
public class RealmAdapterModule {

    private RealmResults<Cache> realmResults;

    @Provides
    @Singleton
    public MoviesRealmAdapter getMoviesRealmAdapter() {
        MoviesRealmAdapter moviesRealmAdapter = new MoviesRealmAdapter(realmResults);
        return moviesRealmAdapter;
    }
}
