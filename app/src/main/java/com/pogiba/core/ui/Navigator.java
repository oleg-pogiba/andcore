package com.pogiba.core.ui;


import android.content.Context;
import android.content.Intent;


import com.pogiba.core.injection.scope.PerActivity;
import com.pogiba.core.ui.auth.profile.ProfileActivity;
import com.pogiba.core.ui.auth.reset_password.ResetPasswordActivity;
import com.pogiba.core.ui.auth.signup.SignupActivity;

import javax.inject.Inject;

/**
 * Class used to navigate through the application.
 */
@PerActivity
public class Navigator {

  @Inject
  public Navigator() {
    //empty
  }

  /**
   * Goes to the sign up screen.
   *
   * @param context A Context needed to open the destiny activity.
   */
  public void navigateToSignUp(Context context) {
    if (context != null) {
      Intent intentToLaunch = new Intent(context, SignupActivity.class);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToResetPassword(Context context) {
    if (context != null) {
      Intent intentToLaunch = new Intent(context, ResetPasswordActivity.class);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToProfile(Context context) {
    if (context != null) {
      Intent intentToLaunch = new Intent(context, ProfileActivity.class);
      context.startActivity(intentToLaunch);
    }
  }
}