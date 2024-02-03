package com.hegemonstudio.hegeworld.api.listeners;

import com.hegemonstudio.hegeworld.api.HegeWorldAPIPlugin;
import com.hegemonstudio.hegeworld.api.events.HWPlayerDeathEvent;
import com.hegemonstudio.hegeworld.api.events.HWPlayerSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerDeathEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTask(HegeWorldAPIPlugin.getInstance(), () -> {
      Bukkit.getPluginManager().callEvent(new HWPlayerDeathEvent(player));
    });
    event.setCancelled(true);
  }

}
