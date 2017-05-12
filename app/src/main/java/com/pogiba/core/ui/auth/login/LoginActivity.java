package com.pogiba.core.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.pogiba.core.R;
import com.pogiba.core.injection.module.FirebaseSignInModule;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {
  private static final String TAG = "LoginActivity";

  @Inject
  LoginPresenter presenter;

  @BindView(R.id.email)
  EditText inputEmail;
  @BindView(R.id.password)
  EditText inputPassword;

  @OnClick(R.id.btn_signup)
  public void signUp(View view) {
    getNavigator().navigateToSignUp(this);
  }

  @OnClick(R.id.btn_reset_password)
  public void resetPassword(View view) {
    getNavigator().navigateToResetPassword(this);
  }

  @OnClick(R.id.btn_login)
  public void login(View view) {
    presenter.signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
  }

  @OnClick(R.id.btn_google_signin)
  public void signIn(View view) {
    googleSignIn();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inject();
    presenter.attachView(this);

    // set the view now
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == LoginPresenter.RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      presenter.handleSignInResult(result);
    }
  }

  public void setErrorForInputPassword() {
    inputPassword.setError(getString(R.string.minimum_password));
  }

  private void inject() {
    activityComponent()
      .inject(this);
    getConfigPersistentComponent()
      .googleSignInComponent(new FirebaseSignInModule(presenter))
      .inject(presenter);
  }

  private void googleSignIn() {
    showProgressDialog();
    Intent signInIntent = presenter.getGoogleSignInIntent();
    startActivityForResult(signInIntent, presenter.RC_SIGN_IN);
  }
}

