package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.module.HWModuleManager;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

  @Contract("_ -> new")
  public static @NotNull NamespacedKey hwKey(@NotNull String value) {
    return HegeWorldPlugin.CreateKey(value);
  }

  public static @NotNull HWModuleManager hwModules() {
    return HegeWorldPlugin.GetModuleManager();
  }

  public static <T extends HWModule> @Nullable T hwGetModule(@NotNull Class<T> moduleClass) {
    return moduleClass.cast(hwModules().getModule(moduleClass).orElse(null));
  }

  public static @NotNull CraftingManager hwCraftings() {
    return HegeWorldPlugin.GetCraftingManager();
  }

  public static void hwAddRecipe(@NotNull NamespacedKey key, @NotNull HWRecipe recipe) {
    hwCraftings().addRecipe(key, recipe);
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
    itemName = itemName.replace(' ', '_');
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
