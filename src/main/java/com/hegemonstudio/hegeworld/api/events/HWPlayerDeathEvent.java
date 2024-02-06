package com.hegemonstudio.hegeworld.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HWPlayerDeathEvent extends Event {

  private static final HandlerList HANDLERS_LIST = new HandlerList();
  private final Player player;

  public HWPlayerDeathEvent(@NotNull Player player) {
    this.player = player;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

}
