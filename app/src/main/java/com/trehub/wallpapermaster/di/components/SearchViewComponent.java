package com.trehub.wallpapermaster.di.components;

import com.trehub.wallpapermaster.di.modules.RecyclerAdapterModule;
import com.trehub.wallpapermaster.di.modules.SearchPresenterModule;
import com.trehub.wallpapermaster.view.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SearchPresenterModule.class, RecyclerAdapterModule.class})
public interface SearchViewComponent {
    void inject(SearchActivity searchActivity);
}
