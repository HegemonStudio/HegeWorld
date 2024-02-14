package com.hegemonstudio.hegeworld.api;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.impact.lib.api.player.WrappedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HWPlayer extends WrappedPlayer {

  private static final ConcurrentHashMap<UUID, HWPlayer> PLAYERS = new ConcurrentHashMap<>();

  public HWPlayer(Player player) {
    super(player);
  }

  @Contract("_ -> new")
  public static @NotNull HWPlayer of(@NotNull Player player) {
    if (PLAYERS.containsKey(player.getUniqueId())) {
      return PLAYERS.get(player.getUniqueId());
    }
    HWPlayer hwPlayer = new HWPlayer(player);
    PLAYERS.put(player.getUniqueId(), hwPlayer);
    return hwPlayer;
  }

  public static Optional<HWPlayer> of(@Nullable String playerName) {
    if (playerName == null) return Optional.empty();
    Player found = Bukkit.getPlayer(playerName);
    if (found == null) return Optional.empty();
    if (PLAYERS.containsKey(found.getUniqueId())) {
      return Optional.ofNullable(PLAYERS.get(found.getUniqueId()));
    }
    HWPlayer player = new HWPlayer(found);
    PLAYERS.put(player.getUniqueId(), player);
    return Optional.of(player);
  }

  public void giveItem(@NotNull ItemStack item) {
    Player player = getPlayer();
    if (player.getInventory().firstEmpty() == -1) {
      player.getLocation().getWorld().dropItem(player.getLocation(), item);
      return;
    }
    player.getInventory().addItem(item);
  }

  public void setData(String path, Object value) {
    HegeWorldPlugin.GetInstance().getPlayersData().set(makeDataPath(path), value);
    HegeWorldPlugin.GetInstance().savePlayersData();
  }

  private @NotNull String makeDataPath(String path) {
    return "data." + getPlayer().getUniqueId() + "." + path;
  }

  public boolean isDataSet(String path) {
    return HegeWorldPlugin.GetInstance().getPlayersData().isSet(makeDataPath(path));
  }

  public Location getDataLocation(String path) {
    return HegeWorldPlugin.GetInstance().getPlayersData().getLocation(makeDataPath(path));
  }

  public String getDataString(String path) {
    return HegeWorldPlugin.GetInstance().getPlayersData().getString(makeDataPath(path));
  }

  public ItemStack getDataItemStack(String path) {
    return HegeWorldPlugin.GetInstance().getPlayersData().getItemStack(makeDataPath(path));
  }

  public int getDataInt(String path) {
    return HegeWorldPlugin.GetInstance().getPlayersData().getInt(makeDataPath(path));
  }

  @Override
  public String toString() {
    return "HWPlayer{" + getPlayer().getName() + "}";
  }
}
