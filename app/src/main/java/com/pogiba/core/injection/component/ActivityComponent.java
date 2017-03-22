package com.pogiba.core.injection.component;

import dagger.Subcomponent;

import com.pogiba.core.injection.scope.PerActivity;
import com.pogiba.core.injection.module.ActivityModule;
import com.pogiba.core.ui.login.LoginActivity;
import com.pogiba.core.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

  void inject(MainActivity mainActivity);

  void inject(LoginActivity loginActivity);

}
