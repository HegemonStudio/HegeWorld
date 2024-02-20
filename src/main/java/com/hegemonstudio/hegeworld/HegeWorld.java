package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.api.tasks.TaskManager;
import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HwRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.module.HWModuleManager;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import com.impact.lib.api.util.Result;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.impact.lib.api.util.Result.Err;
import static com.impact.lib.api.util.Result.Ok;

/**
 * HegeWorld static method class
 *
 * @since 1.0-SNAPSHOT
 */
public final class HegeWorld {

  private static List<String> itemSelectors;
  private static int itemSelectorsCustomItemCount;

  private HegeWorld() {

  }

  /**
   * Sends stringed message of object to console and every online player.<br>
   * If message object is null message will not be sent.
   *
   * @param message The object message.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static void hwBroadcast(@Nullable Object message) {
    if (message == null) return;
    hwBroadcast(hwStr(message));
  }

  /**
   * Sends message to console and every online player.<br>
   * If message is null message will not be sent.
   *
   * @param message The string message.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static void hwBroadcast(@Nullable String message) {
    if (message == null) return;
    hwBroadcast(Component.text(message));
  }

  /**
   * Stringifies {@link Object} and returns the string.<br>
   * If given object is null returns "null"
   *
   * @param object The {@link Object} to stringify.
   * @return The stringed {@link Object}.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull String hwStr(@Nullable Object object) {
    return String.valueOf(object);
  }

  /**
   * Sends message to console and every online player.<br>
   * If message is null message will not be sent.
   *
   * @param message The {@link Component} message.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static void hwBroadcast(@Nullable Component message) {
    if (message == null) return;
    Bukkit.broadcast(message);
  }

  /**
   * Stringifies object and returns the string.<br>
   * If given object is null returns "null"
   *
   * @param str The object to stringify.
   * @return The stringed object.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull String hwStr(@Nullable String str) {
    if (str == null) return "null";
    return str;
  }

  /**
   * Stringifies {@link ItemStack} in format <i>(NAME AMOUNT)</i> and returns the string.<br>
   * If given object is null returns "null"
   *
   * @param item The item stack to stringify.
   * @return The stringed item stack.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull String hwStr(@Nullable ItemStack item) {
    if (item == null) return "null";
    return MessageFormat.format(
        "{0} {1}x",
        PlainTextComponentSerializer.plainText().serialize(item.displayName()).replace("[", "").replace("]", ""),
        item.getAmount()
    );
  }

  /**
   * Stringifies {@link Material} and returns the string.<br>
   * If given material is null returns "null".
   *
   * @param material The {@link Material} to stringify.
   * @return The stringed {@link Material}.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull String hwStr(@Nullable Material material) {
    if (material == null) return "null";
    return WordUtils.capitalizeFully(material.toString().replace('_', ' ').toLowerCase());
  }

  public static @NotNull String hwStr(@Nullable Location location) {
    if (location == null) return "null";
    return location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
  }

  /**
   * Parses given string to int and returns {@link Result}.
   *
   * @param value The string of number.
   * @return The result of integer or {@link NumberFormatException}
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static Result<Integer, NumberFormatException> hwInt(@Nullable String value) {
    if (value == null) return Err(new NumberFormatException());
    try {
      return Ok(Integer.parseInt(value));
    } catch (NumberFormatException exception) {
      return Err(exception);
    }
  }

  /**
   * Gets the online {@link Player} stream.
   *
   * @return The online {@link Player} stream.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull Stream<? extends Player> hwPlayers() {
    return Bukkit.getOnlinePlayers()
        .stream();
  }

  /**
   * Gets the online {@link Player} stream filtered by given {@link World}.
   *
   * @param world The given {@link World}.
   * @return The filtered online {@link Player} stream.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull Stream<? extends Player> hwPlayers(@NotNull World world) {
    return Bukkit.getOnlinePlayers()
        .stream()
        .filter((player) -> player.getWorld().equals(world));
  }

  /**
   * Gets list of {@link Entity} by given entity class in HegeWorld main world.<br>
   * The HegeWorld main world you can access by {@link #hwWorld()}
   *
   * @param entityClass The given {@link Entity} class.
   * @param <T>         The {@link Entity} type.
   * @return The list of all entities in HegeWorld filtered by class.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static <T extends Entity> @NotNull List<T> hwGetEntities(@NotNull Class<T> entityClass) {
    return new ArrayList<>(hwWorld().getEntitiesByClass(entityClass));
  }

  /**
   * Gets the HegeWorld main {@link World}.
   *
   * @return The main world.
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull World hwWorld() {
    return HegeWorldPlugin.GetMainWorld();
  }

  /**
   * Gets the stream of all {@link World}s.
   *
   * @return The stream of all {@link World}s.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @NotNull Stream<World> hwWorlds() {
    return Bukkit.getWorlds()
        .stream();
  }

  public static @NotNull List<Entity> hwGetEntitiesWildcard(@NotNull Class<? extends Entity> classes) {
    return new ArrayList<>(hwWorld().getEntitiesByClasses(classes));
  }

  public static @NotNull List<Entity> hwGetEntities() {
    return new ArrayList<>(hwWorld().getEntities());
  }

  public static @NotNull Collection<HWModule> hwModules() {
    return hwModuleManager().getModules();
  }

  public static @NotNull HWModuleManager hwModuleManager() {
    return HegeWorldPlugin.GetModuleManager();
  }

  public static <T extends HWModule> @Nullable T hwGetModule(@NotNull Class<T> moduleClass) {
    HWModule module = hwModuleManager().getModule(moduleClass).orElse(null);
    if (module == null) return null;
    return moduleClass.cast(module);
  }

  public static void hwRegisterListener(@Nullable Listener listener) {
    if (listener == null) return;
    Bukkit.getPluginManager().registerEvents(listener, hwPlugin());
  }

  public static @NotNull HegeWorldPlugin hwPlugin() {
    return Objects.requireNonNull(HegeWorldPlugin.GetInstance());
  }

  public static void hwRegisterItem(@NotNull String key, @NotNull CustomItem item) {
    NamespacedKey namespacedKey = hwKey(key);
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, namespacedKey, item);
  }

  /**
   * Creates {@link NamespacedKey} with namespace of {@link HegeWorldPlugin} and given value.<br>
   * Method takes care of spaces and character case.
   *
   * @param value The given value.
   * @return The created {@link NamespacedKey}.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  @Contract("_ -> new")
  public static @NotNull NamespacedKey hwKey(@NotNull String value) {
    return HegeWorldPlugin.CreateKey(value.strip().replace(' ', '_').toLowerCase());
  }

  /**
   * Runs runnable action after 1 tick.<br>
   * 20 ticks = 1s
   *
   * @param action The runnable action to run.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static void hwOnTickLater(@NotNull Runnable action) {
    Bukkit.getScheduler().runTask(hwPlugin(), action);
  }

  public static boolean hwHasPlayerPermission(@Nullable Player player, @NotNull String permission) {
    return player != null && player.hasPermission(permission);
  }

  /**
   * Calls a given {@link Event} and returns after call.
   *
   * @param event The {@link Event} to call.
   * @param <T>   The object instanceof {@link Event}.
   * @return The called {@link Event}.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static <T extends Event> @NotNull T hwCallEvent(@NotNull T event) {
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }

  public static @NotNull List<String> hwGetRecipeSelectors() {
    return hwGetRecipes().stream()
        .map(HwRecipe::getRecipeId)
        .map(NamespacedKey::toString)
        .collect(Collectors.toList());
  }

  public static @NotNull Collection<HwRecipe> hwGetRecipes() {
    return hwCraftingManager().getAll();
  }

  public static @NotNull CraftingManager hwCraftingManager() {
    return HegeWorldPlugin.GetCraftingManager();
  }

  public static @NotNull List<String> hwGetRecipeSelectors(@NotNull CraftingSource... sources) {
    return hwGetRecipes(sources).stream()
        .map(HwRecipe::getRecipeId)
        .map(NamespacedKey::toString)
        .collect(Collectors.toList());
  }

  public static @NotNull Collection<HwRecipe> hwGetRecipes(CraftingSource... sources) {
    return hwCraftingManager().getAllBySources(sources);
  }

  public static @NotNull String hwOnTick(@NotNull Runnable action) {
    return TaskManager.OnTick(action);
  }

  public static void hwAddRecipe(@NotNull NamespacedKey key, @NotNull HwRecipe recipe) {
    hwCraftingManager().addRecipe(key, recipe);
  }

  public static void hwAddRecipe(@NotNull String key, @NotNull HwRecipe recipe) {
    hwCraftingManager().addRecipe(hwKey(key), recipe);
  }

  public static @Nullable HwRecipe hwGetRecipe(@NotNull NamespacedKey key) {
    return hwCraftingManager().getRecipe(key).orElse(null);
  }

  public static @Nullable HwRecipe hwGetRecipe(@NotNull String key) {
    return hwCraftingManager().getRecipe(key).orElse(null);
  }

  public static @Nullable HwRecipe hwGetRecipe(@NotNull NamespacedKey key, @NotNull CraftingSource source) {
    return hwCraftingManager().getRecipe(key, source).orElse(null);
  }

  public static @Nullable HwRecipe hwGetRecipe(@NotNull String key, @NotNull CraftingSource source) {
    return hwCraftingManager().getRecipe(key, source).orElse(null);
  }

  public static @Nullable Player hwGetPlayer(@NotNull String selector) {
    selector = selector.strip();

    if (selector.equalsIgnoreCase("@r")) return hwGetPlayerRandom();

    return Bukkit.getPlayer(selector);
  }

  public static @Nullable Player hwGetPlayerRandom() {
    List<Player> players = hwGetPlayersOnline();
    if (players.isEmpty()) return null;
    return players.get((int) Math.floor(Math.random() * players.size()));
  }

  public static @NotNull List<Player> hwGetPlayersOnline() {
    return new ArrayList<>(Bukkit.getOnlinePlayers());
  }

  /**
   * Creates {@link ItemStack} with given item name.<br>
   * Returns null if item is not found.<br>
   * The name can contain namespace. The default namespace is <code>minecraft:</code><br>
   * Example: {@code "stone", "minecraft:dirt", "hegeworld:ak47", "diamond sword"}
   *
   * @param itemName The given item name.
   * @return New item stack with amount equals 1.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @Nullable ItemStack hwItem(@NotNull String itemName) {
    return hwItem(itemName, 1);
  }

  /**
   * Creates {@link ItemStack} with given item name and amount.<br>
   * Returns null if item is not found.<br>
   * The name can contain namespace. The default namespace is <code>minecraft:</code><br>
   * Example: {@code "stone", "minecraft:dirt", "hegeworld:ak47", "diamond sword"}
   *
   * @param itemName The given item name.
   * @param amount   The item amount.
   * @return New item stack with given amount.
   * @author Tymon Woźniak
   * @since 1.0-SNAPSHOT
   */
  public static @Nullable ItemStack hwItem(@NotNull String itemName, int amount) {
    assert amount >= 0;
    itemName = itemName.strip().replace(' ', '_').toLowerCase();
    NamespacedKey key = NamespacedKey.fromString(itemName);
    if (key != null) {
      if (!key.getNamespace().equalsIgnoreCase(NamespacedKey.MINECRAFT_NAMESPACE)) {
        CustomItem item = ImpactRegistries.CUSTOM_ITEM.get(key);
        if (item == null) return null;
        return item.getItemStack(amount);
      }
      itemName = itemName.replaceFirst(NamespacedKey.MINECRAFT_NAMESPACE + ":", "");
    }
    itemName = itemName.toUpperCase();
    Material material = Material.getMaterial(itemName);
    if (material == null) return null;
    return new ItemStack(material, amount);
  }

