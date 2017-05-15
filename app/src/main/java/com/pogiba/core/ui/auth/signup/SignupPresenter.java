package com.pogiba.core.ui.auth.signup;

import android.content.Context;
import android.text.TextUtils;

import com.pogiba.core.ui.auth.FirebaseManager;
import com.pogiba.core.ui.base.BasePresenter;

import rx.Subscription;

public class SignupPresenter extends BasePresenter<SignupView> {
  public static final String TAG = "SignupPresenter";
  private Context context;
  private Subscription subscription;
  private FirebaseManager firebaseManager;

  public SignupPresenter(Context context) {
    this.context = context;
  }

  @Override
  public void attachView(SignupView view) {
    super.attachView(view);
  }

  @Override
  public void detachView() {
    super.detachView();
    if (subscription != null) subscription.unsubscribe();
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
