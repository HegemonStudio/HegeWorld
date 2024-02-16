package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.highlight.BlockHighlightModule;
import com.hegemonstudio.hegeworld.api.persistent.HwPersistentDataType;
import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.module.HWModuleManager;
import com.hegemonstudio.hegeworld.modules.bulding.BuildingModule;
import com.hegemonstudio.hegeworld.modules.crafting.CraftingModule;
import com.hegemonstudio.hegeworld.modules.general.HegeWorldModule;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollectionModule;
import com.hegemonstudio.hegeworld.modules.guns.GunModule;
import com.hegemonstudio.hegeworld.modules.raids.RaidModule;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public final class HegeWorldPlugin extends JavaPlugin {

  private static HegeWorldPlugin instance;
  private static World mainWorld;
  private final transient Logger logger = getSLF4JLogger();
  private transient File playersDataFile;
  private transient File worldsDataFile;
  private transient YamlConfiguration playersData;
  private transient YamlConfiguration worldsData;
  @Getter
  private transient HWModuleManager moduleManager;
  @Getter
  private transient CraftingManager craftingManager;

  public static @NotNull HegeWorldPlugin GetInstance() {
    return instance;
  }

  public static @NotNull World GetMainWorld() {
    return mainWorld;
  }

  public static @NotNull HWModuleManager GetModuleManager() {
    return HegeWorldPlugin.instance.moduleManager;
  }

  public static @NotNull CraftingManager GetCraftingManager() {
    return HegeWorldPlugin.instance.craftingManager;
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

  public @NotNull YamlConfiguration getPlayersData() {
    return playersData;
  }

  public @NotNull YamlConfiguration getWorldsData() {
    return worldsData;
  }

  @Override
  public void onDisable() {
    long start = System.currentTimeMillis();
    savePlayersData();
    saveWorldsData();
    long end = System.currentTimeMillis() - start;
    logger.info("Disabled {} in {}ms", getName(), end);
  }

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    instance = this;
    mainWorld = Bukkit.getWorlds().get(0);
    createYAMLFiles();
    craftingManager = new CraftingManager();
    moduleManager = new HWModuleManager();
    loadModules();
    afterLoad();
    long end = System.currentTimeMillis() - start;
    HWLogger.Log(Component.text("Enabled " + this + " in " + end + "ms").color(NamedTextColor.GREEN));
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

  private void loadModules() {
    moduleManager.addModule(new GroundCollectionModule());
    moduleManager.addModule(new BlockHighlightModule());
    moduleManager.addModule(new BuildingModule());
    moduleManager.addModule(new RaidModule());
    moduleManager.addModule(new GunModule());

    moduleManager.addModule(new HegeWorldModule());
    moduleManager.addModule(new CraftingModule());
  }

  private void afterLoad() {
    if (Bukkit.getAllowEnd()) {
      HWLogger.Err(Component.text("PLEASE DISABLE END!"));
    }
    if (Bukkit.getAllowNether()) {
      HWLogger.Err(Component.text("PLEASE DISABLE NETHER!"));
    }
  }

  private @NotNull Pair<File, YamlConfiguration> createYAMLFile(@NotNull String fileName) throws IOException, InvalidConfigurationException {
    return createYAMLFile(fileName, null);
  }

  private @NotNull Pair<File, YamlConfiguration> createYAMLFile(@NotNull String fileName, @Nullable String defaultResource) throws IOException, InvalidConfigurationException {
    if (!fileName.endsWith(".yml")) fileName = fileName + ".yml";
    File file = new File(getDataFolder(), fileName);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      if (defaultResource == null)
        file.createNewFile();
      else
        saveResource(defaultResource, true);
    }
    YamlConfiguration config = new YamlConfiguration();
    config.load(file);
    return Pair.of(file, config);
  }

  private boolean saveYAML(@NotNull Pair<File, YamlConfiguration> yamlFile) {
    File file = yamlFile.getLeft();
    YamlConfiguration config = yamlFile.getRight();
    try {
      config.save(file);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean saveYAML(File file, YamlConfiguration config) {
    return saveYAML(Pair.of(file, config));
  }

  public boolean savePlayersData() {
    return saveYAML(playersDataFile, playersData);
  }

  public boolean saveWorldsData() {
    return saveYAML(worldsDataFile, worldsData);
  }

}
