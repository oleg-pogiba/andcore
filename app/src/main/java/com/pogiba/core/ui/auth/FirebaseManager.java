package com.pogiba.core.ui.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

public class FirebaseManager {
  public static final String TAG = "FirebaseManager";

  private FirebaseAuth firebaseAuth;
  private GoogleApiClient googleApiClient;
  private BaseActivity activity;
  private FirebaseAuth.AuthStateListener authStateListener;
  private OnCompleteListener<AuthResult> onCompleteListenerAuthResult;

  public FirebaseManager(BaseActivity activity, FirebaseAuth auth, GoogleApiClient googleApiClient, FirebaseAuth.AuthStateListener authStateListener, OnCompleteListener<AuthResult> onCompleteListenerAuthResult) {
    this.firebaseAuth = auth;
    this.googleApiClient = googleApiClient;
    this.activity = activity;
    this.authStateListener = authStateListener;
    this.onCompleteListenerAuthResult = onCompleteListenerAuthResult;
  }

  public void createUserWithEmailAndPassword(String email, String password) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(onCompleteListenerAuthResult);
  }

  public void signOut() {
    //Firebase signOut
    firebaseAuth.signOut();
    //Google signOut
    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
      new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
          //...
        }
      });
    Toast.makeText(activity, "Sign Out...", Toast.LENGTH_SHORT).show();
  }

  public void addAuthStateListener() {
    firebaseAuth.addAuthStateListener(authStateListener);
  }

  public void removeAuthStateListener() {
    firebaseAuth.removeAuthStateListener(authStateListener);
  }

  public void sendPasswordResetEmail(String email, OnCompleteListener<Void> onCompleteListener) {
    firebaseAuth.sendPasswordResetEmail(email)
      .addOnCompleteListener(onCompleteListener);
  }

  public FirebaseAuth getFirebaseAuth() {
    return firebaseAuth;
  }

  public FirebaseAuth.AuthStateListener getAuthListener() {
    return authStateListener;
  }

  public GoogleApiClient getGoogleApiClient() {
    return googleApiClient;
  }

  public AppCompatActivity getActivity() {
    return activity;
  }

  public void authWithGoogle(GoogleSignInAccount account) {
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    getFirebaseAuth().signInWithCredential(credential)
      .addOnCompleteListener(onCompleteListenerAuthResult);
  }

  public void signInWithEmailAndPassword(String email, String password) {
    getFirebaseAuth().signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(onCompleteListenerAuthResult);
  }

  public Intent getGoogleSignInIntent() {
    return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
  }

}
