package com.hegemonstudio.hegeworld.modules.grounditems;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@ToString
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

  public void setItemStack(@NotNull ItemStack item) {
    itemStack = item;
    getItemFrame().setItem(itemStack);
  }

  public ItemFrame getItemFrame() {
    // TODO capture data corruption
    // TODO check frame is valid
    return (ItemFrame) world.getEntity(frameUuid);
  }

}
