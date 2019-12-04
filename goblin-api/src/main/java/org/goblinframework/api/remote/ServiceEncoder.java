package org.goblinframework.api.remote;

import org.goblinframework.api.core.SerializerMode;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceEncoder {

  SerializerMode serializer() default SerializerMode.HESSIAN2;

  boolean enable() default true;
}
