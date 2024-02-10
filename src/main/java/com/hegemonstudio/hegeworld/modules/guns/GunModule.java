package com.hegemonstudio.hegeworld.modules.guns;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.modules.guns.items.AK47Gun;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import org.bukkit.NamespacedKey;

import static com.hegemonstudio.hegeworld.HegeWorld.hwRegisterItem;

public final class GunModule extends HWModule {

  @Override
  public void onEnable() {
    hwRegisterItem("ak47", new AK47Gun());
  }

}
