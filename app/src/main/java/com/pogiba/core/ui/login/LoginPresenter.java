package com.pogiba.core.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.R;
import com.pogiba.core.data.DataManager;
import com.pogiba.core.injection.ApplicationContext;
import com.pogiba.core.injection.ConfigPersistent;
import com.pogiba.core.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginView> {

  private final DataManager mDataManager;
  private final Context mContext;
  private Subscription mSubscription;

  //todo: di
  private FirebaseAuth firebaseAuth;

  // inject dependencies from ApplicationComponent
  @Inject
  public LoginPresenter(DataManager dataManager, @ApplicationContext Context context) {
    mDataManager = dataManager;
    mContext = context;
    //todo: di
    firebaseAuth = FirebaseAuth.getInstance();
  }

  @Override
  public void attachView(LoginView view) {
    super.attachView(view);
  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();
  }

  public FirebaseAuth getFirebaseAuth() {
    return firebaseAuth;
  }

  public void signIn(String email, String password) {

    if (TextUtils.isEmpty(email)) {
      getView().showMessage("Enter email address!");
      return;
    }

    if (TextUtils.isEmpty(password)) {
      getView().showMessage( "Enter password!");
      return;
    }

    if (password.length() < 6) {
      getView().setErrorForInputPassword();
    }

    getView().showProgressDialog();
    //authenticate user
    getFirebaseAuth().signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          // If sign in fails, display a message to the user. If sign in succeeds
          // the firebaseAuth state listener will be notified and logic to handle the
          // signed in user can be handled in the listener.
          getView().hideProgressDialog();
          if (!task.isSuccessful()) {
            // there was an error
            getView().showMessage(mContext.getString(R.string.auth_failed));
          } else {
            getView().updateUI(true);
          }
        }
      });
  }


  public void loadRibots() {

//        checkViewAttached();
//        RxUtil.unsubscribe(mSubscription);
//        mSubscription = mDataManager.getRibots()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<Ribot>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e(e, "There was an error loading the ribots.");
//                        getView().showError();
//                    }
//
//                    @Override
//                    public void onNext(List<Ribot> ribots) {
//                        if (ribots.isEmpty()) {
//                            getView().showRibotsEmpty();
//                        } else {
//                            getView().showRibots(ribots);
//                        }
//                    }
//                });
  }

}
