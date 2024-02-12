package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.api.tasks.TaskManager;
import com.hegemonstudio.hegeworld.api.util.ChatUtil;
import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.module.HWModuleManager;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HegeWorld static method class
 */
public class HegeWorld {

  public static final Object NULL = null;

  private static List<String> itemSelectors;
  private static int itemSelectorsCustomItemCount;

  private HegeWorld() {

  }

  public static @NotNull HegeWorldPlugin hwPlugin() {
    return Objects.requireNonNull(HegeWorldPlugin.GetInstance());
  }

  public static void hwBroadcast(@Nullable Component message) {
    if (message == null) return;
    Bukkit.broadcast(message);
  }

  public static @NotNull String hwStr(@Nullable String str) {
    if (str == null) return "null";
    return str;
  }

  public static @NotNull String hwStr(@Nullable Object object) {
    return String.valueOf(object);
  }

  public static @NotNull String hwStr(@Nullable ItemStack item) {
    if (item == null) return "null";
    return MessageFormat.format(
        "{0} {1}x",
        PlainTextComponentSerializer.plainText().serialize(item.displayName()),
        item.getAmount()
    );
  }

  public static @NotNull Stream<? extends Player> hwPlayers() {
    return Bukkit.getOnlinePlayers()
        .stream();
  }

  public static @NotNull Stream<? extends Player> hwPlayers(@NotNull World world) {
    return Bukkit.getOnlinePlayers()
        .stream()
        .filter((player) -> player.getWorld().equals(world));
  }

  public static <T extends Entity> @NotNull List<T> hwGetEntities(@NotNull Class<T> entityClass) {
    return new ArrayList<>(hwWorld().getEntitiesByClass(entityClass));
  }


  public static @NotNull List<Entity> hwGetEntitiesWildcard(@NotNull Class<? extends Entity> classes) {
    return new ArrayList<>(hwWorld().getEntitiesByClasses(classes));
  }

  public static @NotNull List<Entity> hwGetEntities() {
    return new ArrayList<>(hwWorld().getEntities());
  }

  @Contract("_ -> new")
  public static @NotNull NamespacedKey hwKey(@NotNull String value) {
    return HegeWorldPlugin.CreateKey(value.strip().replace(' ', '_').toLowerCase());
  }

  public static @NotNull HWModuleManager hwModules() {
    return HegeWorldPlugin.GetModuleManager();
  }

  public static <T extends HWModule> @Nullable T hwGetModule(@NotNull Class<T> moduleClass) {
    HWModule module = hwModules().getModule(moduleClass).orElse(null);
    if (module == null) return null;
    return moduleClass.cast(module);
  }

  public static void hwRegisterListener(@Nullable Listener listener) {
    if (listener == null) return;
    Bukkit.getPluginManager().registerEvents(listener, hwPlugin());
  }

