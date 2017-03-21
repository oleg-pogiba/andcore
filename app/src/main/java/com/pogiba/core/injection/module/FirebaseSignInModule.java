package com.pogiba.core.injection.module;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pogiba.core.R;
import com.pogiba.core.ui.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseSignInModule {

  private LoginPresenter presenter;

  public FirebaseSignInModule(LoginPresenter p) {
    presenter = p;
  }

  @Provides
  LoginPresenter providePresenter() {
    return presenter;
  }

  @Provides
  GoogleApiClient provideGoogleApiClient(LoginPresenter LoginPresenter) {
    //Configure sign-in to request the user's ID, email address, and basic
    //profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions googleSignInOptions =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(LoginPresenter.getContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by googleSignInOptions.
    return new GoogleApiClient.Builder(presenter.getContext())
        .enableAutoManage((AppCompatActivity) LoginPresenter.getContext() /* FragmentActivity */, LoginPresenter /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
        .build();
  }

  @Provides
  FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  FirebaseAuth.AuthStateListener provideFirebaseAuthAuthStateListener() {
    return
        new FirebaseAuth.AuthStateListener() {
          @Override
          public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
              // User is signed in
              Log.d(LoginPresenter.TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
              // User is signed out
              Log.d(LoginPresenter.TAG, "onAuthStateChanged:signed_out");
            }
            // ...
          }
        };
  }

}
