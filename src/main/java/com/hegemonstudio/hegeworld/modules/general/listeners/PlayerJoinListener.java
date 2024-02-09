package com.hegemonstudio.hegeworld.modules.general.listeners;

import com.hegemonstudio.hegeworld.api.events.HWPlayerJoinEvent;
import com.hegemonstudio.hegeworld.api.events.HWPlayerSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PluginManager pm = Bukkit.getPluginManager();

    HWPlayerJoinEvent joinEvent = new HWPlayerJoinEvent(player);
    pm.callEvent(joinEvent);

    event.joinMessage(joinEvent.getJoinMessage().orElse(null));

    HWPlayerSpawnEvent spawnEvent = new HWPlayerSpawnEvent(player);
    pm.callEvent(spawnEvent);
  }

}
