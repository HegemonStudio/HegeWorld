package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.tasks.TaskManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/**
 * HegeWorld module which adds ground item to pickup and every {@link Item} entity changes to ground item
 */
public class GroundCollectionModule extends HWModule {
  @Override
  public void onEnable() {
    registerListener(new GroundCollectionListener());

    TaskManager.OnTick(() -> {
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
