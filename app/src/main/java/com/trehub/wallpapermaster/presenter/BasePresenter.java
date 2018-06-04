package com.trehub.wallpapermaster.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.trehub.wallpapermaster.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<View extends BaseView> {

    private View view;
    private CompositeDisposable compositeDisposable;

    public void onAttach(View view) {
        this.view = view;
    }

    public void onDetach() {
        view = null;
    }

    public View getView() {
        return view;
    }

    public CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
            return compositeDisposable;
        }
        return compositeDisposable;
    }

    public void onDispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
