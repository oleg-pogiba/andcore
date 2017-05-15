package com.pogiba.core.ui.auth.signup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.ui.auth.FirebaseManager;
import com.pogiba.core.ui.base.BasePresenter;

import rx.Subscription;

public class SignupPresenter extends BasePresenter<SignupView> {
  public static final String TAG = "SignupPresenter";
  private Context mContext;
  private Subscription mSubscription;
  private FirebaseManager firebaseManager;

  public SignupPresenter(Context context) {
    this.mContext = context;
  }

  @Override
  public void attachView(SignupView view) {
    super.attachView(view);
  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();
  }

  protected void signup(String email, String password) {

    if (TextUtils.isEmpty(email)) {
      getView().showMessage("Enter email address!");
      return;
    }

    if (TextUtils.isEmpty(password)) {
      getView().showMessage("Enter password!");
      return;
    }

    if (password.length() < 6) {
      getView().setErrorForInputPassword();
      return;
    }

    getView().showProgressDialog();
    //create user
    firebaseManager.createUserWithEmailAndPassword(email, password);
  }

  public void setFirebaseManager(FirebaseManager firebaseManager) {
    this.firebaseManager = firebaseManager;
  }
}
