package com.pogiba.core.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import com.pogiba.core.data.DataManager;
import com.pogiba.core.data.SyncService;
import com.pogiba.core.data.local.DatabaseHelper;
import com.pogiba.core.data.local.PreferencesHelper;
import com.pogiba.core.data.remote.RibotsService;
import com.pogiba.core.injection.qualifier.ApplicationContext;
import com.pogiba.core.injection.module.ApplicationModule;
import com.pogiba.core.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  void inject(SyncService syncService);

  @ApplicationContext
  Context context();

  Application application();

  RibotsService ribotsService();

  PreferencesHelper preferencesHelper();

  DatabaseHelper databaseHelper();

  DataManager dataManager();

  RxEventBus eventBus();


}
