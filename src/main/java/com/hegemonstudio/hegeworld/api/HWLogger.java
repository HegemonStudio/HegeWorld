package com.hegemonstudio.hegeworld.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HWLogger {

  public static void Log(Component component) {
    TextComponent.Builder builder = Component.text();
    builder.append(Component.text("AC").color(NamedTextColor.RED));
    builder.appendSpace();
    builder.append(Component.text("»").color(NamedTextColor.DARK_GRAY));
    builder.appendSpace();
    builder.resetStyle();
    builder.color(NamedTextColor.GRAY);
    builder.append(component);
    Component message = builder.build();
    Bukkit.getConsoleSender().sendMessage(message);
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.isOp()) continue;
      player.sendMessage(message);
    }
  }

  public static void Warn(Component component) {
    TextComponent.Builder builder = Component.text();
    builder.append(Component.text("WARN").color(NamedTextColor.GOLD));
    builder.appendSpace();
    builder.append(Component.text("»").color(NamedTextColor.DARK_GRAY));
    builder.appendSpace();
    builder.resetStyle();
    builder.color(NamedTextColor.YELLOW);
    builder.append(component);
    Component message = builder.build();

    Bukkit.getConsoleSender().sendMessage(message);
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.isOp()) continue;
      player.sendMessage(message);
    }
  }

  public static void Err(Component component) {
    TextComponent.Builder builder = Component.text();
    builder.append(Component.text("ERR").color(NamedTextColor.DARK_RED));
    builder.appendSpace();
    builder.append(Component.text("»").color(NamedTextColor.DARK_GRAY));
    builder.appendSpace();
    builder.resetStyle();
    builder.color(NamedTextColor.RED);
    builder.append(component);
    Component message = builder.build();

    Bukkit.getConsoleSender().sendMessage(message);
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.isOp()) continue;
      player.sendMessage(message);
    }
  }

}
