package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.module.HWModule;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import static com.hegemonstudio.hegeworld.HegeWorld.*;

/**
 * HegeWorld module which adds ground item to pickup and every {@link Item} entity changes to ground item
 */
public class GroundCollectionModule extends HWModule {
  @Override
  public void onEnable() {
    hwRegisterListener(new GroundCollectionListener());
    hwOnTick(() -> {
      for (Item item : hwGetEntities(Item.class)) {
        if (!item.isOnGround()) continue;
        ItemStack itemStack = item.getItemStack();
        hwSpawnGroundItem(item.getLocation(), itemStack);
        item.remove();
      }
    });

  }
}
