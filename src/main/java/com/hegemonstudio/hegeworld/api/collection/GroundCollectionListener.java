package com.hegemonstudio.hegeworld.api.collection;

import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.api.HegeWorldAPIPlugin;
import com.hegemonstudio.hegeworld.api.events.HWPlayerCollectItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class GroundCollectionListener implements Listener {

  @EventHandler
  public void onItemFrameClick(@NotNull PlayerInteractEntityEvent event) {
    Entity clicked = event.getRightClicked();
    if (clicked instanceof ItemFrame) {
      ItemFrame frame = (ItemFrame) clicked;

      if (!GroundCollection.IsGroundItem(frame)) return;
      event.setCancelled(true);

      HWPlayer player = HWPlayer.of(event.getPlayer());
      ItemStack groundItem = GroundCollection.GetItemStackFromGround(frame);

      HWPlayerCollectItemEvent collectEvent = new HWPlayerCollectItemEvent(player, frame.getLocation(), groundItem, HWPlayerCollectItemEvent.CollectionType.HAND_FROM_GROUND);

      PluginManager pluginManager = Bukkit.getPluginManager();
      pluginManager.callEvent(collectEvent);

      if (collectEvent.isCancelled()) return;

      ItemStack collectedItem = collectEvent.getItemStack();
      player.giveItem(collectedItem);

      if (collectEvent.isPlayDefaultPickupSound()) {
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
        frame.getWorld().playSound(collectEvent.getSource(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 0.8f);
      }

      GroundCollection.RemoveGroundItem(frame);
    }
  }

}
