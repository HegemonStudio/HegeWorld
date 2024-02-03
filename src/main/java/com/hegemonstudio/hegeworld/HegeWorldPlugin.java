package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.highlight.BlockHighlight;
import com.hegemonstudio.hegeworld.commands.*;
import com.hegemonstudio.hegeworld.listeners.FunListener;
import com.hegemonstudio.hegeworld.listeners.PlayerBlockListener;
import com.hegemonstudio.hegeworld.listeners.PlayerDeathListener;
import com.hegemonstudio.hegeworld.listeners.PlayerJoinListener;
import com.hegemonstudio.hegeworld.modules.bulding.BuildingModule;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollection;
import com.hegemonstudio.hegeworld.modules.grounditems.GroundCollectionListener;
import com.hegemonstudio.hegeworld.modules.guns.AK47Gun;
import com.impact.lib.Impact;
import com.impact.lib.api.command.MCommand;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public final class HegeWorldPlugin extends JavaPlugin {

  private static HegeWorldPlugin instance;

  public static HegeWorldPlugin getInstance() {
    return instance;
  }

  private final Logger logger = getSLF4JLogger();

  private File playersDataFile;
  private File worldsDataFile;
  private FileConfiguration playersData;
  private FileConfiguration worldsData;

  public FileConfiguration getPlayersData() {
    return playersData;
  }

  public FileConfiguration getWorldsData() {
    return worldsData;
  }

  @Override
  public void onEnable() {
    instance = this;
    long start = System.currentTimeMillis();
    try {
      createPlayersData();
      createWorldsData();
    } catch (IOException | InvalidConfigurationException e) {
      throw new RuntimeException(e);
    }
    loadListeners();
    loadCommands();

    GroundCollection.LoadFrames(Bukkit.getWorld("world"));

    new BlockHighlight().run();

    NamespacedKey key = new NamespacedKey(this, "ak47");
    AK47Gun.KEY = key;
    ImpactRegistry.register(ImpactRegistries.CUSTOM_ITEM, key, new AK47Gun());

    new BuildingModule().start();

    long end = System.currentTimeMillis() - start;
    HWLogger.Log(Component.text("Enabled " + this + " in " + end + "ms").color(NamedTextColor.GREEN));

    if (Bukkit.getAllowEnd()) {
      HWLogger.Err(Component.text("PLEASE DISABLE END!"));
    }
    if (Bukkit.getAllowNether()) {
      HWLogger.Err(Component.text("PLEASE DISABLE NETHER!"));
    }


  }

  private void loadCommands() {
    registerCommand(new ChunkInfoCommand());
    registerCommand(new HWDebugCommand());
    registerCommand(new CreateCommand());
    registerCommand(new TPWCommand());
    registerCommand(new SpawnItemCommand());
  }

  private void createWorldsData() throws IOException, InvalidConfigurationException {
    worldsDataFile = new File(getDataFolder(), "worlds_data.yml");
    if (!worldsDataFile.exists()) {
      worldsDataFile.getParentFile().mkdirs();
      worldsDataFile.createNewFile();
    }
    worldsData = new YamlConfiguration();
    worldsData.load(worldsDataFile);
  }

  private void createPlayersData() throws IOException, InvalidConfigurationException {
    playersDataFile = new File(getDataFolder(), "players_data.yml");
    if (!playersDataFile.exists()) {
      playersDataFile.getParentFile().mkdirs();
      playersDataFile.createNewFile();
    }
    playersData = new YamlConfiguration();
    playersData.load(playersDataFile);
  }

  public boolean savePlayersData() {
    try {
      playersData.save(playersDataFile);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public void onDisable() {
    long start = System.currentTimeMillis();
    savePlayersData();
    long end = System.currentTimeMillis() - start;
    logger.info("Disabled {} in {}ms", getName(), end);
  }

  private void loadListeners() {
    logger.info("Loading listeners..");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new PlayerJoinListener(), this);
    pm.registerEvents(new PlayerDeathListener(), this);
    pm.registerEvents(new PlayerBlockListener(), this);
    pm.registerEvents(new GroundCollectionListener(), this);
    pm.registerEvents(new FunListener(), this);
    logger.info("Loaded listeners");
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

  private void registerCommand(MCommand<?> command) {
    Impact.registerCommand(new NamespacedKey(this, command.getLabel()), command);
  }
}
