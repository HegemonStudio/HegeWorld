package com.hegemonstudio.hegeworld.modules.general.commands;

import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.hegemonstudio.hegeworld.HegeWorld.*;

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

    if (hwIsWorldExists(worldName)) {
      // required confirm
      if (argc == 1) {
        error(0, "World does not exist. Please add confirm");
        responseSound(Sound.ENTITY_VILLAGER_NO);
        return;
      }
      if (!args[1].equalsIgnoreCase("confirm")) return;
      String playerName = sender.getName();

      hwLog(playerName + " creating world " + worldName + "...");
      hwCreateWorld(worldName);
      hwLog(playerName + " created new world " + worldName);
    }

    World world = hwGetWorld(worldName);
    assert world != null;

    sender.teleport(world.getSpawnLocation());
    sender.sendMessage(Component.text("Teleported to " + worldName).color(NamedTextColor.GREEN));
    responseSound(Sound.ENTITY_ENDERMAN_TELEPORT);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return hwGetWorldNames();
    }
    return null;
  }
}
