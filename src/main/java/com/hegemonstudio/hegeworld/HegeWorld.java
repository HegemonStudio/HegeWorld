package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.module.HWModuleManager;
import com.impact.lib.api.item.CustomItem;
import com.impact.lib.api.registry.ImpactRegistries;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

  @Contract("_ -> new")
  public static @NotNull NamespacedKey hwKey(@NotNull String value) {
    return HegeWorldPlugin.CreateKey(value);
  }

  public static @NotNull HWModuleManager hwModules() {
    return HegeWorldPlugin.GetModuleManager();
  }

  public static @NotNull CraftingManager hwCraftings() {
    return HegeWorldPlugin.GetCraftingManager();
  }

  public static @NotNull World hwWorld() {
    return HegeWorldPlugin.GetMainWorld();
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

  public static @NotNull List<Player> hwPlayersBySelector(@NotNull String selector, @Nullable Player context) {
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

  public static @NotNull List<String> hwPlayerSelectors(@Nullable Player context) {
    List<String> strings = new ArrayList<>();
    strings.add("@p");
    strings.add("@a");
    strings.add("@r");
    strings.addAll(Bukkit.getOnlinePlayers()
        .stream()
        .filter((player -> context == null || context.canSee(player)))
        .map(Player::getName)
        .collect(Collectors.toCollection(ArrayList::new))
    );
    return strings;
  }

  public static @NotNull List<String> hwItemSelectors() {
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

}
