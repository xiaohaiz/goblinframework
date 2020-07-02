package org.goblinframework.api.function;

/**
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface Block2<T1, T2> {

  void apply(T1 t1, T2 t2);

}
