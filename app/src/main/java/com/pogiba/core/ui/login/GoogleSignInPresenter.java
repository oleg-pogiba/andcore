package com.pogiba.core.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pogiba.core.injection.ConfigPersistent;
import com.pogiba.core.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

@ConfigPersistent
public class GoogleSignInPresenter extends BasePresenter<LoginView>  implements
  GoogleApiClient.OnConnectionFailedListener{
  private static final String TAG = "GoogleSignInPresenter";
  public static final int RC_SIGN_IN = 9001;

  private Context mContext;
  private Subscription mSubscription;

  @Inject
  public GoogleApiClient mGoogleApiClient;

  private FirebaseAuth.AuthStateListener mAuthListener;

  //@Inject
  //public GoogleSignInOptions googleSignInOptions;

  // firebase auth
  //todo: di
  private FirebaseAuth mAuth;

  public FirebaseAuth getFirebaseAuth() {
    return mAuth;
  }

  public GoogleSignInPresenter(Context context) {
    mContext = context;
    googleInit();
  }

  private void googleInit() {

    // firebase
    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
          Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
          // User is signed out
          Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...


      }
    };
  }

  public Intent getGoogleSignInIntent() {
    return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
  }


  @Override
  public void attachView(LoginView view) {
    super.attachView(view);


    // firebase auth
    //todo: di
    mAuth = FirebaseAuth.getInstance();

  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();
  }

  protected void onStart() {
    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
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
    getFirebaseAuth().addAuthStateListener(mAuthListener);
  }

  protected void onStop() {
    if (mAuthListener != null) {
      getFirebaseAuth().removeAuthStateListener(mAuthListener);
    }
  }

  protected void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount account = result.getSignInAccount();
      firebaseAuthWithGoogle(account);
      //updateUI(true);
    } else {
      // Signed out, show unauthenticated UI.
      getView().updateUI(false);
    }

    getView().hideProgressDialog();
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "firebaseAuthWithGoogle Id:" + account.getId());
    Log.d(TAG, "firebaseAuthWithGoogle IdToken:" + account.getIdToken());

    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    getFirebaseAuth().signInWithCredential(credential)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          handleFirebaseAuthWithGoogleResult(task);
        }
      });
  }

  private void handleFirebaseAuthWithGoogleResult(Task<AuthResult> task) {
    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

    // If sign in fails, display a message to the user. If sign in succeeds
    // the mAuth state listener will be notified and logic to handle the
    // signed in user can be handled in the listener.
    if (!task.isSuccessful()) {
      Log.w(TAG, "signInWithCredential", task.getException());
      ///// todo
      Toast.makeText(mContext, "Authentication failed.",
        Toast.LENGTH_SHORT).show();
      getView().updateUI(false);
    }
    getView().updateUI(true);

    // ...

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
}
