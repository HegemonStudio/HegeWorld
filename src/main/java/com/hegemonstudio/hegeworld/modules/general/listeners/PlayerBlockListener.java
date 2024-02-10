package com.hegemonstudio.hegeworld.modules.general.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import static com.hegemonstudio.hegeworld.HegeWorld.hwHasPlayerPermission;

/**
 * HegeWorld API Handling block events
 */
public class PlayerBlockListener implements Listener {

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent event) {
    Player player = event.getPlayer();
    event.setCancelled(!hwHasPlayerPermission(player, "hegeworld.block.admin"));
  }

  @EventHandler
  public void onBlockPlace(@NotNull BlockPlaceEvent event) {
    Player player = event.getPlayer();
    event.setBuild(hwHasPlayerPermission(player, "hegeworld.block.admin"));
  }

}
