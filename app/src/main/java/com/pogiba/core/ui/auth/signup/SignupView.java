package com.pogiba.core.ui.auth.signup;

import com.pogiba.core.ui.base.BaseView;

public interface SignupView extends BaseView {
  public void setErrorForInputPassword();

  public void goToProfileAndFinishActivity();
}
