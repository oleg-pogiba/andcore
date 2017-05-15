package com.pogiba.core.ui.auth.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pogiba.core.R;
import com.pogiba.core.ui.auth.FirebaseManager;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity implements SignupView {

  private final String TAG = "SignupActivity";
  @Inject
  SignupPresenter presenter;

  @Inject
  FirebaseManager firebaseManager;

  @BindView(R.id.email)
  EditText inputEmail;

  @BindView(R.id.password)
  EditText inputPassword;

  @OnClick(R.id.sign_in_button)
  public void signin(View view) {
    finish();
  }

  @OnClick(R.id.btn_reset_password)
  public void resetPassword(View view) {
    getNavigator().navigateToResetPassword(this);
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
    presenter.setFirebaseManager(firebaseManager);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.attachView(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.detachView();
  }

  private void inject() {
    activityComponent()
      .inject(this);
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