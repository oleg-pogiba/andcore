package com.pogiba.core.ui.auth.profile;

import com.pogiba.core.ui.base.BaseView;

public interface ProfileView extends BaseView {
  public void setErrorOldEmail(String str);

  public void setErrorNewPassword(String str);

  public void setErrorNewEmail(String str);
}
