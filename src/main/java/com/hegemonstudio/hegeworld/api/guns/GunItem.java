package com.hegemonstudio.hegeworld.api.guns;

import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.item.CustomItemSettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunItem extends CustomItem {
  public GunItem(@NotNull CustomItemSettings settings) {
    super(settings);
  }

  @Override
  public Result onUse(@NotNull Player player, @NotNull ItemStack item, boolean isLeftClick) {
    GunMechanics.Shoot(player);
    return Result.DENY;
  }
}
