package org.goblinframework.api.remote;

import org.goblinframework.api.registry.RegistrySystem;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinServiceRegistry {

  RegistrySystem system();

  String name();

}
