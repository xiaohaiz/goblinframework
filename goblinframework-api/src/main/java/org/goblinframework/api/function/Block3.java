package org.goblinframework.api.function;

@FunctionalInterface
public interface Block3<T1, T2, T3> {

  void apply(T1 t1, T2 t2, T3 t3);

}
