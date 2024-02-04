package com.hegemonstudio.hegeworld.modules.grounditems;

import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GroundItemData {

  private final World world;
  private final UUID frameUuid;
  private ItemStack itemStack;

  public GroundItemData(@NotNull ItemFrame groundFrame, @NotNull ItemStack item) {
    this.world = groundFrame.getWorld();
    this.frameUuid = groundFrame.getUniqueId();
    this.itemStack = item;
  }

  public GroundItemData(@NotNull World world, @NotNull UUID groundFrameUuid, @NotNull ItemStack item) {
    this.world = world;
    this.frameUuid = groundFrameUuid;
    this.itemStack = item;
  }

  public World getWorld() {
    return world;
  }

  public ItemFrame getItemFrame() {
    // TODO capture data corruption
    // TODO check frame is valid
    return (ItemFrame) world.getEntity(frameUuid);
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public void setItemStack(@NotNull ItemStack item) {
    this.itemStack = item;
    getItemFrame().setItem(this.itemStack);
  }

}
