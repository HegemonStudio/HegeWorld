package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.events.HWPlayerGroundSpawnEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroundCollection {

  public static final String ITEM_FRAME_METADATA_KEY = "natural";
  public static final String ITEM_FRAME_DATA_KEY = "grounditems";

  public static boolean IsGroundItem(@Nullable ItemFrame frame) {
    if (frame == null) return false;
    return frame.hasMetadata(ITEM_FRAME_METADATA_KEY);
  }

  public static boolean IsGroundItem(@Nullable Entity entity) {
    if (entity == null) return false;
    if (!(entity instanceof ItemFrame)) return false;
    return IsGroundItem((ItemFrame) entity);
  }

  public static Optional<ItemFrame> GetGroundItem(@Nullable Entity entity) {
    if (entity == null) return Optional.empty();
    if (!(entity instanceof ItemFrame)) return Optional.empty();
    ItemFrame frame = (ItemFrame) entity;
    if(!IsGroundItem(frame)) return Optional.empty();
    return Optional.of(frame);
  }

  public static @NotNull ItemStack GetItemStackFromGround(@NotNull ItemFrame frame) {
    return frame.getItem();
  }

  public static void RemoveGroundItem(@NotNull ItemFrame frame) {
    UnsetFrame(frame.getWorld(), frame.getUniqueId());
    frame.remove();
  }

  public static void SpawnGroundItem(@NotNull Location location, @NotNull ItemStack item) {
    PluginManager pm = Bukkit.getPluginManager();
    HWPlayerGroundSpawnEvent spawnEvent = new HWPlayerGroundSpawnEvent(location, item);
    pm.callEvent(spawnEvent);

    if (spawnEvent.isCancelled()) return;

    World world = spawnEvent.getLocation().getWorld();
    ItemFrame frame = (ItemFrame) world.spawnEntity(spawnEvent.getLocation(), EntityType.ITEM_FRAME);
    frame.setVisible(false);
    frame.setRotation(Rotation.values()[(int) Math.floor(Math.random() * Rotation.values().length)]);
    frame.setItem(spawnEvent.getItemStack());
    frame.setMetadata(ITEM_FRAME_METADATA_KEY, new FixedMetadataValue(HegeWorldPlugin.getInstance(), true));

    SaveFrame(frame);
  }

  public static void LoadFrames(@Nullable World world) {
    if (world == null) return;
    FileConfiguration data = HegeWorldPlugin.getInstance().getWorldsData();
    String groundItemsPath = world.getName() + "." + ITEM_FRAME_DATA_KEY;
    if (!data.isSet(groundItemsPath)) return;
    List<String> suids = data.getStringList(groundItemsPath);
    long successfullyLoaded = 0;
    for (String suid : suids) {
      UUID uuid = UUID.fromString(suid);
      Entity entity = world.getEntity(uuid);
      if (entity == null) {
        UnsetFrame(world, uuid);
        continue;
      }
      if (!(entity instanceof ItemFrame)) {
        UnsetFrame(world, uuid);
        continue;
      }
      ItemFrame frame = (ItemFrame) entity;
      frame.setMetadata(ITEM_FRAME_METADATA_KEY, new FixedMetadataValue(HegeWorldPlugin.getInstance(), true));
      successfullyLoaded += 1;
    }
    HWLogger.Log(Component.text("Loaded " + successfullyLoaded + "/" + suids.size() + " ground items."));
  }

  public static void SaveFrame(@Nullable ItemFrame frame) {
    if (frame == null) return;
    FileConfiguration data = HegeWorldPlugin.getInstance().getWorldsData();
    World world = frame.getWorld();
    String groundItemsPath = world.getName() + "." + ITEM_FRAME_DATA_KEY;
    List<String> uuids = data.isSet(groundItemsPath) ? data.getStringList(groundItemsPath) : new ArrayList<>();
    uuids.add(frame.getUniqueId().toString());
    data.set(groundItemsPath, uuids);
    HegeWorldPlugin.getInstance().saveWorldsData();
  }

  public static void UnsetFrame(@NotNull World world, @NotNull UUID uuid) {
    FileConfiguration data = HegeWorldPlugin.getInstance().getWorldsData();
    String groundItemsPath = world.getName() + "." + ITEM_FRAME_DATA_KEY;
    if (!data.isSet(groundItemsPath)) return;
    List<String> uuids = data.getStringList(groundItemsPath);
    uuids.remove(uuid.toString());
    data.set(groundItemsPath, uuids);
    HegeWorldPlugin.getInstance().saveWorldsData();
  }

}
