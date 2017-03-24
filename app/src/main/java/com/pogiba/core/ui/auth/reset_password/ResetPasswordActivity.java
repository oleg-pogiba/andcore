package com.pogiba.core.ui.auth.reset_password;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.R;
import com.pogiba.core.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseActivity {

  @BindView(R.id.email)
  EditText inputEmail;

  @OnClick(R.id.btn_reset_password)
  public void resetPassword(View view) {
    String email = inputEmail.getText().toString().trim();

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
      return;
    }
    showProgressDialog();
    auth.sendPasswordResetEmail(email)
      .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          if (task.isSuccessful()) {
            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
          }
          hideProgressDialog();
        }
      });
  }

  @OnClick(R.id.btn_back)
  public void back(View view) {
    finish();
  }

  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);
    auth = FirebaseAuth.getInstance();
    ButterKnife.bind(this);
  }
}
