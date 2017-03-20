package com.pogiba.core.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.CoreApplication;
import com.pogiba.core.R;
import com.pogiba.core.injection.component.ActivityComponent;
import com.pogiba.core.injection.component.ConfigPersistentComponent;
import com.pogiba.core.injection.component.DaggerConfigPersistentComponent;
import com.pogiba.core.injection.module.ActivityModule;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {

  private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
  private static final AtomicLong NEXT_ID = new AtomicLong(0);
  private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

  private ActivityComponent mActivityComponent;
  private long mActivityId;

  // firebase auth
  private ProgressDialog progressDialog;
  //todo: di
  private FirebaseAuth mAuth;

  public FirebaseAuth getFirebaseAuth() {
    return mAuth;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
    // being called after a configuration change.
    mActivityId = savedInstanceState != null ?
                    savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
    ConfigPersistentComponent configPersistentComponent;
    if (!sComponentsMap.containsKey(mActivityId)) {
      Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
      configPersistentComponent = DaggerConfigPersistentComponent.builder()
                                    .applicationComponent(CoreApplication.get(this).getComponent())
                                    .build();
      sComponentsMap.put(mActivityId, configPersistentComponent);
    } else {
      Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
      configPersistentComponent = sComponentsMap.get(mActivityId);
    }
    mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    // firebase auth
    //todo: di
    mAuth = FirebaseAuth.getInstance();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putLong(KEY_ACTIVITY_ID, mActivityId);
  }

  @Override
  protected void onDestroy() {
    if (!isChangingConfigurations()) {
      Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
      sComponentsMap.remove(mActivityId);
    }
    super.onDestroy();
  }

  public ActivityComponent activityComponent() {
    return mActivityComponent;
  }

  public void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setMessage(getString(R.string.loading));
      progressDialog.setIndeterminate(true);
    }
    progressDialog.show();
  }

  public void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  public void showMessage(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

}
