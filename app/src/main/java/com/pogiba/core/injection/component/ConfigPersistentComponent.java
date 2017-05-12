package com.pogiba.core.injection.component;

import dagger.Component;

import com.pogiba.core.injection.module.FirebaseModule;
import com.pogiba.core.injection.scope.ConfigPersistent;
import com.pogiba.core.injection.module.ActivityModule;
import com.pogiba.core.injection.module.FirebaseSignInModule;
import com.pogiba.core.ui.base.BaseActivity;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

  ActivityComponent activityComponent(ActivityModule activityModule, FirebaseModule firebaseModule);

  FirebaseSignInComponent googleSignInComponent(FirebaseSignInModule presenterModule);

}