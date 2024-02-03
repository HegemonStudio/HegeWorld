package com.hegemonstudio.hegeworld.api.math;

public class HWMath {

  public static double Clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  public static float Clamp(float value, float min, float max) {
    return Math.min(Math.max(value, min), max);
  }

}
