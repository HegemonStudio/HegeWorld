package com.hegemonstudio.hegeworld.api;

import java.util.Objects;

public class Dimensions<T> {

  private T x;
  private T y;
  private T z;

  public Dimensions(T x, T y, T z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public T getX() {
    return x;
  }

  public void setX(T x) {
    this.x = x;
  }

  public T getY() {
    return y;
  }

  public void setY(T y) {
    this.y = y;
  }

  public T getZ() {
    return z;
  }

  public void setZ(T z) {
    this.z = z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    Dimensions<?> that = (Dimensions<?>) object;
    return x == that.x && y == that.y && z == that.z;
  }
}
