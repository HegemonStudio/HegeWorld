package com.hegemonstudio.hegeworld.modules.general.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * HegeWorld API Handling block events
 */
public class PlayerBlockListener implements Listener {

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent event) {
    Player player = event.getPlayer();
    boolean canBreak = player.hasPermission("hegeworld.block.admin");
    event.setCancelled(!canBreak);
  }

  @EventHandler
  public void onBlockPlace(@NotNull BlockPlaceEvent event) {
    Player player = event.getPlayer();
    boolean canPlace = player.hasPermission("hegeworld.block.admin");
    event.setBuild(canPlace);
  }

}
