package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.module.HWModule;
import com.hegemonstudio.hegeworld.api.tasks.TaskManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/**
 * HegeWorld module which adds ground item to pickup and every {@link Item} entity changes to ground item
 */
public class GroundCollectionModule extends HWModule {
  @Override
  public void start() {
    // TODO use GroundItemData
    HegeWorldPlugin.getInstance().registerListener(new GroundCollectionListener());

    TaskManager.OnTick(() -> {
      for (Item item : HegeWorldPlugin.getMainWorld().getEntitiesByClass(Item.class)) {
        if (!item.isOnGround()) continue;
        ItemStack itemStack = item.getItemStack();
        if (itemStack.getAmount() > 1 || itemStack.getType() == Material.ITEM_FRAME) continue;
        GroundCollection.SpawnGroundItemLegacy(item.getLocation(), itemStack);
        item.remove();
      }
    });

  }
}
