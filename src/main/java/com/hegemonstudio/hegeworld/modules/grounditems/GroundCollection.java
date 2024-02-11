package com.hegemonstudio.hegeworld.modules.grounditems;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.events.HWPlayerGroundSpawnEvent;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.hegemonstudio.hegeworld.HegeWorld.*;

public class GroundCollection {

  public static final String ITEM_FRAME_METADATA_KEY = "natural";
  public static final String ITEM_FRAME_DATA_KEY = "grounditems";

  private static final Map<UUID, GroundItemData> GROUND_ITEMS = new HashMap<>();

  public static boolean IsGroundItem(@Nullable ItemFrame frame) {
    if (frame == null) return false;
    return frame.hasMetadata(ITEM_FRAME_DATA_KEY);
  }

  public static boolean IsGroundItem(@Nullable Entity entity) {
    if (entity == null) return false;
    if (!(entity instanceof ItemFrame)) return false;
    return IsGroundItem((ItemFrame) entity);
  }

  public static @NotNull Optional<GroundItemData> GetGroundItemData(@Nullable ItemFrame frame) {
    if (frame == null) return Optional.empty();
    if (!IsGroundItem(frame)) return Optional.empty();
    UUID uuid = frame.getUniqueId();
    return Optional.ofNullable(GROUND_ITEMS.get(uuid));
  }

  public static @NotNull Optional<GroundItemData> GetGroundItemData(@Nullable Entity entity) {
    if (entity == null) return Optional.empty();
    if (!(entity instanceof ItemFrame)) return Optional.empty();
    return GetGroundItemData((ItemFrame) entity);
  }

  public static Optional<ItemFrame> GetFrame(@Nullable Entity entity) {
    if (entity == null) return Optional.empty();
    if (!(entity instanceof ItemFrame frame)) return Optional.empty();
    if (!IsGroundItem(frame)) return Optional.empty();
    return Optional.of(frame);
  }

  public static @NotNull ItemStack GetItemStackFromGround(@NotNull ItemFrame frame) {
    return frame.getItem();
  }

  public static void RemoveGroundItem(@NotNull ItemFrame frame) {
    UnsetFrame(frame.getWorld(), frame.getUniqueId());
    frame.remove();
  }

  /**
   * @param location
   * @param item
   * @deprecated
   */
  public static void SpawnGroundItemLegacy(@NotNull Location location, @NotNull ItemStack item) {
    HWPlayerGroundSpawnEvent spawnEvent = hwCallEvent(new HWPlayerGroundSpawnEvent(location, item));
    if (spawnEvent.isCancelled()) return;

    ItemFrame frame = hwSpawn(spawnEvent.getLocation(), ItemFrame.class);
    frame.setVisible(false);
    frame.setRotation(Rotation.values()[(int) Math.floor(Math.random() * Rotation.values().length)]);
    frame.setItem(spawnEvent.getItemStack());
    hwSetMetadata(frame, ITEM_FRAME_METADATA_KEY, true);

    SaveFrame(frame);
  }

  public static void SpawnGroundItem(@NotNull Location location, @NotNull ItemStack item) {
    HWPlayerGroundSpawnEvent spawnEvent = hwCallEvent(new HWPlayerGroundSpawnEvent(location, item));
    if (spawnEvent.isCancelled()) return;

    location = spawnEvent.getLocation();
    item = spawnEvent.getItemStack();

    // Spawn ItemFrame
    ItemFrame frame = hwSpawn(location, ItemFrame.class);
    frame.setVisible(false);
    frame.setGravity(false);
    frame.setInvulnerable(true);
    frame.setItemDropChance(0);
    frame.setFacingDirection(BlockFace.UP);
    frame.setItem(item);
    frame.setRotation(Rotation.values()[(int) Math.floor(Math.random() * Rotation.values().length)]);

    // Add mechanics
    GroundItemData data = new GroundItemData(frame, item);
    GROUND_ITEMS.put(frame.getUniqueId(), data);

    hwSetMetadata(frame, ITEM_FRAME_DATA_KEY, true);
    hwSetMetadata(frame, ITEM_FRAME_METADATA_KEY, true);

    SaveFrame(frame);
    // TODO SaveFrameData(data);
  }

  public static void LoadFrames(@Nullable World world) {
    if (world == null) return;
    FileConfiguration data = HegeWorldPlugin.GetInstance().getWorldsData();
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
      if (!(entity instanceof ItemFrame frame)) {
        UnsetFrame(world, uuid);
        continue;
      }
      hwSetMetadata(frame, ITEM_FRAME_METADATA_KEY, true);
      successfullyLoaded += 1;
    }
    hwLog("Loaded " + successfullyLoaded + "/" + suids.size() + " ground items.");
  }

  public static void SaveFrame(@Nullable ItemFrame frame) {
    if (frame == null) return;
    FileConfiguration data = HegeWorldPlugin.GetInstance().getWorldsData();
    World world = frame.getWorld();
    String groundItemsPath = world.getName() + "." + ITEM_FRAME_DATA_KEY;
    List<String> uuids = data.isSet(groundItemsPath) ? data.getStringList(groundItemsPath) : new ArrayList<>();
    uuids.add(frame.getUniqueId().toString());
    data.set(groundItemsPath, uuids);
    HegeWorldPlugin.GetInstance().saveWorldsData();
  }

  public static void UnsetFrame(@NotNull World world, @NotNull UUID uuid) {
    FileConfiguration data = HegeWorldPlugin.GetInstance().getWorldsData();
    String groundItemsPath = world.getName() + "." + ITEM_FRAME_DATA_KEY;
    if (!data.isSet(groundItemsPath)) return;
    List<String> uuids = data.getStringList(groundItemsPath);
    uuids.remove(uuid.toString());
    data.set(groundItemsPath, uuids);
    HegeWorldPlugin.GetInstance().saveWorldsData();
  }

}
