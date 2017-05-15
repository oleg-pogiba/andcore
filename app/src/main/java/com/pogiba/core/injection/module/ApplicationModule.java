package com.pogiba.core.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.pogiba.core.data.remote.RibotsService;
import com.pogiba.core.injection.qualifier.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
  protected final Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  Application provideApplication() {
    return application;
  }

  @Provides
  @ApplicationContext
  Context provideContext() {
    return application;
  }

  @Provides
  @Singleton
  RibotsService provideRibotsService() {
    return RibotsService.Creator.newRibotsService();
  }

}
