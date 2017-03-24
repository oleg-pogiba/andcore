package com.pogiba.core.ui.auth.signup;

import com.pogiba.core.ui.base.MvpView;

public interface SignupView extends MvpView {
  public void setErrorForInputPassword();
  public void goToProfileAndFinishActivity();
}
