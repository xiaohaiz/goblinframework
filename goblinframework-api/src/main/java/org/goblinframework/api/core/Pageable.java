package org.goblinframework.api.core;

public interface Pageable {

  int getPageNumber();

  int getPageSize();

  int getOffset();

  Sort getSort();

  Pageable next();

  Pageable previousOrFirst();

  Pageable first();

  boolean hasPrevious();
}