  public static @NotNull ItemStack hwItem(@NotNull Material material) {
    return hwItem(material, 1);
  }

  public static @NotNull ItemStack hwItem(@NotNull Material material, int amount) {
    return new ItemStack(material, amount);
  }

  public static void hwPlayerGiveItem(@NotNull Player player, @NotNull ItemStack item) {
    HWPlayer.of(player).giveItem(item);
  }

  public static @NotNull List<String> hwGetWorldNames() {
    return Bukkit.getWorlds()
        .stream()
        .map(WorldInfo::getName)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public static @NotNull List<String> hwGetPlayerNames() {
    return Bukkit.getOnlinePlayers()
        .stream()
        .map(Player::getName)
        .collect(Collectors.toList());
  }

  public static boolean hwIsWorldExists(@Nullable String name) {
    return name != null && hwGetWorld(name) != null;
  }

  public static @Nullable World hwGetWorld(@NotNull String name) {
    return Bukkit.getWorld(name);
  }

  public static @Nullable World hwCreateWorld(@NotNull String name) {
    return WorldCreator
        .name(name)
        .environment(World.Environment.NORMAL)
        .type(WorldType.NORMAL)
        .createWorld();
  }

  public static void hwLog(@Nullable String format, @NotNull Object... args) {
    hwLog(hwTextColor(format, args));
  }

  public static void hwLog(@NotNull Component message) {
    HWLogger.Log(message);
  }

  public static @NotNull Component hwTextColor(@Nullable String content, Object... args) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize((new MessageFormat(String.valueOf(content))).format(args));
  }

