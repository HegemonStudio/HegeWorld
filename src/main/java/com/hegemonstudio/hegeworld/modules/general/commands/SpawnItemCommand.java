package com.hegemonstudio.hegeworld.modules.general.commands;

import com.impact.lib.api.command.MPlayerCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnItemCommand extends MPlayerCommand {

  public SpawnItemCommand() {
    super("spawnitem");
  }

  public void perform(@NotNull Player sender, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (argc == 0) {
      error(-1, "za mało argumentów!");
    }
    Material material = Material.valueOf(args[0].toUpperCase());
    int count = Integer.parseInt(args[1]);
    sender.getWorld().dropItemNaturally(sender.getLocation(), new ItemStack(material, count)).setCanPlayerPickup(false);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      List<String> materials = new ArrayList<>();
      for (Material material : Material.values()) {
        materials.add(material.toString().toLowerCase());
      }
      return materials;
    }
    if (args.length == 2) {
      return List.of("count");
    }
    return null;
  }

}
