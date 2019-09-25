package org.goblinframework.api.remote;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinServiceRegistries {

  GoblinServiceRegistry[] value();

}
