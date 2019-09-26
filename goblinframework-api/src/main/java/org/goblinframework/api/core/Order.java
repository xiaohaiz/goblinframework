package org.goblinframework.api.core;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable {
  private static final long serialVersionUID = 2863641878469626043L;

  private final Direction direction;
  private final String property;

  public Order(String property) {
    this(Sort.DEFAULT_DIRECTION, property);
  }

  public Order(Direction direction, String property) {
    if (property == null || property.length() == 0) {
      throw new IllegalArgumentException("Property must not null or empty!");
    }
    this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
    this.property = property;
  }

  public Direction getDirection() {
    return direction;
  }

  public String getProperty() {
    return property;
  }

  public boolean isAscending() {
    return this.direction.equals(Direction.ASC);
  }

  public Order with(Direction order) {
    return new Order(order, this.property);
  }

  public Sort withProperties(String... properties) {
    return new Sort(this.direction, properties);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return direction == order.direction &&
        Objects.equals(property, order.property);
  }

  @Override
  public int hashCode() {
    return Objects.hash(direction, property);
  }
}
