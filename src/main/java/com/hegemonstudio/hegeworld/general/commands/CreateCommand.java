package com.hegemonstudio.hegeworld.general.commands;

import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundItemData;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * HegeWorld Create Command to easily spawn objects.
 *
 * @see GroundItemData
 */
public class CreateCommand extends MPlayerCommand {

  public CreateCommand() {
    super("create");
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (args.length == 0) {
      error(-1, "Not enough arguments");
      responseSound(Sound.ENTITY_VILLAGER_NO);
      return;
    }
    if (args[0].equalsIgnoreCase("ground")) {
      if (args.length == 1) {
        error(0, "Required material name");
        responseSound(Sound.ENTITY_VILLAGER_NO);
        return;
      }

      String materialName = args[1].toUpperCase();
      Material material = Material.getMaterial(materialName);
      if (material == null) {
        error(1, "Invalid material name");
        responseSound(Sound.ENTITY_VILLAGER_NO);
        return;
      }
      ItemStack item = new ItemStack(material);

      GroundCollection.SpawnGroundItem(player.getLocation(), item);

      response(Component.text("Successfully spawned ground item.").color(NamedTextColor.GREEN));
      responseSound(Sound.ENTITY_VILLAGER_YES);
      return;
    }
    error(0, "Invalid argument");
    responseSound(Sound.ENTITY_VILLAGER_NO);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return List.of("ground");
    }
    return null;
  }
}
