package com.pogiba.core.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.pogiba.core.injection.component.ApplicationComponent;
import com.pogiba.core.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
