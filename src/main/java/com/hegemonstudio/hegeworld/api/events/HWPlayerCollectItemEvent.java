package com.hegemonstudio.hegeworld.api.events;

import com.hegemonstudio.hegeworld.api.HWPlayer;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class HWPlayerCollectItemEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS_LIST = new HandlerList();
  private final HWPlayer player;
  private final Location source;
  private final CollectionType collectionType;
  private ItemStack itemStack;
  private boolean isCancelled;
  private boolean playDefaultPickupSound = true;
  public HWPlayerCollectItemEvent(@NotNull HWPlayer player, @NotNull Location source, @NotNull ItemStack itemStack, @NotNull CollectionType collectionType) {
    this.player = player;
    this.source = source;
    this.itemStack = itemStack;
    this.collectionType = collectionType;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public HWPlayer getPlayer() {
    return player;
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.isCancelled = cancelled;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public void setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public CollectionType getCollectionType() {
    return collectionType;
  }

  public boolean isPlayDefaultPickupSound() {
    return playDefaultPickupSound;
  }

  public void setPlayDefaultPickupSound(boolean playDefaultSound) {
    this.playDefaultPickupSound = playDefaultSound;
  }

  public Location getSource() {
    return source;
  }

  public enum CollectionType {
    HAND_FROM_GROUND, PICKUP_ITEM
  }

}
