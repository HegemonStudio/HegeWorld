package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.module.HWModule;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import static com.hegemonstudio.hegeworld.HegeWorld.hwOnTick;
import static com.hegemonstudio.hegeworld.HegeWorld.hwRegisterListener;

/**
 * HegeWorld module which adds ground item to pickup and every {@link Item} entity changes to ground item
 */
public class GroundCollectionModule extends HWModule {
  @Override
  public void onEnable() {
    hwRegisterListener(new GroundCollectionListener());
    hwOnTick(() -> {
      for (Item item : HegeWorldPlugin.GetMainWorld().getEntitiesByClass(Item.class)) {
        if (!item.isOnGround()) continue;
        ItemStack itemStack = item.getItemStack();
        if (itemStack.getType() == Material.ITEM_FRAME) continue;
        GroundCollection.SpawnGroundItem(item.getLocation(), itemStack);
        item.remove();
      }
    });

  }
}
