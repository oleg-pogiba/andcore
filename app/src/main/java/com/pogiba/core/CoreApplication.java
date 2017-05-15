package com.pogiba.core;

import android.app.Application;
import android.content.Context;

//import io.fabric.sdk.android.Fabric;

import com.pogiba.core.injection.component.ApplicationComponent;
import com.pogiba.core.injection.component.DaggerApplicationComponent;
import com.pogiba.core.injection.module.ApplicationModule;

public class CoreApplication extends Application {

  ApplicationComponent applicationComponent;

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
    if (applicationComponent == null) {
      applicationComponent = DaggerApplicationComponent.builder()
                                .applicationModule(new ApplicationModule(this))
                                .build();
    }
    return applicationComponent;
  }

  // Needed to replace the component with a test specific one
  public void setComponent(ApplicationComponent applicationComponent) {
    this.applicationComponent = applicationComponent;
  }
}
