package com.pogiba.core.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.R;
import com.pogiba.core.injection.module.FirebaseSignInModule;
import com.pogiba.core.ui.auth.ProfileActivity;
import com.pogiba.core.ui.auth.ResetPasswordActivity;
import com.pogiba.core.ui.auth.SignupActivity;
import com.pogiba.core.ui.base.BaseActivity;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements
    View.OnClickListener, LoginView {

  @Inject
  LoginPresenter presenter;

  private EditText inputEmail, inputPassword;

  private Button btnSignUp, btnLogin, btnReset;


  private static final String TAG = "LoginActivity";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject();
    presenter.attachView(this);

    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      updateUI(true);
    }

    // set the view now
    setContentView(R.layout.activity_login);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);

    btnSignUp = (Button) findViewById(R.id.btn_signup);
    btnLogin = (Button) findViewById(R.id.btn_login);
    btnReset = (Button) findViewById(R.id.btn_reset_password);
    SignInButton signInButton = (SignInButton) findViewById(R.id.btn_google_signin);
    signInButton.setSize(SignInButton.SIZE_STANDARD);

    btnSignUp.setOnClickListener(this);
    btnLogin.setOnClickListener(this);
    btnReset.setOnClickListener(this);
    signInButton.setOnClickListener(this);
  }

  private void inject() {
    activityComponent()
      .inject(this);
    getConfigPersistentComponent()
      .googleSignInComponent(new FirebaseSignInModule(presenter))
      .inject(presenter);
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

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_signup:
        signUp();
        break;
      case R.id.btn_reset_password:
        resetPassword();
        break;
      case R.id.btn_login:
        presenter.signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
        break;
      case R.id.btn_google_signin:
        googleSignIn();
        break;
    }
  }

  private void googleSignIn() {
    showProgressDialog();
    Intent signInIntent = presenter.getGoogleSignInIntent();
    startActivityForResult(signInIntent, presenter.RC_SIGN_IN);
  }

  public void updateUI(boolean signedIn) {
    if (signedIn) {
      startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
      finish();
    } else {
      //... some msg
    }
  }

  public void setErrorForInputPassword() {
    inputPassword.setError(getString(R.string.minimum_password));
  }

  private void signUp() {
    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
  }

  private void resetPassword() {
    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
  }
}

