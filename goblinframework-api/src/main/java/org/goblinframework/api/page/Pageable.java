package org.goblinframework.api.page;

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
