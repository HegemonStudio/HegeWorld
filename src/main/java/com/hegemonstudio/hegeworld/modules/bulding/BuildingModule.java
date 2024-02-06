package com.hegemonstudio.hegeworld.modules.bulding;

import com.hegemonstudio.hegeworld.HWModule;
import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import org.bukkit.NamespacedKey;

public class BuildingModule extends HWModule {

  public void onEnable() {
    // Register FoundationPlacerItem
    NamespacedKey foundation = new NamespacedKey(HegeWorldPlugin.GetInstance(), "foundation");
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, foundation, new FoundationPlacerItem());
  }

}
