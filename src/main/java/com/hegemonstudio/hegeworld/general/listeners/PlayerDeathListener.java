package com.hegemonstudio.hegeworld.general.listeners;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.events.HWPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerDeathEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTask(HegeWorldPlugin.GetInstance(), () -> {
      Bukkit.getPluginManager().callEvent(new HWPlayerDeathEvent(player));
    });
    event.setCancelled(true);
  }

}
