package com.pogiba.core.ui.auth.profile;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pogiba.core.R;
import com.pogiba.core.injection.module.FirebaseSignInModule;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements ProfileView {

  private static final String TAG = "ProfileActivity";

  @Inject
  ProfilePresenter presenter;

  @BindView(R.id.old_email)
  EditText oldEmail;
  @BindView(R.id.new_email)
  EditText newEmail;
  @BindView(R.id.password)
  EditText password;
  @BindView(R.id.newPassword)
  EditText newPassword;

  @BindView(R.id.change_email_button)
  Button btnChangeEmail;
  @BindView(R.id.change_password_button)
  Button btnChangePassword;
  @BindView(R.id.sending_pass_reset_button)
  Button btnSendResetEmail;
  @BindView(R.id.changeEmail)
  Button changeEmail;
  @BindView(R.id.changePass)
  Button changePassword;
  @BindView(R.id.send)
  Button sendEmail;
  @BindView(R.id.sign_out)
  Button signOut;

  @OnClick(R.id.change_email_button)
  public void btnChangeEmail(View view) {
    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.VISIBLE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.VISIBLE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.GONE);
  }

  @OnClick(R.id.change_password_button)
  public void change_password_button(View view) {
    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.VISIBLE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.VISIBLE);
    sendEmail.setVisibility(View.GONE);
  }

  @OnClick(R.id.sending_pass_reset_button)
  public void sending_pass_reset_button(View view) {
    oldEmail.setVisibility(View.VISIBLE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.changeEmail)
  public void changeEmail(View view) {
    presenter.changeEmail(newEmail.getText().toString().trim());
  }

  @OnClick(R.id.changePass)
  public void changePass(View view) {
    presenter.changePass(newPassword.getText().toString().trim());
  }

  @OnClick(R.id.send)
  public void send(View view) {
    presenter.send(oldEmail.getText().toString().trim());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getString(R.string.app_name));
    setSupportActionBar(toolbar);

    inject();
    presenter.attachView(this);
    ButterKnife.bind(this);

    setVisibility();
  }

  private void inject() {
    activityComponent()
      .inject(this);
    getConfigPersistentComponent()
      .googleSignInComponent(new FirebaseSignInModule(presenter))
      .inject(presenter);
  }

  private void setVisibility() {
    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.GONE);
  }

  @Override
  public void setErrorOldEmail(String str) {
    oldEmail.setError(str);
  }

  @Override
  public void setErrorNewPassword(String str) {
    newPassword.setError(str);
  }

  @Override
  public void setErrorNewEmail(String str) {
    newEmail.setError(str);
  }

  @OnClick(R.id.sign_out)
  public void signOut() {
    presenter.signOut();
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideProgressDialog();
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.onStop();
  }

}