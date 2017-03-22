package com.pogiba.core.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.R;
import com.pogiba.core.ui.auth.ProfileActivity;
import com.pogiba.core.ui.auth.ResetPasswordActivity;
import com.pogiba.core.ui.base.BaseActivity;

public class SignupActivity extends BaseActivity {

  private final String TAG = "SignupActivity";
  private EditText inputEmail, inputPassword;
  private Button btnSignIn, btnSignUp, btnResetPassword;
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    //Get Firebase auth instance
    auth = FirebaseAuth.getInstance();

    btnSignIn = (Button) findViewById(R.id.sign_in_button);
    btnSignUp = (Button) findViewById(R.id.sign_up_button);
    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);
    btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

    btnResetPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
      }
    });

    btnSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
          Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
          return;
        }

        if (TextUtils.isEmpty(password)) {
          Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
          return;
        }

        if (password.length() < 6) {
          Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
          return;
        }

        showProgressDialog();
        //create user
        auth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              hideProgressDialog();
              Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
              // If sign in fails, display a message to the user. If sign in succeeds
              // the auth state listener will be notified and logic to handle the
              // signed in user can be handled in the listener.
              if (!task.isSuccessful()) {
                Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                  Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Authentication failed." + task.getException());
              } else {
                startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
                finish();
              }
            }
          });

      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideProgressDialog();
  }
}