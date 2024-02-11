package com.hegemonstudio.hegeworld.modules.general.listeners;

import com.hegemonstudio.hegeworld.api.events.HWPlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import static com.hegemonstudio.hegeworld.HegeWorld.hwCallEvent;
import static com.hegemonstudio.hegeworld.HegeWorld.hwOnTickLater;

public class PlayerDeathListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerDeathEvent event) {
    Player player = event.getPlayer();
    hwOnTickLater(() -> hwCallEvent(new HWPlayerDeathEvent(player)));
    event.setCancelled(true);
  }

}
