package com.pogiba.core.ui.auth.profile;

import com.bumptech.glide.load.model.StringLoader;
import com.pogiba.core.ui.base.MvpView;

public interface ProfileView extends MvpView {
  public void setErrorOldEmail(String str);
  public void setErrorNewPassword(String str);
  public void setErrorNewEmail(String str);
}
