package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

abstract public class CollectionUtils extends org.springframework.util.CollectionUtils {

  final public static class CollectionDelta<T> {

    public final List<T> deletionList = new LinkedList<>();
    public final List<T> neonatalList = new LinkedList<>();

    public boolean isEmpty() {
      return deletionList.isEmpty() && neonatalList.isEmpty();
    }
  }

  @NotNull
  public static <E> CollectionDelta<E> calculateCollectionDelta(@NotNull Collection<E> base, @NotNull Collection<E> incoming) {
    CollectionDelta<E> delta = new CollectionDelta<>();
    for (E e : base) {
      if (!incoming.contains(e)) {
        delta.deletionList.add(e);
      }
    }
    for (E e : incoming) {
      if (!base.contains(e)) {
        delta.neonatalList.add(e);
      }
    }
    return delta;
  }
}
