package com.hegemonstudio.hegeworld.modules.guns;

import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.modules.guns.items.AK47Gun;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import org.bukkit.NamespacedKey;

public final class GunModule extends HWModule {

  @Override
  public void onEnable() {
    NamespacedKey key = HegeWorldPlugin.CreateKey("ak47");
    AK47Gun.KEY = key;
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, key, new AK47Gun());
  }

}
