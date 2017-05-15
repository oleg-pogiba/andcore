package com.pogiba.core.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.pogiba.core.CoreApplication;
import com.pogiba.core.R;
import com.pogiba.core.injection.component.ActivityComponent;
import com.pogiba.core.injection.component.ConfigPersistentComponent;
import com.pogiba.core.injection.component.DaggerConfigPersistentComponent;
import com.pogiba.core.injection.module.ActivityModule;
import com.pogiba.core.injection.module.FirebaseModule;
import com.pogiba.core.ui.Navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity implements
  GoogleApiClient.OnConnectionFailedListener{

  private static final String TAG = "BaseActivity";
  private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
  private static final AtomicLong NEXT_ID = new AtomicLong(0);
  private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

  private ActivityComponent mActivityComponent;
  private long activityId;
  private ProgressDialog progressDialog;

  @Inject
  Navigator navigator;

  ConfigPersistentComponent configPersistentComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
    // being called after a configuration change.
    activityId = savedInstanceState != null ?
        savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

    if (!sComponentsMap.containsKey(activityId)) {
      Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
      configPersistentComponent = DaggerConfigPersistentComponent.builder()
          .applicationComponent(CoreApplication.get(this).getComponent())
          .build();
      sComponentsMap.put(activityId, configPersistentComponent);
    } else {
      Timber.i("Reusing ConfigPersistentComponent id=%d", activityId);
      configPersistentComponent = sComponentsMap.get(activityId);
    }

    mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this), new FirebaseModule(this));
  }

  public void checkAccess() {
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      getNavigator().navigateToLogin(this);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putLong(KEY_ACTIVITY_ID, activityId);
  }

  @Override
  protected void onDestroy() {
    if (!isChangingConfigurations()) {
      Timber.i("Clearing ConfigPersistentComponent id=%d", activityId);
      sComponentsMap.remove(activityId);
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

  public ConfigPersistentComponent getConfigPersistentComponent() {
    return configPersistentComponent;
  }

  public Navigator getNavigator() {
    return navigator;
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

}
