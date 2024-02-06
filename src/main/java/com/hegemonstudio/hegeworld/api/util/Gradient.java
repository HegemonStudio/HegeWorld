package com.hegemonstudio.hegeworld.api.util;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class Gradient {

  private final List<Color> colors;
  private final int stepSize;
  private int step, stepIndex;

  public Gradient(@NotNull List<Color> colors, int totalColors) {
    if (colors.size() < 2) {
      throw new IllegalArgumentException("Must provide at least 2 colors");
    }
    if (totalColors < 1) {
      throw new IllegalArgumentException("Must have at least 1 total color");
    }
    this.colors = colors;
    this.stepSize = totalColors / (colors.size() - 1);
    this.step = this.stepIndex = 0;
  }

  public static @NotNull Color getGradientInterval(Color start, Color end, float interval) {
    if (0 > interval || interval > 1)
      throw new IllegalArgumentException("Interval must be between 0 and 1 inclusively.");

    int r = (int) (end.getRed() * interval + start.getRed() * (1 - interval));
    int g = (int) (end.getGreen() * interval + start.getGreen() * (1 - interval));
    int b = (int) (end.getBlue() * interval + start.getBlue() * (1 - interval));

    return new Color(r, g, b);
  }

  public Color next() {
    Color color;
    if (this.stepIndex + 1 < this.colors.size()) {
      Color start = this.colors.get(this.stepIndex);
      Color end = this.colors.get(this.stepIndex + 1);
      float interval = (float) this.step / this.stepSize;
      color = getGradientInterval(start, end, interval);
    } else {
      color = this.colors.get(this.colors.size() - 1);
    }
    this.step += 1;
    if (this.step >= this.stepSize) {
      this.step = 0;
      this.stepIndex++;
    }
    return color;
  }
}