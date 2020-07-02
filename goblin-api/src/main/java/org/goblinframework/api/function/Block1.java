package org.goblinframework.api.function;

/**
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface Block1<T> {

  void apply(T t);

}
