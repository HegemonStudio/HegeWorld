package com.hegemonstudio.hegeworld.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HWPlayerDeathEvent extends Event {

  private static final HandlerList HANDLERS_LIST = new HandlerList();

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  private final Player player;

  public HWPlayerDeathEvent(@NotNull Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

}
