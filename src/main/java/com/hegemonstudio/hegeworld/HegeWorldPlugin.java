package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.highlight.BlockHighlightModule;
import com.hegemonstudio.hegeworld.general.HegeWorldModule;
import com.hegemonstudio.hegeworld.modules.bulding.BuildingModule;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollectionModule;
import com.hegemonstudio.hegeworld.modules.guns.GunModule;
import com.hegemonstudio.hegeworld.modules.raids.RaidModule;
import com.impact.lib.Impact;
import com.impact.lib.api.command.MCommand;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public final class HegeWorldPlugin extends JavaPlugin {

  private static HegeWorldPlugin instance;
  private static World mainWorld;
  private final Logger logger = getSLF4JLogger();
  private File playersDataFile;
  private File worldsDataFile;
  private FileConfiguration playersData;
  private FileConfiguration worldsData;
  @Getter
  private HWModuleManager moduleManager;

  public static @NotNull HegeWorldPlugin GetInstance() {
    return instance;
  }

  public static @NotNull World GetMainWorld() {
    return mainWorld;
  }

  public static @NotNull HWModuleManager GetModuleManager() {
    return HegeWorldPlugin.instance.moduleManager;
  }

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull MetadataValue CreateMetadata(Object value) {
    return new FixedMetadataValue(instance, value);
  }

  /**
   * Returns {@link NamespacedKey} with namespace of {@link HegeWorldPlugin} and given value.
   *
   * @param value The given value.
   * @return The HegeWorldPlugin NamespacedKey.
   */
  public static @NotNull NamespacedKey CreateKey(@NotNull String value) {
    return new NamespacedKey(HegeWorldPlugin.instance, value);
  }

  public @NotNull FileConfiguration getPlayersData() {
    return playersData;
  }

  public @NotNull FileConfiguration getWorldsData() {
    return worldsData;
  }

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    instance = this;
    mainWorld = Bukkit.getWorlds().get(0);
    createYAMLFiles();
    moduleManager = new HWModuleManager();
    loadModules();
    afterLoad();
    long end = System.currentTimeMillis() - start;
    HWLogger.Log(Component.text("Enabled " + this + " in " + end + "ms").color(NamedTextColor.GREEN));
  }

  private void loadModules() {
    moduleManager.addModule(new GroundCollectionModule());
    moduleManager.addModule(new BlockHighlightModule());
    moduleManager.addModule(new BuildingModule());
    moduleManager.addModule(new RaidModule());
    moduleManager.addModule(new GunModule());

    moduleManager.addModule(new HegeWorldModule());
  }

  @Override
  public void onDisable() {
    long start = System.currentTimeMillis();
    savePlayersData();
    saveWorldsData();
    long end = System.currentTimeMillis() - start;
    logger.info("Disabled {} in {}ms", getName(), end);
  }

  private void afterLoad() {
    if (Bukkit.getAllowEnd()) {
      HWLogger.Err(Component.text("PLEASE DISABLE END!"));
    }
    if (Bukkit.getAllowNether()) {
      HWLogger.Err(Component.text("PLEASE DISABLE NETHER!"));
    }
  }

  private void createYAMLFiles() {
    try {
      // players
      Pair<File, YamlConfiguration> players = createYAMLFile("players_data.yml");
      playersDataFile = players.getLeft();
      playersData = players.getRight();
      // worlds
      Pair<File, YamlConfiguration> worlds = createYAMLFile("worlds_data.yml");
      worldsDataFile = worlds.getLeft();
      worldsData = worlds.getRight();
    } catch (IOException | InvalidConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  private @NotNull Pair<File, YamlConfiguration> createYAMLFile(@NotNull String fileName) throws IOException, InvalidConfigurationException {
    if (!fileName.endsWith(".yml")) fileName = fileName + ".yml";
    File file = new File(getDataFolder(), fileName);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    YamlConfiguration config = new YamlConfiguration();
    config.load(file);
    return Pair.of(file, config);
  }

  public boolean savePlayersData() {
    try {
      playersData.save(playersDataFile);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public boolean saveWorldsData() {
    try {
      worldsData.save(worldsDataFile);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public void registerCommand(@NotNull MCommand<?> command) {
    Impact.registerCommand(new NamespacedKey(this, command.getLabel()), command);
  }

  public void registerListener(@NotNull Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