  public static void hwRegisterItem(@NotNull String key, @NotNull CustomItem item) {
    NamespacedKey namespacedKey = hwKey(key);
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, namespacedKey, item);
  }

  public static void hwOnTickLater(@NotNull Runnable action) {
    Bukkit.getScheduler().runTaskLater(hwPlugin(), action, 1L);
  }

  public static boolean hwHasPlayerPermission(@Nullable Player player, @NotNull String permission) {
    return player != null && player.hasPermission(permission);
  }

  public static <T extends Event> @NotNull T hwCallEvent(@NotNull T event) {
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }


  public static @NotNull List<String> hwGetRecipeSelectors() {
    return hwGetRecipes().stream()
        .map(HWRecipe::getRecipeId)
        .map(NamespacedKey::toString)
        .collect(Collectors.toList());
  }

  public static @NotNull List<String> hwGetRecipeSelectors(@NotNull CraftingSource... sources) {
    return hwGetRecipes(sources).stream()
        .map(HWRecipe::getRecipeId)
        .map(NamespacedKey::toString)
        .collect(Collectors.toList());
  }

  public static @NotNull String hwOnTick(@NotNull Runnable action) {
    return TaskManager.OnTick(action);
  }

  public static @NotNull CraftingManager hwCraftings() {
    return HegeWorldPlugin.GetCraftingManager();
  }

  public static void hwAddRecipe(@NotNull NamespacedKey key, @NotNull HWRecipe recipe) {
    hwCraftings().addRecipe(key, recipe);
  }

  public static void hwAddRecipe(@NotNull String key, @NotNull HWRecipe recipe) {
    hwCraftings().addRecipe(hwKey(key), recipe);
  }

  public static @NotNull Collection<HWRecipe> hwGetRecipes() {
    return hwCraftings().getAll();
  }

  public static @NotNull Collection<HWRecipe> hwGetRecipes(CraftingSource... sources) {
    return hwCraftings().getAllBySources(sources);
  }

  public static @Nullable HWRecipe hwGetRecipe(@NotNull NamespacedKey key) {
    return hwCraftings().getRecipe(key).orElse(null);
  }

  public static @Nullable HWRecipe hwGetRecipe(@NotNull String key) {
    return hwCraftings().getRecipe(key).orElse(null);
  }

  public static @Nullable HWRecipe hwGetRecipe(@NotNull NamespacedKey key, @NotNull CraftingSource source) {
    return hwCraftings().getRecipe(key, source).orElse(null);
  }

  public static @Nullable HWRecipe hwGetRecipe(@NotNull String key, @NotNull CraftingSource source) {
    return hwCraftings().getRecipe(key, source).orElse(null);
  }

  public static @NotNull World hwWorld() {
    return HegeWorldPlugin.GetMainWorld();
  }

  public static @Nullable Player hwGetPlayer(@NotNull String selector) {
    selector = selector.trim();

    if (selector.equalsIgnoreCase("@r")) return hwGetPlayerRandom();

    return Bukkit.getPlayer(selector);
  }

  public static @NotNull List<Player> hwGetPlayersOnline() {
    return new ArrayList<>(Bukkit.getOnlinePlayers());
  }

  public static @Nullable Player hwGetPlayerRandom() {
    List<Player> players = hwGetPlayersOnline();
    if (players.isEmpty()) return null;
    return players.get((int) Math.floor(Math.random() * players.size()));
  }

  public static @Nullable ItemStack hwGetItem(@NotNull String itemName, int count) {
    assert count >= 0;
    itemName = itemName.strip().replace(' ', '_').toLowerCase();
    NamespacedKey key = NamespacedKey.fromString(itemName);
    if (key != null) {
      if (!key.getNamespace().equalsIgnoreCase(NamespacedKey.MINECRAFT_NAMESPACE)) {
        CustomItem item = ImpactRegistries.CUSTOM_ITEM.get(key);
        if (item == null) return null;
        return item.getItemStack(count);
      }
      itemName = itemName.replaceFirst(NamespacedKey.MINECRAFT_NAMESPACE + ":", "");
    }
    itemName = itemName.toUpperCase();
    Material material = Material.getMaterial(itemName);
    if (material == null) return null;
    return new ItemStack(material, count);
  }

  public static @Nullable ItemStack hwGetItem(@NotNull String itemName) {
    return hwGetItem(itemName, 1);
  }

  public static void hwPlayerGiveItem(@NotNull Player player, @NotNull ItemStack item) {
    HWPlayer.of(player).giveItem(item);
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

  public static @Nullable World hwGetWorld(@NotNull String name) {
    return Bukkit.getWorld(name);
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

  public static @Nullable World hwCreateWorld(@NotNull String name) {
    return WorldCreator
        .name(name)
        .environment(World.Environment.NORMAL)
        .type(WorldType.NORMAL)
        .createWorld();
  }

  public static void hwLog(@NotNull Component message) {
    HWLogger.Log(message);
  }

  public static void hwLog(@NotNull String message) {
    HWLogger.Log(Component.text(message));
  }

  public static void hwLog(@NotNull Object message) {
    hwLog(String.valueOf(message));
  }

  public static @NotNull List<Player> hwGetPlayers(@NotNull String selector) {
    return hwGetPlayers(selector, null);
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

  public static @NotNull List<String> hwGetPlayerSelectors() {
    return hwGetPlayerSelectors(null);
  }

  public static @NotNull Item hwDropItem(@NotNull Location location, @NotNull ItemStack item) {
    return location.getWorld().dropItemNaturally(location, item);
  }

  public static @NotNull Item hwSpawnItem(@NotNull Location location, @NotNull ItemStack item) {
    return hwDropItem(location, item);
  }

  public static <T extends Entity> @NotNull T hwSpawn(@NotNull Location location, @NotNull Class<T> toSpawn) {
    return location.getWorld().spawn(location, toSpawn);
  }

  public static @NotNull Entity hwSpawn(@NotNull Location location, @NotNull EntityType entityType) {
    assert entityType.getEntityClass() != null;
    return location.getWorld().spawn(location, entityType.getEntityClass());
  }

  public static <T extends Metadatable> @NotNull T hwSetMetadata(@NotNull T metadatable, @NotNull String key, @NotNull Object value) {
    metadatable.setMetadata(key, new FixedMetadataValue(hwPlugin(), value));
    return metadatable;
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

  public static boolean hwBlockDestroy(@Nullable Block block) {
    return hwBlockDestroy(block, null);
  }

}
