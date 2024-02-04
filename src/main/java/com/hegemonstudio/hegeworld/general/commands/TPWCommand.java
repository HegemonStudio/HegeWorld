package com.hegemonstudio.hegeworld.general.commands;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HegeWorld command to teleport and create worlds.<br>
 * Usage /tpw
 */
public class TPWCommand extends MPlayerCommand {
  public TPWCommand() {
    super("tpw");
  }

  @Override
  public void perform(@NotNull Player sender, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (argc == 0) {
      error(-1, "Required world name");
      responseSound(Sound.ENTITY_VILLAGER_NO);
      return;
    }
    String worldName = args[0];
    boolean isWorldExists =  Bukkit.getWorld(worldName) != null;

    if (!isWorldExists) {
      // required confirm
      if (argc == 1) {
        error(0, "World does not exist. Please add confirm");
        responseSound(Sound.ENTITY_VILLAGER_NO);
        return;
      }
      if (!args[1].equalsIgnoreCase("confirm")) return;

      // create world
      HWLogger.Log(Component.text(sender.getName() + " creating world " + worldName + "..."));
      WorldCreator
          .name(worldName)
          .environment(World.Environment.NORMAL)
          .type(WorldType.NORMAL)
          .createWorld();
      HWLogger.Log(Component.text(sender.getName() + " created new world " + worldName));
    }

    World world = Bukkit.getWorld(worldName);
    assert world != null;

    sender.teleport(world.getSpawnLocation());
    sender.sendMessage(Component.text("Teleported to " + worldName).color(NamedTextColor.GREEN));
    responseSound(Sound.ENTITY_ENDERMAN_TELEPORT);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    // First argument
    if (args.length == 1) {
      return Bukkit.getWorlds()
          .stream()
          .map(WorldInfo::getName)
          .collect(Collectors.toCollection(ArrayList::new));
    }
    return null;
  }
}
