package com.hegemonstudio.hegeworld.api.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class HWPlayerGroundSpawnEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS_LIST = new HandlerList();

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  private Location location;
  private ItemStack itemStack;

  private boolean isCancelled;

  public HWPlayerGroundSpawnEvent(@NotNull Location location, @NotNull ItemStack itemStack) {
    this.location = location;
    this.itemStack = itemStack;
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.isCancelled = cancelled;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public void setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;
  }
}
