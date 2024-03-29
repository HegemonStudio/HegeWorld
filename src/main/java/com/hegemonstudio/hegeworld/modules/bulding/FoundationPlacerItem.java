package com.hegemonstudio.hegeworld.modules.bulding;

import com.hegemonstudio.hegeworld.api.BlockPlacement;
import com.hegemonstudio.hegeworld.api.BuildMechanics;
import com.hegemonstudio.hegeworld.api.Dimensions;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.item.CustomItemSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FoundationPlacerItem extends CustomItem {
  public FoundationPlacerItem() {
    super(CustomItemSettings.of(Material.GRAY_DYE).setDisplayName("Concrete Foundation"));
  }

  @Override
  public Result onBlockUse(@NotNull Player player, @NotNull ItemStack item, boolean isLeftClick, @NotNull Block clickedBlock, @NotNull BlockFace clickedFace) {
    if (isLeftClick) return Result.DENY;
    if (BuildMechanics.IsGenerated(clickedBlock)) return Result.DENY;
    Location platform = clickedBlock.getLocation().add(0, 1, 0);
    Dimensions<Integer> dimensions = new Dimensions<>(5, 10, 5);
    if (!BlockPlacement.CheckPlatform(platform, dimensions, (block) -> !BuildMechanics.IsGenerated(block)))
      return Result.DENY;
    BlockPlacement.PlacePlatform(platform, Material.ANDESITE, dimensions, BuildMechanics::SelectAsGenerated);
    return Result.CONSUME;
  }
}
