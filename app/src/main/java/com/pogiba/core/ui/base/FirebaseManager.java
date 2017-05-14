package com.pogiba.core.ui.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class FirebaseManager {
  public static final String TAG = "FirebaseManager";

  private FirebaseAuth firebaseAuth;
  private FirebaseAuth.AuthStateListener authListener;
  private GoogleApiClient googleApiClient;
  private BaseActivity activity;

  public FirebaseManager(BaseActivity activity, FirebaseAuth auth, FirebaseAuth.AuthStateListener authListener, GoogleApiClient googleApiClient) {
    this.firebaseAuth = auth;
    this.authListener = authListener;
    this.googleApiClient = googleApiClient;
    this.activity = activity;
    firebaseAuth.addAuthStateListener(authListener);
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
    firebaseAuth.addAuthStateListener(authListener);
  }

  public void removeAuthStateListener() {
    firebaseAuth.removeAuthStateListener(authListener);
  }

  public void sendPasswordResetEmail(String email, OnCompleteListener<Void> onCompleteListener) {
    firebaseAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener(onCompleteListener);
  }

  public FirebaseAuth getFirebaseAuth() {
    return firebaseAuth;
  }

  public FirebaseAuth.AuthStateListener getAuthListener() {
    return authListener;
  }

  public GoogleApiClient getGoogleApiClient() {
    return googleApiClient;
  }

  public AppCompatActivity getActivity() {
    return activity;
  }

  public void authWithGoogle(GoogleSignInAccount account, OnCompleteListener<AuthResult> onCompleteListener) {
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    getFirebaseAuth().signInWithCredential(credential)
        .addOnCompleteListener(onCompleteListener);
  }

  public void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
    getFirebaseAuth().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(onCompleteListener);
  }

  public Intent getGoogleSignInIntent() {
    return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
  }

}
