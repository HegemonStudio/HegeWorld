package com.hegemonstudio.hegeworld.api.commands;

import com.hegemonstudio.hegeworld.api.HegeWorldAPIPlugin;
import com.hegemonstudio.hegeworld.api.effect.HWEffect;
import com.impact.lib.api.command.MPlayerCommand;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ChunkInfoCommand extends MPlayerCommand {
  public ChunkInfoCommand() {
    super("hwchunk");
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int i, @NotNull String @NotNull [] strings) {
    Particle particle = Particle.valueOf(args[0]);
    int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(HegeWorldAPIPlugin.getInstance(), () -> {
      HWEffect.HighlightChunk(player.getChunk(), particle, player.getY());
    }, 0, 10L);
    Bukkit.getScheduler().runTaskLater(HegeWorldAPIPlugin.getInstance(), () -> {
      Bukkit.getScheduler().cancelTask(id);
    }, 20L * 5L);
  }

}
