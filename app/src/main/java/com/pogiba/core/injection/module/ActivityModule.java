package com.pogiba.core.injection.module;

import android.app.Activity;
import android.content.Context;

import com.pogiba.core.injection.ActivityContext;
import com.pogiba.core.ui.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

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
  LoginPresenter providesGoogleSignInPresenter(Activity activity) {
    return new LoginPresenter(activity);
  }

}
