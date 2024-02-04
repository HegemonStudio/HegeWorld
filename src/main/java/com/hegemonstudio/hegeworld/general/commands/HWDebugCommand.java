package com.hegemonstudio.hegeworld.general.commands;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.hegemonstudio.hegeworld.modules.guns.items.AK47Gun;
import com.impact.lib.api.command.MPlayerCommand;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HWDebugCommand extends MPlayerCommand {

  public static final String LABEL = "hwdebug";
  private static final int DEBUG_MAX_DISTANCE = 5;

  public HWDebugCommand() {
    super(LABEL);
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (argc == 0) {
      error(-1, "Not enough arguments");
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1f,1f);
      return;
    }
    if (args[0].equalsIgnoreCase("shoot")) {
      HWPlayer.of(player).giveItem(ImpactRegistries.CUSTOM_ITEM.get(AK47Gun.KEY).getItemStack(1));
      return;
    }
    if (args[0].equalsIgnoreCase("giveitem")) {
      HWPlayer hwplayer = HWPlayer.of(player);
      if (argc == 1) {
        error(0, "Required item name");
        return;
      }

      int count = 1;
      if (argc >= 3) {
        try {
          count = Integer.parseInt(args[2]);
        } catch (Exception exception) {
          error(2, "Invalid number");
          return;
        }
        if (count <= 0) throw new AssertionError();
      }

      try {
        Material material = Material.valueOf(args[1].toUpperCase());
        hwplayer.giveItem(new ItemStack(material, count));
        return;
      } catch (IllegalArgumentException exception) {}

      CustomItem customItem = ImpactRegistries.CUSTOM_ITEM.get(new NamespacedKey(HegeWorldPlugin.GetInstance(), args[1].toLowerCase()));
      if (customItem != null) {
        hwplayer.giveItem(customItem.getItemStack(count));
        return;
      }
      error(1, "Unknown item");
      return;
    }
    if (args[0].equalsIgnoreCase("looking")) {
      Entity targetEntity = player.getTargetEntity(DEBUG_MAX_DISTANCE);
      if (targetEntity != null) {
        debugEntity(player, targetEntity);
        return;
      }
      Block targetBlock = player.getTargetBlockExact(DEBUG_MAX_DISTANCE);
      if (targetBlock != null) {
        debugBlock(player, targetBlock);
        return;
      }
      player.sendMessage(
          Component
              .text("You are not looking at anything")
              .color(NamedTextColor.YELLOW)
      );
      return;
    }
  }

  private void debugBlock(@NotNull Player player, @NotNull Block targetBlock) {
    player.sendMessage(Component.translatable(targetBlock.translationKey()));
    player.sendMessage(Component.text(targetBlock.getLocation().toString()));
  }

  public void debugEntity(@NotNull Player player, Entity entity) {
    boolean isGroundItem = GroundCollection.IsGroundItem(entity);
    player.sendMessage(Component.text("Is GroundItem = " + isGroundItem));
    player.sendMessage(Component.text(entity.getLocation().toString()));
    if (isGroundItem) {
      ItemFrame frame = GroundCollection.GetFrame(entity).get();
      // TODO
    }
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return List.of("looking", "grounditem", "block", "entity", "shoot", "giveitem");
    }
    if (args[0].equalsIgnoreCase("giveitem") && args.length == 2) {
      List<String> materials = new ArrayList<>();
      for (Material material : Material.values()) {
        materials.add(material.toString().toLowerCase());
      }
      for (CustomItem customItem : ImpactRegistries.CUSTOM_ITEM.getAll()) {
        materials.add(customItem.getNamespacedKey().getKey());
      }
      return materials;
    }
    if (args[0].equalsIgnoreCase("giveitem") && args.length == 3) {
      return List.of("[count]");
    }
    return null;
  }


}
