package com.pogiba.core.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pogiba.core.R;
import com.pogiba.core.ui.auth.ResetPasswordActivity;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity implements SignupView {

  private final String TAG = "SignupActivity";
  @Inject
  SignupPresenter presenter;

  @BindView(R.id.email)
  EditText inputEmail;

  @BindView(R.id.password)
  EditText inputPassword;

  @OnClick(R.id.sign_in_button)
  public void signIn(View view) {
    finish();
  }

  @OnClick(R.id.btn_reset_password)
  public void resetPassword(View view) {
    startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
  }

  @OnClick(R.id.sign_up_button)
  public void signup(View view) {
    String email = inputEmail.getText().toString().trim();
    String password = inputPassword.getText().toString().trim();
    presenter.signup(email, password);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject();
    presenter.attachView(this);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);

  }

  private void inject() {
    activityComponent()
      .inject(this);
//    getConfigPersistentComponent()
//      .googleSignInComponent(new FirebaseSignInModule(presenter))
//      .inject(presenter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideProgressDialog();
  }

  public void setErrorForInputPassword() {
    inputPassword.setError(getString(R.string.minimum_password));
  }

  @Override
  public void goToProfileAndFinishActivity() {
    getNavigator().navigateToProfile(this);
    finish();
  }
}