package com.pogiba.core.ui.auth.profile;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pogiba.core.ui.base.BasePresenter;
import com.pogiba.core.ui.auth.FirebaseManager;

import rx.Subscription;

public class ProfilePresenter extends BasePresenter<ProfileView> implements
  GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = "ProfilePresenter";
  private Context mContext;
  private Subscription mSubscription;

//  @Inject
//  FirebaseAuth mAuth;
//
//  @Inject
//  FirebaseAuth.AuthStateListener authListener;
//
//  @Inject
//  GoogleApiClient mGoogleApiClient;

  private FirebaseManager firebaseManager;

  public ProfilePresenter(Activity view) {
    mContext = view;
    super.attachView((ProfileView) view);
  }

  //get current user
  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

  public void setFirebaseManager(FirebaseManager firebaseManager) {
    this.firebaseManager = firebaseManager;
  }

  @Override
  public void attachView(ProfileView view) {
    super.attachView(view);
  }

  @Override
  public void detachView() {
    super.detachView();
    if (mSubscription != null) mSubscription.unsubscribe();
  }

  protected void sendPasswordResetEmail(String email) {
    OnCompleteListener<Void> onCompleteListener = new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
          getView().showMessage("Reset password email is sent!");
          getView().hideProgressDialog();
        } else {
          getView().showMessage("Failed to send reset email!");
          getView().hideProgressDialog();
        }
      }
    };

    getView().showProgressDialog();
    if (!email.equals("")) {
      firebaseManager.sendPasswordResetEmail(email, onCompleteListener);
    } else {
      getView().setErrorOldEmail("Enter email");
      getView().hideProgressDialog();
    }
  }

  protected void changePass(String password) {
    getView().showProgressDialog();


    if (user != null && !password.equals("")) {
      if (password.length() < 6) {
        getView().setErrorNewPassword("Password too short, enter minimum 6 characters");
        getView().hideProgressDialog();
      } else {
        user.updatePassword(password)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                getView().showMessage("Password is updated, sign in with new password!");
                firebaseManager.signOut();
                getView().hideProgressDialog();
              } else {
                getView().showMessage("Failed to update password!");
                getView().hideProgressDialog();
              }
            }
          });
      }
    } else if (password.equals("")) {
      getView().setErrorNewPassword("Enter password");
      getView().hideProgressDialog();
    }
  }

  protected void changeEmail(String email) {
    getView().showProgressDialog();
    if (user != null && !email.equals("")) {
      user.updateEmail(email)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              getView().showMessage("Email address is updated. Please sign in with new email id!");
              firebaseManager.signOut();
              getView().hideProgressDialog();
            } else {
              getView().showMessage("Failed to update email!");
              getView().hideProgressDialog();
            }
          }
        });
    } else if (email.equals("")) {
      getView().setErrorNewEmail("Enter email");
      getView().hideProgressDialog();
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  protected void onStart() {
    //mAuth.addAuthStateListener(authListener);
  }

  protected void onStop() {
//    if (authListener != null) {
//      mAuth.removeAuthStateListener(authListener);
//    }
  }
}
