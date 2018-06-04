package com.trehub.wallpapermaster.model;

import android.content.Context;

import com.trehub.wallpapermaster.model.pojo.primaryList.Cache;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.trehub.wallpapermaster.utils.Constants.LIST_ID;

public class DataManager {

    private Realm realm;
    private Context context;
    private RealmResults<Cache> realmResults;

    public DataManager(Context context) {
        this.context = context;
    }

    public void initRealm() {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);
    }

    public void updateRealm(Cache cache) {
        realm.beginTransaction();
        realmResults = realm.where(Cache.class).equalTo(LIST_ID, cache.getListId()).findAll();

        if (realmResults.size() == 0) {
            realm.insert(cache);
        } else {
            if (cache.getPage() == 1) {
                realmResults.deleteAllFromRealm();
                realm.insert(cache);
            } else if (cache.getPage() > realmResults.get(0).getPage()) {
                realmResults.get(0).setPage(cache.getPage());
                realmResults.get(0).getResults().addAll(cache.getResults());
            }
        }
        realm.commitTransaction();
    }

    public Flowable getRealmDataFlowable() {
        return Flowable.create(new FlowableOnSubscribe<RealmResults<Cache>>() {
            @Override
            public void subscribe(final FlowableEmitter<RealmResults<Cache>> emitter) throws Exception {
                realmResults = realm.where(Cache.class)
                        .findAllAsync();

                RealmChangeListener<RealmResults<Cache>> listener = new RealmChangeListener<RealmResults<Cache>>() {
                    @Override
                    public void onChange(RealmResults<Cache> element) {
                        if (!emitter.isCancelled()) {
                            emitter.onNext(element);
                        }
                    }
                };
                realmResults.addChangeListener(listener);
            }
        }, BackpressureStrategy.LATEST);
    }

    public boolean isRealmEmpty() {
        realm.beginTransaction();
        realmResults = realm.where(Cache.class).findAll();
        realm.commitTransaction();
        return realmResults.isEmpty();
    }

    public void closeRealm() {
        if (realmResults.isValid()) {
            realmResults.removeAllChangeListeners();
        }
        realm.close();
    }
}
