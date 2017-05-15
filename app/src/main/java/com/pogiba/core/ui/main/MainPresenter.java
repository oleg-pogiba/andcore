package com.pogiba.core.ui.main;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.pogiba.core.data.DataManager;
import com.pogiba.core.data.model.Ribot;
import com.pogiba.core.injection.scope.ConfigPersistent;
import com.pogiba.core.ui.auth.FirebaseManager;
import com.pogiba.core.ui.base.BasePresenter;
import com.pogiba.core.util.RxUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

  private final DataManager mDataManager;
  private Subscription mSubscription;
  private FirebaseManager firebaseManager;

  @Inject
  public MainPresenter(DataManager dataManager) {
    mDataManager = dataManager;
  }

  @Override
  public void attachView(MainMvpView view) {
    super.attachView(view);
    firebaseManager.addAuthStateListener();
  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();

    if (firebaseManager.getAuthListener() != null) {
      firebaseManager.removeAuthStateListener();
    }
  }

  public void loadRibots() {
    checkViewAttached();
    RxUtil.unsubscribe(mSubscription);
    mSubscription = mDataManager.getRibots()
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
