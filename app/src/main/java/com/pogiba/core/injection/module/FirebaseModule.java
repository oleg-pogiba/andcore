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
import com.pogiba.core.ui.Navigator;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

  private AppCompatActivity activity;

  public FirebaseModule(AppCompatActivity a) {
    activity = a;
  }

  @Provides
  AppCompatActivity provideActivity() {
    return activity;
  }

  @Provides
  GoogleApiClient provideGoogleApiClient(AppCompatActivity activity) {
   // AppCompatActivity context = (AppCompatActivity) presenter.getView();

    //Configure sign-in to request the user's ID, email address, and basic
    //profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions googleSignInOptions =
      new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by googleSignInOptions.
    return new GoogleApiClient.Builder(activity)
             .enableAutoManage(activity /* FragmentActivity */, (GoogleApiClient.OnConnectionFailedListener) activity /* OnConnectionFailedListener */)
             .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
             .build();
  }

  @Provides
  FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  FirebaseAuth.AuthStateListener provideFirebaseAuthAuthStateListener(final Navigator navigator) {
    return
      new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
          FirebaseUser user = firebaseAuth.getCurrentUser();
          if (user != null) {
            // User is signed in
            Log.d("FirebaseAuthStateLstnr", "onAuthStateChanged:signed_in:" + user.getUid());
            navigator.navigateToDefaultAndFinishCurrent(activity);
          } else {
            // User is signed out
            Log.d("FirebaseAuthStateLstnr", "onAuthStateChanged:signed_out");
            navigator.navigateToLogin(activity);
          }
        }
      };
  }

}
