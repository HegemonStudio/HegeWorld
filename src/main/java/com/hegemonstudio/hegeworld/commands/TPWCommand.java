package com.hegemonstudio.hegeworld.commands;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    World world = Bukkit.getWorld(worldName);
    boolean isWorldExists = world != null;
    if (!isWorldExists) {
      // create world
      if (argc == 1) {
        error(0, "World does not exist. Please add confirm");
        return;
      }
      if (!args[1].equalsIgnoreCase("confirm")) return;
      HWLogger.Log(Component.text(sender.getName() + "creating world " + worldName + "..."));
      WorldCreator
          .name(worldName)
          .environment(World.Environment.NORMAL)
          .type(WorldType.NORMAL)
          .createWorld();
      HWLogger.Log(Component.text(sender.getName() + " created new world " + worldName));
    }
    world = Bukkit.getWorld(worldName);
    assert world != null;
    sender.teleport(world.getSpawnLocation());
    sender.sendMessage(Component.text("Teleported to " + worldName).color(NamedTextColor.GREEN));
    responseSound(Sound.ENTITY_ENDERMAN_TELEPORT);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      List<String> worldNames = new ArrayList<>();
      for (World world : Bukkit.getWorlds()) {
        worldNames.add(world.getName());
      }
      return worldNames;
    }
    return null;
  }
}
