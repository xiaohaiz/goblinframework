package org.goblinframework.api.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PageImpl<T> implements Page<T>, Serializable {
  private static final long serialVersionUID = -2064084486481042484L;

  private final List<T> content = new ArrayList<T>();
  private final long total;
  private final Pageable pageable;

  public PageImpl(@NotNull List<T> content, Pageable pageable, long total) {
    this.content.addAll(content);
    if (total < content.size()) {
      throw new IllegalArgumentException("Total must not be less than the number of elements given");
    }
    this.total = total;
    this.pageable = pageable;
  }

  public PageImpl(@NotNull List<T> content) {
    this(content, null, content.size());
  }

  @Override
  public int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
  }

  @Override
  public long getTotalElements() {
    return total;
  }

  @Override
  public boolean hasNext() {
    return getNumber() + 1 < getTotalPages();
  }

  @Override
  public boolean isLast() {
    return !hasNext();
  }

  @Override
  public int getNumber() {
    return pageable == null ? 0 : pageable.getPageNumber();
  }

  @Override
  public int getSize() {
    return pageable == null ? 0 : pageable.getPageSize();
  }

  @Override
  public int getNumberOfElements() {
    return content.size();
  }

  @Override
  public boolean hasPrevious() {
    return getNumber() > 0;
  }

  @Override
  public boolean isFirst() {
    return !hasPrevious();
  }

  @Override
  public Pageable nextPageable() {
    return hasNext() ? pageable.next() : null;
  }

  @Override
  public Pageable previousPageable() {
    if (hasPrevious()) {
      return pageable.previousOrFirst();
    }
    return null;
  }

  @Override
  public boolean hasContent() {
    return !content.isEmpty();
  }

  @Override
  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  @Override
  public Sort getSort() {
    return pageable == null ? null : pageable.getSort();
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }
}
