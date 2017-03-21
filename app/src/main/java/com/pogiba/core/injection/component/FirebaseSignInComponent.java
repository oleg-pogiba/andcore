package com.pogiba.core.injection.component;

import com.pogiba.core.injection.PerActivity;
import com.pogiba.core.injection.module.FirebaseSignInModule;
import com.pogiba.core.ui.login.LoginPresenter;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Presenters across the application
 */
@PerActivity
@Subcomponent(modules = {FirebaseSignInModule.class})
public interface FirebaseSignInComponent {

  void inject(LoginPresenter presenter);
}