  public static void hwLog(@NotNull Object message) {
    hwLog(String.valueOf(message));
  }

  public static void hwLog(@NotNull String message) {
    HWLogger.Log(Component.text(message));
  }

  public static @NotNull List<Player> hwGetPlayers(@NotNull String selector) {
    return hwGetPlayers(selector, null);
  }

  public static @NotNull List<Player> hwGetPlayers(@NotNull String selector, @Nullable Player context) {
    if (selector.equalsIgnoreCase("@p")) {
      assert context != null;
      return List.of(context);
    }
    if (selector.equalsIgnoreCase("@a")) {
      return new ArrayList<>(Bukkit.getOnlinePlayers());
    }
    if (selector.equalsIgnoreCase("@r")) {
      List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
      return List.of(players.get((int) Math.floor(Math.random() * players.size())));
    }
    Player found = Bukkit.getPlayer(selector);
    if (found != null) {
      return List.of(found);
    }
    return List.of();
  }

  public static @NotNull List<String> hwGetPlayerSelectors() {
    return hwGetPlayerSelectors(null);
  }

  public static @NotNull List<String> hwGetPlayerSelectors(@Nullable Player context) {
    List<String> strings = new ArrayList<>();
    if (context != null) {
      strings.add("@p");
      strings.add("@a");
    }
    strings.add("@r");
    strings.addAll(Bukkit.getOnlinePlayers()
        .stream()
        .filter((player -> context == null || context.canSee(player)))
        .map(Player::getName)
        .collect(Collectors.toCollection(ArrayList::new))
    );
    return strings;
  }

