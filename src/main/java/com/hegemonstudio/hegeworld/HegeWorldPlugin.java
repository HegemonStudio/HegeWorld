package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.highlight.BlockHighlight;
import com.hegemonstudio.hegeworld.commands.*;
import com.hegemonstudio.hegeworld.listeners.PlayerBlockListener;
import com.hegemonstudio.hegeworld.listeners.PlayerDeathListener;
import com.hegemonstudio.hegeworld.listeners.PlayerJoinListener;
import com.hegemonstudio.hegeworld.modules.bulding.BuildingModule;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollectionModule;
import com.hegemonstudio.hegeworld.modules.guns.AK47Gun;
import com.impact.lib.Impact;
import com.impact.lib.api.command.MCommand;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
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

  public static @NotNull HegeWorldPlugin GetInstance() {
    return instance;
  }

  public static @NotNull World GetMainWorld() {
    return mainWorld;
  }

  private final Logger logger = getSLF4JLogger();

  private File playersDataFile;
  private File worldsDataFile;
  private FileConfiguration playersData;
  private FileConfiguration worldsData;

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
    loadAPI();
    loadListeners();
    loadCommands();
    loadModules();
    loadCustomGuis();
    loadCustomBlocks();
    loadCustomItems();
    loadData();
    loadTasks();
    afterLoad();
    long end = System.currentTimeMillis() - start;
    HWLogger.Log(Component.text("Enabled " + this + " in " + end + "ms").color(NamedTextColor.GREEN));
  }

  private void loadModules() {
    new BlockHighlight().start();
    new GroundCollectionModule().start();
    new BuildingModule().start();
  }

  private void loadCommands() {
    registerCommand(new ChunkInfoCommand());
    registerCommand(new HWDebugCommand());
    registerCommand(new CreateCommand());
    registerCommand(new TPWCommand());
    registerCommand(new SpawnItemCommand());
  }

  private void loadListeners() {
    registerListener(new PlayerJoinListener());
    registerListener(new PlayerDeathListener());
    registerListener(new PlayerBlockListener());
    registerListener(new PlayerDeathListener());
  }

  @Override
  public void onDisable() {
    long start = System.currentTimeMillis();
    savePlayersData();
    saveWorldsData();
    long end = System.currentTimeMillis() - start;
    logger.info("Disabled {} in {}ms", getName(), end);
  }

  private void loadCustomItems() {
    NamespacedKey key = new NamespacedKey(this, "ak47");
    AK47Gun.KEY = key;
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, key, new AK47Gun());
  }

  private void loadCustomBlocks() {

  }

  private void loadCustomGuis() {

  }

  private void afterLoad() {
    if (Bukkit.getAllowEnd()) {
      HWLogger.Err(Component.text("PLEASE DISABLE END!"));
    }
    if (Bukkit.getAllowNether()) {
      HWLogger.Err(Component.text("PLEASE DISABLE NETHER!"));
    }
  }

  private void loadTasks() {

  }

  private void loadData() {
    GroundCollection.LoadFrames(Bukkit.getWorld("world"));
  }

  private void loadAPI() {

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

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull MetadataValue CreateMetadata(Object value) {
    return new FixedMetadataValue(instance, value);
  }

  public void registerCommand(@NotNull MCommand<?> command) {
    Impact.registerCommand(new NamespacedKey(this, command.getLabel()), command);
  }

  public void registerListener(@NotNull Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
