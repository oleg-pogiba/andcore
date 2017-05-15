package com.pogiba.core.injection.module;

import android.app.Activity;
import android.content.Context;

import com.pogiba.core.data.DataManager;
import com.pogiba.core.injection.qualifier.ActivityContext;
import com.pogiba.core.ui.auth.login.LoginPresenter;
import com.pogiba.core.ui.auth.profile.ProfilePresenter;
import com.pogiba.core.ui.auth.signup.SignupPresenter;
import com.pogiba.core.ui.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  Activity provideActivity() {
    return activity;
  }

  @Provides
  @ActivityContext
  Context providesContext() {
    return activity;
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

  @Provides
  MainPresenter providesMainPresenter(Activity activity, DataManager dataManager) {
    return new MainPresenter(activity, dataManager);
  }
}
