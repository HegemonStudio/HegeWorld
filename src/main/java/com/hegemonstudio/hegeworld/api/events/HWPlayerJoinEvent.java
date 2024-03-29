package com.hegemonstudio.hegeworld.api.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class HWPlayerJoinEvent extends Event {

  private static final HandlerList HANDLERS_LIST = new HandlerList();
  private final Player player;
  private Component joinMessage;

  public HWPlayerJoinEvent(@NotNull Player player) {
    this.player = player;
    this.joinMessage = null;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public Player getPlayer() {
    return player;
  }

  public Optional<Component> getJoinMessage() {
    return Optional.ofNullable(joinMessage);
  }

  public void setJoinMessage(Component joinMessage) {
    this.joinMessage = joinMessage;
  }
}
