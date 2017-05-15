package com.pogiba.core.ui.auth.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pogiba.core.R;
import com.pogiba.core.data.DataManager;
import com.pogiba.core.injection.scope.ConfigPersistent;
import com.pogiba.core.ui.base.BasePresenter;
import com.pogiba.core.ui.auth.FirebaseManager;

import javax.inject.Inject;

import rx.Subscription;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginView> implements
    GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "LoginPresenter";
  protected static final int RC_SIGN_IN = 9001;

  private Context mContext;
  private Subscription mSubscription;
  private FirebaseManager firebaseManager;

  @Inject
  DataManager mDataManager;

  public LoginPresenter(Activity view) {
    mContext = view;
    super.attachView((LoginView) view);
  }

  protected void signIn(String email, String password) {

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
    //authenticate user
    firebaseManager.signInWithEmailAndPassword(email, password);
  }

  @Override
  public void attachView(LoginView view) {
    super.attachView(view);
    firebaseManager.addAuthStateListener();

    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(firebaseManager.getGoogleApiClient());
    if (opr.isDone()) {
      // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
      // and the GoogleSignInResult will be available instantly.
      Log.d(TAG, "Got cached sign-in");
      GoogleSignInResult result = opr.get();
      handleSignInResult(result);
    } else {
      // If the user has not previously signed in on this device or the sign-in has expired,
      // this asynchronous branch will attempt to sign in the user silently.  Cross-device
      // single sign-on will occur in this branch.
      getView().showProgressDialog();
      opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
        @Override
        public void onResult(GoogleSignInResult googleSignInResult) {
          getView().hideProgressDialog();
          handleSignInResult(googleSignInResult);
        }
      });
    }
  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();

    if (firebaseManager.getAuthListener() != null) {
      firebaseManager.removeAuthStateListener();
    }
  }

  public void handleSignInResult(GoogleSignInResult googleSignInResult){
    Log.d(TAG, "handleSignInResult:" + googleSignInResult.isSuccess());
    if (googleSignInResult.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount account = googleSignInResult.getSignInAccount();
      firebaseAuthWithGoogle(account);
    } else {
      // Signed out, show unauthenticated UI.
    }
  }

  public void checkGoogleSignInApiResult(int requestCode, Intent data) {
    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == LoginPresenter.RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }


  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "firebaseAuthWithGoogle Id:" + account.getId());
    Log.d(TAG, "firebaseAuthWithGoogle IdToken:" + account.getIdToken());
    firebaseManager.authWithGoogle(account);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  public Context getContext() {
    return mContext;
  }

  public void setFirebaseManager(FirebaseManager firebaseManager) {
    this.firebaseManager = firebaseManager;
  }
}
