package org.goblinframework.api.core;

import java.util.List;

public interface Page<T> extends Iterable<T> {

  int getNumber();

  int getSize();

  int getNumberOfElements();

  List<T> getContent();

  boolean hasContent();

  Sort getSort();

  boolean isFirst();

  boolean isLast();

  boolean hasNext();

  boolean hasPrevious();

  Pageable nextPageable();

  Pageable previousPageable();

  int getTotalPages();

  long getTotalElements();

}
