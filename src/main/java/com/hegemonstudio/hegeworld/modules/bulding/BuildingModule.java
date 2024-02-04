package com.hegemonstudio.hegeworld.modules.bulding;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.module.HWModule;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import org.bukkit.NamespacedKey;

public class BuildingModule extends HWModule {

  public void start() {
    // Register FoundationPlacerItem
    NamespacedKey foundation = new NamespacedKey(HegeWorldPlugin.getInstance(), "foundation");
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, foundation, new FoundationPlacerItem());
  }

}
