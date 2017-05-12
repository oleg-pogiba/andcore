package com.pogiba.core.injection.component;

import dagger.Subcomponent;

import com.pogiba.core.injection.module.FirebaseModule;
import com.pogiba.core.injection.scope.PerActivity;
import com.pogiba.core.injection.module.ActivityModule;
import com.pogiba.core.ui.auth.login.LoginActivity;
import com.pogiba.core.ui.auth.profile.ProfileActivity;
import com.pogiba.core.ui.main.MainActivity;
import com.pogiba.core.ui.auth.signup.SignupActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, FirebaseModule.class})
public interface ActivityComponent {

  void inject(MainActivity mainActivity);

  void inject(LoginActivity loginActivity);

  void inject(SignupActivity signupActivity);

  void inject(ProfileActivity profileActivity);
}
