package com.pogiba.core.ui.auth.login;

import com.pogiba.core.ui.base.MvpView;

public interface LoginView extends MvpView {
  public void updateUI(boolean b);

  public void setErrorForInputPassword();
}
