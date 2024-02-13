package com.hegemonstudio.hegeworld.modules.general.commands;

import com.impact.lib.api.command.MPlayerCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.hegemonstudio.hegeworld.HegeWorld.*;

public class SpawnItemCommand extends MPlayerCommand {

  public SpawnItemCommand() {
    super("spawnitem");
  }

  public void perform(@NotNull Player sender, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (argc == 0) {
      error(-1, "za mało argumentów!");
    }
    int count = Integer.parseInt(args[1]);
    ItemStack item = hwItem(args[0], count);
    assert item != null;
    hwDropItem(sender.getLocation(), item)
        .setCanPlayerPickup(false);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1) return hwGetItemSelectors();
    if (args.length == 2)
      return List.of("<count>");
    return null;
  }

}
