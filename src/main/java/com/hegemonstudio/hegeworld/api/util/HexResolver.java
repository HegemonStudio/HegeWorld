package com.hegemonstudio.hegeworld.api.util;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HexResolver {
  private static final Pattern GRADIENT_PATTERN = Pattern.compile("<(gradient|g)(:#([a-fA-F0-9]){6})+>");
  private static final Pattern HEX_PATTERN = Pattern.compile("<(#[a-fA-F0-9]{6})>");

  public static @NotNull String parseHexString(String text) {
    return parseHexString(text, HexResolver.HEX_PATTERN);
  }

  public static @NotNull String parseHexString(String text, @NotNull Pattern hexPattern) {
    Matcher hexColorMatcher = hexPattern.matcher(text);

    text = parseGradients(text);
    while (hexColorMatcher.find()) {
      String hex = hexColorMatcher.group(1);
      ChatColor color = ChatColor.of(hex);
      String before = text.substring(0, hexColorMatcher.start());
      String after = text.substring(hexColorMatcher.end());
      text = before + color + after;
      hexColorMatcher = hexPattern.matcher(text);
    }

    return ChatColor.translateAlternateColorCodes('&', text);
  }

  private static String parseGradients(String text) {
    String parsed = text;
    Matcher matcher = GRADIENT_PATTERN.matcher(parsed);
    while (matcher.find()) {
      StringBuilder parsedGradient = new StringBuilder();
      String match = matcher.group();
      int tagLength = match.startsWith("<gr") ? 10 : 3;
      int indexOfClose = match.indexOf(">");
      String hexContent = match.substring(tagLength, indexOfClose);
      List<Color> hexSteps = Arrays.stream(hexContent.split(":")).map(Color::decode).collect(Collectors.toList());
      int stop = findGradientStop(parsed, matcher.end());
      String content = parsed.substring(matcher.end(), stop);
      Gradient gradient = new Gradient(hexSteps, content.length());
      for (char c : content.toCharArray())
        parsedGradient.append(ChatColor.of(gradient.next()).toString()).append(c);
      String before = parsed.substring(0, matcher.start());
      String after = parsed.substring(stop);
      parsed = before + parsedGradient + after;
      matcher = GRADIENT_PATTERN.matcher(parsed);
    }
    return parsed;
  }

  private static int findGradientStop(String content, int searchAfter) {
    Matcher matcher = GRADIENT_PATTERN.matcher(content);
    while (matcher.find()) if (matcher.start() > searchAfter) return matcher.start();
    return content.length() - 1;
  }

}