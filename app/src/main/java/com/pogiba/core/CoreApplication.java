package com.pogiba.core;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

import com.pogiba.core.injection.component.ApplicationComponent;
import com.pogiba.core.injection.component.DaggerApplicationComponent;
import com.pogiba.core.injection.module.ApplicationModule;

public class CoreApplication extends Application {

  ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();

//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//            Fabric.with(this, new Crashlytics());
//        }
  }

  public static CoreApplication get(Context context) {
    return (CoreApplication) context.getApplicationContext();
  }

  public ApplicationComponent getComponent() {
    if (mApplicationComponent == null) {
      mApplicationComponent = DaggerApplicationComponent.builder()
                                .applicationModule(new ApplicationModule(this))
                                .build();
    }
    return mApplicationComponent;
  }

  // Needed to replace the component with a test specific one
  public void setComponent(ApplicationComponent applicationComponent) {
    mApplicationComponent = applicationComponent;
  }
}
