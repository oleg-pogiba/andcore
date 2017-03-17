package com.pogiba.core.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pogiba.core.R;
import com.pogiba.core.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements
    GoogleApiClient.OnConnectionFailedListener,
    View.OnClickListener {

  private EditText inputEmail, inputPassword;

  private Button btnSignUp, btnLogin, btnReset;


  private static final String TAG = "LoginActivity";
  private static final int RC_SIGN_IN = 9001;

  private GoogleApiClient mGoogleApiClient;


  private FirebaseAuth.AuthStateListener mAuthListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getFirebaseAuth().getCurrentUser() != null) {
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
    SignInButton signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
    signInButton.setSize(SignInButton.SIZE_STANDARD);


    btnSignUp.setOnClickListener(this);
    btnLogin.setOnClickListener(this);
    btnReset.setOnClickListener(this);
    signInButton.setOnClickListener(this);

    //Configure sign-in to request the user's ID, email address, and basic
    //profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by gso.
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    //Customize sign-in button. The sign-in button can be displayed in
    //multiple sizes and color schemes. It can also be contextually
    //rendered based on the requested scopes. For example. a red button may
    //be displayed when Google+ scopes are requested, but a white button
    //may be displayed when only basic profile is requested. Try adding the
    //Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
    //difference.
    signInButton.setScopes(gso.getScopeArray());

    // firebase
    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
          Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
          // User is signed out
          Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...
      }
    };
  }

  @Override
  public void onStart() {
    super.onStart();

    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    if (opr.isDone()) {
      // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
      // and the GoogleSignInResult will be available instantly.
      Log.d(TAG, "Got cached sign-in");
      GoogleSignInResult result = opr.get();
      handleSignInResult(result);
    } else {
      // If the user has not previously signed in on this device or the sign-in has expired,
      // this asynchronous branch will attempt to sign in the user silently.  Cross-device
      // single sign-on will occur in this branch.
      showProgressDialog();
      opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
        @Override
        public void onResult(GoogleSignInResult googleSignInResult) {
          hideProgressDialog();
          handleSignInResult(googleSignInResult);
        }
      });
    }
    getFirebaseAuth().addAuthStateListener(mAuthListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mAuthListener != null) {
      getFirebaseAuth().removeAuthStateListener(mAuthListener);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount account = result.getSignInAccount();
      firebaseAuthWithGoogle(account);
      //updateUI(true);
    } else {
      // Signed out, show unauthenticated UI.
      updateUI(false);
    }

    hideProgressDialog();
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "firebaseAuthWithGoogle Id:" + account.getId());
    Log.d(TAG, "firebaseAuthWithGoogle IdToken:" + account.getIdToken());

    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    getFirebaseAuth().signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            handleFirebaseAuthWithGoogleResult(task);
          }
        });
  }

  private void handleFirebaseAuthWithGoogleResult(Task<AuthResult> task) {
    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

    // If sign in fails, display a message to the user. If sign in succeeds
    // the mAuth state listener will be notified and logic to handle the
    // signed in user can be handled in the listener.
    if (!task.isSuccessful()) {
      Log.w(TAG, "signInWithCredential", task.getException());
      Toast.makeText(LoginActivity.this, "Authentication failed.",
          Toast.LENGTH_SHORT).show();
      updateUI(false);
    }
    updateUI(true);

    // ...
  }

  // [START signIn]
  private void googleSignIn() {
    showProgressDialog();
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }


  private void updateUI(boolean signedIn) {
    if (signedIn) {
      startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
      finish();
    } else {
      //... some msg
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
        signIn();
        break;
      case R.id.btn_sign_in:
        googleSignIn();
        break;
    }
  }

  private void signUp() {
    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
  }

  private void resetPassword() {
    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
  }

  private void signIn() {
    String email = inputEmail.getText().toString();
    final String password = inputPassword.getText().toString();

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(password)) {
      Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
      return;
    }

    showProgressDialog();
    //authenticate user
    getFirebaseAuth().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            // If sign in fails, display a message to the user. If sign in succeeds
            // the mAuth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            hideProgressDialog();
            if (!task.isSuccessful()) {
              // there was an error
              if (password.length() < 6) {
                inputPassword.setError(getString(R.string.minimum_password));
              } else {
                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
              }
            } else {
              updateUI(true);
            }
          }
        });
  }

}

