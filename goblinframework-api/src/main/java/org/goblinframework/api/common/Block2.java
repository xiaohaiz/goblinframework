package org.goblinframework.api.common;

/**
 * @see java.util.function.BiConsumer
 */
public interface Block2<T1, T2> {

  void apply(T1 t1, T2 t2);

}
