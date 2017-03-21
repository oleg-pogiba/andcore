package com.pogiba.core.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pogiba.core.R;
import com.pogiba.core.injection.ActivityContext;
import com.pogiba.core.injection.ApplicationContext;
import com.pogiba.core.ui.login.GoogleSignInPresenter;

import javax.inject.Singleton;

@Module
public class ActivityModule {

  private Activity mActivity;

  public ActivityModule(Activity activity) {
    mActivity = activity;
  }

  @Provides
  Activity provideActivity() {
    return mActivity;
  }

  @Provides
  @ActivityContext
  Context providesContext() {
    return mActivity;
  }

  @Provides
  GoogleSignInPresenter providesGoogleSignInPresenter(Activity activity) {
    return new GoogleSignInPresenter(activity);
  }

}
