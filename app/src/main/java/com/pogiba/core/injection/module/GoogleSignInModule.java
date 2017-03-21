package com.pogiba.core.injection.module;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pogiba.core.R;
import com.pogiba.core.injection.ActivityContext;
import com.pogiba.core.ui.login.GoogleSignInPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class GoogleSignInModule {

  private GoogleSignInPresenter presenter;

  public GoogleSignInModule(GoogleSignInPresenter p) {
    presenter = p;
  }

  @Provides
  GoogleSignInPresenter providePresenter() {
    return presenter;
  }

  @Provides
  GoogleApiClient provideGoogleApiClient(GoogleSignInPresenter googleSignInPresenter) {
    //Configure sign-in to request the user's ID, email address, and basic
    //profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions googleSignInOptions =
      new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleSignInPresenter.getContext().getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by googleSignInOptions.
    return new GoogleApiClient.Builder(presenter.getContext())
             .enableAutoManage((AppCompatActivity) googleSignInPresenter.getContext() /* FragmentActivity */, googleSignInPresenter /* OnConnectionFailedListener */)
             .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
             .build();
  }
}
