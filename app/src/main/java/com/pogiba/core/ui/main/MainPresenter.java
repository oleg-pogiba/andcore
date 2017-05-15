package com.pogiba.core.ui.main;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import com.pogiba.core.data.DataManager;
import com.pogiba.core.data.model.Ribot;
import com.pogiba.core.injection.scope.ConfigPersistent;
import com.pogiba.core.ui.auth.FirebaseManager;
import com.pogiba.core.ui.base.BasePresenter;
import com.pogiba.core.util.RxUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

  private Context context;
  private final DataManager dataManager;
  private Subscription subscription;
  private FirebaseManager firebaseManager;

  public MainPresenter(Activity view, DataManager dataManager) {
    this.dataManager = dataManager;
    context = view;
  }

  @Override
  public void attachView(MainView view) {
    super.attachView(view);
    firebaseManager.addAuthStateListener();
  }

  @Override
  public void detachView() {
    super.detachView();
    if (subscription != null) subscription.unsubscribe();

    if (firebaseManager.getAuthListener() != null) {
      firebaseManager.removeAuthStateListener();
    }
  }

  public void loadRibots() {
    checkViewAttached();
    RxUtil.unsubscribe(subscription);
    subscription = dataManager.getRibots()
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribeOn(Schedulers.io())
                      .subscribe(new Subscriber<List<Ribot>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                          Timber.e(e, "There was an error loading the ribots.");
                          getView().showError();
                        }

                        @Override
                        public void onNext(List<Ribot> ribots) {
                          if (ribots.isEmpty()) {
                            getView().showRibotsEmpty();
                          } else {
                            getView().showRibots(ribots);
                          }
                        }
                      });
  }

  public void setFirebaseManager(FirebaseManager firebaseManager) {
    this.firebaseManager = firebaseManager;
  }
}
