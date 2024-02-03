package com.hegemonstudio.hegeworld.modules.guns;

import com.impact.lib.api.item.CustomItemSettings;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class AK47Gun extends GunItem{

  public static NamespacedKey KEY;

  public AK47Gun() {
    super(CustomItemSettings.of(Material.CARROT_ON_A_STICK));
  }
}
