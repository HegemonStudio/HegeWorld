package com.hegemonstudio.hegeworld.commands;

import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.impact.lib.api.command.MPlayerCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreateCommand extends MPlayerCommand {
  public CreateCommand() {
    super("create");
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (args.length == 0) {
      error(-1, "Not enough arguments");
      return;
    }
    if (args[0].equalsIgnoreCase("ground")) {
      if (args.length == 1) {
        error(0, "Required material name");
        return;
      }
      GroundCollection.SpawnGroundItemLegacy(player.getLocation(), new ItemStack(Material.valueOf(args[1].toUpperCase())));
      return;
    }
    error(0, "Invalid argument");
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      return List.of("ground");
    }
    return null;
  }
}
