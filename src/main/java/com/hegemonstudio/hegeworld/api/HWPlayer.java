package com.hegemonstudio.hegeworld.api;

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

  private @NotNull String makeDataPath(String path) {
    return "data." + getPlayer().getUniqueId() + "." + path;
  }

  public void setData(String path, Object value) {
    HegeWorldAPIPlugin.getInstance().getPlayersData().set(makeDataPath(path), value);
    HegeWorldAPIPlugin.getInstance().savePlayersData();
  }

  public boolean isDataSet(String path) {
    return HegeWorldAPIPlugin.getInstance().getPlayersData().isSet(makeDataPath(path));
  }

  public Location getDataLocation(String path) {
    return HegeWorldAPIPlugin.getInstance().getPlayersData().getLocation(makeDataPath(path));
  }

  public String getDataString(String path) {
    return HegeWorldAPIPlugin.getInstance().getPlayersData().getString(makeDataPath(path));
  }

  public ItemStack getDataItemStack(String path) {
    return HegeWorldAPIPlugin.getInstance().getPlayersData().getItemStack(makeDataPath(path));
  }

  public int getDataInt(String path) {
    return HegeWorldAPIPlugin.getInstance().getPlayersData().getInt(makeDataPath(path));
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   * @apiNote In general, the
   * {@code toString} method returns a string that
   * "textually represents" this object. The result should
   * be a concise but informative representation that is easy for a
   * person to read.
   * It is recommended that all subclasses override this method.
   * The string output is not necessarily stable over time or across
   * JVM invocations.
   * @implSpec The {@code toString} method for class {@code Object}
   * returns a string consisting of the name of the class of which the
   * object is an instance, the at-sign character `{@code @}', and
   * the unsigned hexadecimal representation of the hash code of the
   * object. In other words, this method returns a string equal to the
   * value of:
   * <blockquote>
   * <pre>
   * getClass().getName() + '@' + Integer.toHexString(hashCode())
   * </pre></blockquote>
   */
  @Override
  public String toString() {
    return "HWPlayer{" + getPlayer().getName() + "}";
  }
}
