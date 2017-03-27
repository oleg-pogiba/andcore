package com.pogiba.core.injection.module;

import android.app.Activity;
import android.content.Context;

import com.pogiba.core.data.model.Profile;
import com.pogiba.core.injection.qualifier.ActivityContext;
import com.pogiba.core.ui.auth.login.LoginPresenter;
import com.pogiba.core.ui.auth.profile.ProfilePresenter;
import com.pogiba.core.ui.auth.signup.SignupPresenter;

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
  LoginPresenter providesLoginPresenter(Activity activity) {
    return new LoginPresenter(activity);
  }

  @Provides
  SignupPresenter providesSignUpPresenter(Activity activity) {
    return new SignupPresenter(activity);
  }

  @Provides
  ProfilePresenter providesProfilePresenter(Activity activity) {
    return new ProfilePresenter(activity);
  }
}
