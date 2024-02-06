package com.hegemonstudio.hegeworld.api.events;

import com.hegemonstudio.hegeworld.api.HWPlayer;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HWPlayerTargetBlockEvent extends Event {

  private static final HandlerList HANDLERS_LIST = new HandlerList();
  private final HWPlayer player;
  private final Block block;

  public HWPlayerTargetBlockEvent(@NotNull HWPlayer player, @NotNull Block block) {
    this.player = player;
    this.block = block;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

}