  public static @NotNull Item hwSpawnItem(@NotNull Location location, @NotNull ItemStack item) {
    return hwDropItem(location, item);
  }

  public static @NotNull Item hwDropItem(@NotNull Location location, @NotNull ItemStack item) {
    return location.getWorld().dropItemNaturally(location, item);
  }

  public static <T extends Entity> @NotNull T hwSpawn(@NotNull Location location, @NotNull Class<T> toSpawn) {
    return location.getWorld().spawn(location, toSpawn);
  }

  public static @NotNull Entity hwSpawn(@NotNull Location location, @NotNull EntityType entityType) {
    assert entityType.getEntityClass() != null;
    return location.getWorld().spawn(location, entityType.getEntityClass());
  }

  public static <T extends Metadatable> void hwSetMetadata(@NotNull T metadatable, @NotNull String key, @NotNull Object value) {
    metadatable.setMetadata(key, new FixedMetadataValue(hwPlugin(), value));
  }

  public static <T extends Metadatable> void hwRemoveMetadata(@NotNull T metadatable, @NotNull String key) {
    metadatable.removeMetadata(key, hwPlugin());
  }

  public static <M extends Metadatable, T> @Nullable T hwGetMetadata(@NotNull M metadatable, @NotNull String key, @NotNull Class<T> type) {
    if (!metadatable.hasMetadata(key)) return null;
    return type.cast(metadatable.getMetadata(key).get(0).value());
  }

