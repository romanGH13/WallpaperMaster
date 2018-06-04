package com.trehub.wallpapermaster;

import android.app.Application;

import com.trehub.wallpapermaster.di.components.DaggerInfoViewComponent;
import com.trehub.wallpapermaster.di.components.DaggerMainViewComponent;
import com.trehub.wallpapermaster.di.components.DaggerNetComponent;
import com.trehub.wallpapermaster.di.components.DaggerSearchViewComponent;
import com.trehub.wallpapermaster.di.components.InfoViewComponent;
import com.trehub.wallpapermaster.di.components.MainViewComponent;
import com.trehub.wallpapermaster.di.components.NetComponent;
import com.trehub.wallpapermaster.di.components.SearchViewComponent;
import com.trehub.wallpapermaster.di.modules.InfoPresenterModule;
import com.trehub.wallpapermaster.di.modules.MainPresenterModule;
import com.trehub.wallpapermaster.di.modules.RealmAdapterModule;
import com.trehub.wallpapermaster.di.modules.YouTubeListAdapterModule;

public class MyApplication extends Application {

    private static NetComponent netComponent;
    private static MainViewComponent mainViewComponent;
    private static SearchViewComponent searchViewComponent;
    private static InfoViewComponent infoViewComponent;

    public static NetComponent getNetComponent() {
        return netComponent;
    }

    public static MainViewComponent getMainViewComponent() {
        return mainViewComponent;
    }

    public static SearchViewComponent getSearchViewComponent() {
        return searchViewComponent;
    }

    public static InfoViewComponent getInfoViewComponent() {
        return infoViewComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        netComponent = DaggerNetComponent.create();
        mainViewComponent = DaggerMainViewComponent.builder()
                .mainPresenterModule(new MainPresenterModule(getApplicationContext()))
                .realmAdapterModule(new RealmAdapterModule())
                .build();
        searchViewComponent = DaggerSearchViewComponent.create();
        infoViewComponent = DaggerInfoViewComponent.create();
    }
}
