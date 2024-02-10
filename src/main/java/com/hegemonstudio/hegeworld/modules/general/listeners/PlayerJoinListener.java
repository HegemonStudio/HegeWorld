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

import static com.hegemonstudio.hegeworld.HegeWorld.hwCallEvent;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();

    HWPlayerJoinEvent joinEvent = hwCallEvent(new HWPlayerJoinEvent(player));
    event.joinMessage(joinEvent.getJoinMessage().orElse(null));

    hwCallEvent(new HWPlayerSpawnEvent(player));
  }

}