  public static void hwSpawnGroundItem(@NotNull Location location, @NotNull ItemStack item) {
    GroundCollection.SpawnGroundItem(location, item);
  }

  public static @NotNull List<String> hwGetItemSelectors() {
    if (itemSelectorsCustomItemCount == ImpactRegistries.CUSTOM_ITEM.getAll().size() && itemSelectors != null) {
      return itemSelectors;
    }
    List<String> items = new ArrayList<>();
    for (Material material : Material.values()) {
      items.add(NamespacedKey.MINECRAFT_NAMESPACE + ":" + material.toString().toLowerCase());
    }
    for (CustomItem customItem : ImpactRegistries.CUSTOM_ITEM.getAll()) {
      items.add(customItem.getNamespacedKey().toString());
    }
    itemSelectors = items;
    itemSelectorsCustomItemCount = ImpactRegistries.CUSTOM_ITEM.getAll().size();
    return items;
  }

  public static boolean hwBlockDestroy(@Nullable Block block) {
    return hwBlockDestroy(block, null);
  }

  public static boolean hwBlockDestroy(@Nullable Block block, @Nullable Entity destroyer) {
    if (block == null) return false;
    Material beforeDestroy = block.getType();
    if (destroyer != null) {
      if (destroyer instanceof LivingEntity livingEntity) {
        for (ItemStack drop : block.getDrops(livingEntity.getActiveItem(), destroyer)) {
          block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }
      }
      if (destroyer instanceof Player player) {
        player.incrementStatistic(Statistic.MINE_BLOCK, beforeDestroy);
      }
    }
    block.setType(Material.AIR);
    return beforeDestroy != block.getType();
  }

  public static boolean hwBlockSet(@Nullable Block block, @NotNull Material material) {
    if (block == null) return false;
    block.setType(material);
    return true;
  }

  public static boolean hwIsBlockExists(@Nullable Location location) {
    if (location == null) return false;
    return hwIsBlockExists(location.getBlock());
  }

  public static boolean hwIsBlockExists(@Nullable Block block) {
    if (block == null) return false;
    return !block.getType().equals(Material.AIR);
  }

  public static @NotNull Block hwGetBlock(int x, int y, int z) {
    return hwLoc(x, y, z).getBlock();
  }

  /**
   * Creates {@link Location} with given coordinates and HegeWorld main world.<br>
   * The  main world you can assess by {@link HegeWorld#hwWorld()}
   *
   * @param x The X-Coordinate
   * @param y The Y-Coordinate
   * @param z The Z-Coordinate
   * @return The created location.
   * @author Tymon Woźniak
   */
  public static @NotNull Location hwLoc(double x, double y, double z) {
    return new Location(hwWorld(), x, y, z);
  }

