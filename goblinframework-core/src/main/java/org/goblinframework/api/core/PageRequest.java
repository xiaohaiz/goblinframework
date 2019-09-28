package org.goblinframework.api.core;

import java.io.Serializable;
import java.util.Objects;

public class PageRequest implements Pageable, Serializable {
  private static final long serialVersionUID = 6217268391465418675L;

  private final int page;
  private final int size;
  private final Sort sort;

  public PageRequest(int page, int size) {
    this(page, size, null);
  }

  public PageRequest(int page, int size, Direction direction, String... properties) {
    this(page, size, new Sort(direction, properties));
  }

  public PageRequest(int page, int size, Sort sort) {
    if (page < 0) throw new IllegalArgumentException("Page index must not be less than zero");
    if (size < 1) throw new IllegalArgumentException("Page size must not be less than one");
    this.page = page;
    this.size = size;
    this.sort = sort;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public int getPageNumber() {
    return page;
  }

  @Override
  public int getPageSize() {
    return size;
  }

  @Override
  public int getOffset() {
    return page * size;
  }

  @Override
  public boolean hasPrevious() {
    return page > 0;
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  public Pageable next() {
    return new PageRequest(getPageNumber() + 1, getPageSize(), getSort());
  }

  public PageRequest previous() {
    return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize(), getSort());
  }

  @Override
  public Pageable first() {
    return new PageRequest(0, getPageSize(), getSort());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PageRequest that = (PageRequest) o;
    return page == that.page &&
        size == that.size &&
        Objects.equals(sort, that.sort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, size, sort);
  }
}
