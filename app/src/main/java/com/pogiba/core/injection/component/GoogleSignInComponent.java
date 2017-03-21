package com.pogiba.core.injection.component;

import com.pogiba.core.injection.PerActivity;
import com.pogiba.core.injection.module.GoogleSignInModule;
import com.pogiba.core.ui.login.GoogleSignInPresenter;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Presenters across the application
 */
@PerActivity
@Subcomponent(modules = {GoogleSignInModule.class})
public interface GoogleSignInComponent {

  void inject(GoogleSignInPresenter presenter);
}
