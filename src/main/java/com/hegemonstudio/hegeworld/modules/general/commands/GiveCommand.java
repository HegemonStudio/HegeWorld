package com.hegemonstudio.hegeworld.modules.general.commands;

import com.hegemonstudio.hegeworld.HegeWorld;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.impact.lib.api.command.UniversalCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GiveCommand extends UniversalCommand {
  public GiveCommand() {
    super("give");
  }

  @Override
  public void onPlayerExecute(@NotNull Player player, int i, @NotNull String @NotNull [] args) {
    if (args.length == 0) {
      error(-1, "Required player selector");
      return;
    }
    if (args.length == 1) {
      error(0, "Required item");
      return;
    }
    String selector = args[0];
    List<Player> players = HegeWorld.hwPlayersBySelector(selector, player);
    String itemName = args[1];
    int count = 1;
    if (args.length > 2) {
      try {
        count = Integer.parseInt(args[2]);
      } catch (Exception ignored) {
      }
    }
    ItemStack item = HegeWorld.hwGetItem(itemName, count);
    if (item == null) {
      player.sendMessage(Component.text("Unknown item.").color(NamedTextColor.RED));
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
      return;
    }

    if (players.isEmpty()) {
      player.sendMessage(Component.text("No one received item.").color(NamedTextColor.YELLOW));
      return;
    }
    for (Player toGive : players) {
      HWPlayer.of(toGive).giveItem(item);
    }
    if (players.size() == 1) {
      player.sendMessage(Component.text("Given ").color(NamedTextColor.GREEN).append(Component.translatable(item.translationKey())).append(Component.text(" to " + players.get(0).getName())));
      return;
    }
    player.sendMessage(Component.text("Given ").color(NamedTextColor.GREEN).append(Component.translatable(item.translationKey())).append(Component.text(" to " + players.size() + " players")));
  }

  @Override
  public void onConsoleExecute(@NotNull ConsoleCommandSender consoleCommandSender, int i, @NotNull String[] args) {

  }

  @Override
  public @Nullable List<String> onPlayerTabComplete(@NotNull Player player, int i, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return HegeWorld.hwPlayerSelectors(player);
    }
    if (args.length == 2) {
      return HegeWorld.hwItemSelectors();
    }
    if (args.length == 3) {
      return List.of("[<count>]");
    }
    if (args.length > 3) {
      return List.of("Incorrect argument");
    }
    return null;
  }

  @Override
  public @Nullable List<String> onConsoleTabComplete(@NotNull ConsoleCommandSender consoleCommandSender, int i, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return HegeWorld.hwPlayerSelectors(null);
    }
    if (args.length == 2) {
      return HegeWorld.hwItemSelectors();
    }
    if (args.length == 3) {
      return List.of("[<count>]");
    }
    if (args.length > 3) {
      return List.of("Incorrect argument");
    }
    return null;
  }
}