  @Contract("_ -> new")
  public static @NotNull @Unmodifiable Stream<Block> hwGetBlockNeighbours(@NotNull Block block) {
    return Stream.of(hwGetBlock(block, 0, 1, 0), hwGetBlock(block, 0, -1, 0),
        hwGetBlock(block, 1, 0, 0), hwGetBlock(block, -1, 0, 0),
        hwGetBlock(block, 0, 0, 1), hwGetBlock(block, 0, 0, -1));
  }

  public static @NotNull Block hwGetBlock(@NotNull Block block, int relativeX, int relativeY, int relativeZ) {
    return hwGetBlock(block.getLocation(), relativeX, relativeY, relativeZ);
  }

  public static @NotNull Block hwGetBlock(@NotNull Location location, int relativeX, int relativeY, int relativeZ) {
    return hwGetBlock(hwOffset(location, relativeX, relativeY, relativeZ));
  }

  public static @NotNull Block hwGetBlock(@NotNull Location location) {
    return location.getBlock();
  }

  public static @NotNull Location hwOffset(@NotNull Location location, double x, double y, double z) {
    return location.clone().add(x, y, z);
  }

  public static <P, C> void hwSetData(@NotNull ItemStack item, @NotNull String key, @NotNull PersistentDataType<P, C> type, @NotNull C value) {
    NamespacedKey namespacedKey = hwKey(key);
    ItemMeta meta = item.getItemMeta();
    meta.getPersistentDataContainer().set(namespacedKey, type, value);
    item.setItemMeta(meta);
  }

  public static <P, C> @Nullable C hwGetData(@NotNull ItemStack item, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    if (!hwHasData(item, key, type)) return null;
    return item.getItemMeta().getPersistentDataContainer().get(hwKey(key), type);
  }

  public static <P, C> boolean hwHasData(@NotNull ItemStack item, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    NamespacedKey namespacedKey = hwKey(key);
    ItemMeta meta = item.getItemMeta();
    return meta.getPersistentDataContainer().has(namespacedKey, type);
  }

  public static <P, C> void hwSetData(@NotNull Entity entity, @NotNull String key, @NotNull PersistentDataType<P, C> type, @NotNull C value) {
    NamespacedKey namespacedKey = hwKey(key);
    entity.getPersistentDataContainer().set(namespacedKey, type, value);
  }

  public static <P, C> @Nullable C hwGetData(@NotNull Entity entity, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    if (!hwHasData(entity, key, type)) return null;
    return entity.getPersistentDataContainer().get(hwKey(key), type);
  }

  public static <P, C> boolean hwHasData(@NotNull Entity entity, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    NamespacedKey namespacedKey = hwKey(key);
    return entity.getPersistentDataContainer().has(namespacedKey, type);
  }

  public static <P, C> boolean hwSetData(@NotNull Block block, @NotNull String key, @NotNull PersistentDataType<P, C> type, @NotNull C value) {
    if (!(block instanceof TileState)) return false;
    TileState state = (TileState) block.getState();
    NamespacedKey namespacedKey = hwKey(key);
    state.getPersistentDataContainer().set(namespacedKey, type, value);
    state.update();
    return true;
  }

  public static <P, C> @Nullable C hwGetData(@NotNull Block block, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    if (!hwHasData(block, key, type)) return null;
    TileState state = (TileState) block.getState();
    return state.getPersistentDataContainer().get(hwKey(key), type);
  }

  public static <P, C> boolean hwHasData(@NotNull Block block, @NotNull String key, @NotNull PersistentDataType<P, C> type) {
    if (!(block instanceof TileState)) return false;
    TileState state = (TileState) block.getState();
    NamespacedKey namespacedKey = hwKey(key);
    return state.getPersistentDataContainer().has(namespacedKey, type);
  }

  public static @NotNull Component hwText(@Nullable String content, Object... args) {
    return Component.text((new MessageFormat(String.valueOf(content))).format(args));
  }

}
