package com.hegemonstudio.hegeworld.listeners;

import com.google.common.util.concurrent.Service;
import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ItemDropListener implements Listener {

  @EventHandler
  public void ItemDropped(){
    Bukkit.getScheduler().scheduleSyncRepeatingTask(HegeWorldPlugin.getInstance(), () -> {
        Bukkit.getWorld().getEntities();
    }, 0, 5);
  }
}
