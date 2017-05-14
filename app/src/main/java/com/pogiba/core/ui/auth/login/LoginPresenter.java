package com.pogiba.core.ui.auth.login;

import android.app.Activity;
import android.content.Context;
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
import com.pogiba.core.ui.base.FirebaseManager;

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
    OnCompleteListener<AuthResult> onCompleteListener = new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        // If sign in fails, display a message to the user. If sign in succeeds
        // the firebaseAuth state listener will be notified and logic to handle the
        // signed in user can be handled in the listener.
        getView().hideProgressDialog();
        if (!task.isSuccessful()) {
          // there was an error
          getView().showMessage(mContext.getString(R.string.auth_failed));
        }
      }
    };

    firebaseManager.signInWithEmailAndPassword(email, password, onCompleteListener);
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

  protected void onStart() {
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

    firebaseManager.addAuthStateListener();
  }

  protected void onStop() {
    if (firebaseManager.getAuthListener() != null) {
      firebaseManager.removeAuthStateListener();
    }
  }

  protected void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount account = result.getSignInAccount();
      firebaseAuthWithGoogle(account);
    } else {
      // Signed out, show unauthenticated UI.
    }
    getView().hideProgressDialog();
  }

  private void handleFirebaseAuthWithGoogleResult(Task<AuthResult> task) {
    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

    // If sign in fails, display a message to the user. If sign in succeeds
    // the mAuth state listener will be notified and logic to handle the
    // signed in user can be handled in the listener.
    if (!task.isSuccessful()) {
      Log.w(TAG, "signInWithCredential", task.getException());
      getView().showMessage("Authentication failed.");
    } else {
      //...
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "firebaseAuthWithGoogle Id:" + account.getId());
    Log.d(TAG, "firebaseAuthWithGoogle IdToken:" + account.getIdToken());
    OnCompleteListener<AuthResult> onCompleteListener = new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        handleFirebaseAuthWithGoogleResult(task);
      }
    };
    firebaseManager.authWithGoogle(account, onCompleteListener);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  public Context getContext() {
    return mContext;
  }

  public void setFirebaseManager(FirebaseManager firebaseManager) {
    this.firebaseManager = firebaseManager;
  }
}
