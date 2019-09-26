package org.goblinframework.dao.core.cql;

import org.goblinframework.api.core.Pageable;
import org.goblinframework.api.core.Sort;
import org.goblinframework.core.util.StringUtils;

import java.util.Map;
import java.util.Objects;

public class Query {

  private final Criteria criteria;
  private final Projection projection = new Projection();
  private Integer skip;
  private Integer limit;
  private Sort sort;
  private boolean reverse;      // for support multiple database/collection query

  private NativeSQL nativeSQL;  // for supporting JDBC only

  public static Query query(Criteria criteria) {
    return new Query(criteria);
  }

  public Criteria getCriteria() {
    return criteria;
  }

  public Integer getSkip() {
    return skip;
  }

  public Integer getLimit() {
    return limit;
  }

  public Sort getSort() {
    return sort;
  }

  public boolean isReverse() {
    return reverse;
  }

  public NativeSQL getNativeSQL() {
    return nativeSQL;
  }

  public static Query nativeSQL(String sql) {
    return nativeSQL(sql, null);
  }

  public static Query nativeSQL(String sql, Map<String, Object> parameters) {
    Objects.requireNonNull(sql);
    if (!StringUtils.startsWith(sql, " ")) {
      sql = " " + sql;
    }
    Query query = new Query(new Criteria());
    query.nativeSQL = new NativeSQL(sql, parameters);
    return query;
  }

  public Query(Criteria criteria) {
    this.criteria = Objects.requireNonNull(criteria);
  }

  public Projection field() {
    return projection;
  }

  public Query skip(int skip) {
    if (skip < 0) {
      throw new IllegalArgumentException("Skip must not be negative");
    }
    this.skip = skip;
    return this;
  }

  public Query limit(int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException("Limit must not be negative");
    }
    this.limit = limit;
    return this;
  }

  public Query with(Pageable pageable) {
    if (pageable == null) {
      return this;
    }
    limit(pageable.getPageSize());
    skip(pageable.getOffset());
    return with(pageable.getSort());
  }

  public Query with(Sort sort) {
    if (sort == null) {
      return this;
    }
    if (this.sort == null) {
      this.sort = sort;
    } else {
      this.sort = this.sort.and(sort);
    }
    return this;
  }

  public Query resetSort() {
    this.sort = null;
    return this;
  }

  public Query reverse(boolean value) {
    this.reverse = value;
    return this;
  }
}
