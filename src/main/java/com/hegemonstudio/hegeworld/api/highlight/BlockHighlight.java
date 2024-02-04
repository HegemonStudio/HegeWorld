package com.hegemonstudio.hegeworld.api.highlight;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.api.events.HWPlayerTargetBlockEvent;
import com.hegemonstudio.hegeworld.api.module.HWModule;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BlockHighlight extends HWModule {

  public static Map<UUID, Block> HIGHLIGHTED_BLOCKS = new HashMap<>();

  public static Block GetHighlightedBlock(@NotNull Player player) {
    return HIGHLIGHTED_BLOCKS.get(player.getUniqueId());
  }

  @Override
  public void start() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(HegeWorldPlugin.GetInstance(), () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        UUID uuid = player.getUniqueId();
        Block target = player.getTargetBlockExact(5);
        if (target == null) continue;
        if (HIGHLIGHTED_BLOCKS.containsKey(uuid)) {
          if (HIGHLIGHTED_BLOCKS.get(uuid).equals(target)) continue;
        }
        HIGHLIGHTED_BLOCKS.put(player.getUniqueId(), target);
        HWPlayerTargetBlockEvent event = new HWPlayerTargetBlockEvent(HWPlayer.of(player), target);
        Bukkit.getPluginManager().callEvent(event);
      }
    }, 0, 5L);
  }

}
