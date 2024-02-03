package com.hegemonstudio.hegeworld.api.commands;

import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.api.collection.GroundCollection;
import com.hegemonstudio.hegeworld.api.guns.AK47Gun;
import com.hegemonstudio.hegeworld.api.guns.GunMechanics;
import com.impact.lib.api.command.MPlayerCommand;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      ItemFrame frame = GroundCollection.GetGroundItem(entity).get();
      // TODO
    }
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return List.of("looking", "grounditem", "block", "entity", "shoot");
    }
    return null;
  }


}
