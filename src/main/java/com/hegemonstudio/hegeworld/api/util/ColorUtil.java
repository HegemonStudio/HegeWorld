package com.hegemonstudio.hegeworld.api.util;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public final class ColorUtil {

  private static final Pattern COLOR_PATTERN = Pattern.compile("#[a-fA-f0-9]{6}");

  private ColorUtil() {

  }

  @Deprecated
  public static @NotNull String Color(String message) {
    Matcher matcher = COLOR_PATTERN.matcher(message);
    while (matcher.find()) {
      String color = message.substring(matcher.start(), matcher.end());
      message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
      matcher = COLOR_PATTERN.matcher(message);
    }
    return ChatColor.translateAlternateColorCodes('&', message);
  }

}