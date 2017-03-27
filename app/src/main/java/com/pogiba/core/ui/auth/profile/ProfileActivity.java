package com.pogiba.core.ui.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pogiba.core.R;
import com.pogiba.core.ui.base.BaseActivity;
import com.pogiba.core.ui.auth.login.LoginActivity;
import com.pogiba.core.ui.auth.signup.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements
    GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "ProfileActivity";

  private FirebaseAuth.AuthStateListener authListener;
  private FirebaseAuth mAuth;
  private GoogleApiClient mGoogleApiClient;
  //get current user
  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
    showProgressDialog();
    if (user != null && !newEmail.getText().toString().trim().equals("")) {
      user.updateEmail(newEmail.getText().toString().trim())
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Toast.makeText(ProfileActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
              signOut();
              hideProgressDialog();
            } else {
              Toast.makeText(ProfileActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
              hideProgressDialog();
            }
          }
        });
    } else if (newEmail.getText().toString().trim().equals("")) {
      newEmail.setError("Enter email");
      hideProgressDialog();
    }
  }

  @OnClick(R.id.changePass)
  public void changePass(View view) {
    showProgressDialog();
    if (user != null && !newPassword.getText().toString().trim().equals("")) {
      if (newPassword.getText().toString().trim().length() < 6) {
        newPassword.setError("Password too short, enter minimum 6 characters");
        hideProgressDialog();
      } else {
        user.updatePassword(newPassword.getText().toString().trim())
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                signOut();
                hideProgressDialog();
              } else {
                Toast.makeText(ProfileActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
              }
            }
          });
      }
    } else if (newPassword.getText().toString().trim().equals("")) {
      newPassword.setError("Enter password");
      hideProgressDialog();
    }
  }

  @OnClick(R.id.send)
  public void send(View view) {
    showProgressDialog();
    if (!oldEmail.getText().toString().trim().equals("")) {
      mAuth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Toast.makeText(ProfileActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
              hideProgressDialog();
            } else {
              Toast.makeText(ProfileActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
              hideProgressDialog();
            }
          }
        });
    } else {
      oldEmail.setError("Enter email");
      hideProgressDialog();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getString(R.string.app_name));
    setSupportActionBar(toolbar);

    //get firebase mAuth instance
    mAuth = FirebaseAuth.getInstance();

    ButterKnife.bind(this);

    //Configure sign-in to request the user's ID, email address, and basic
    //profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by googleSignInOptions.
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    authListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
          // user mAuth state is changed - user is null
          // launch login activity
          startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
          finish();
        }
      }
    };

    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.GONE);
  }

  @OnClick(R.id.sign_out)
  public void signOut() {
    //Firebase signOut
    mAuth.signOut();
    //Google signOut
    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
        new ResultCallback<Status>() {
          @Override
          public void onResult(Status status) {
            //updateUI(false);
          }
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideProgressDialog();
  }

  @Override
  public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(authListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (authListener != null) {
      mAuth.removeAuthStateListener(authListener);
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

}