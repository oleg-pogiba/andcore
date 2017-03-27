package com.pogiba.core.ui.auth.signup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.ui.base.BasePresenter;

import rx.Subscription;

public class SignupPresenter extends BasePresenter<SignupView> {
  public static final String TAG = "SignupPresenter";
  private Context mContext;
  private Subscription mSubscription;

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
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          getView().hideProgressDialog();
          getView().showMessage("createUserWithEmail:onComplete:" + task.isSuccessful());
          // If sign in fails, display a message to the user. If sign in succeeds
          // the auth state listener will be notified and logic to handle the
          // signed in user can be handled in the listener.
          if (!task.isSuccessful()) {
            getView().showMessage("Authentication failed." + task.getException());
            Log.e(TAG, "Authentication failed." + task.getException());
          } else {
            getView().goToProfileAndFinishActivity();
          }
        }
      });
  }
}
