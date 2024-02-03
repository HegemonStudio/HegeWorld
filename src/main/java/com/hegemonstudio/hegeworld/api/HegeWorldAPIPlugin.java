package com.hegemonstudio.hegeworld.api;

import com.hegemonstudio.hegeworld.api.collection.GroundCollection;
import com.hegemonstudio.hegeworld.api.collection.GroundCollectionListener;
import com.hegemonstudio.hegeworld.api.commands.ChunkInfoCommand;
import com.hegemonstudio.hegeworld.api.commands.HWDebugCommand;
import com.hegemonstudio.hegeworld.api.guns.AK47Gun;
import com.hegemonstudio.hegeworld.api.highlight.BlockHighlight;
import com.hegemonstudio.hegeworld.api.listeners.FunListener;
import com.hegemonstudio.hegeworld.api.listeners.PlayerBlockListener;
import com.hegemonstudio.hegeworld.api.listeners.PlayerDeathListener;
import com.hegemonstudio.hegeworld.api.listeners.PlayerJoinListener;
import com.impact.lib.Impact;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public final class HegeWorldAPIPlugin extends JavaPlugin {

  private static HegeWorldAPIPlugin instance;

  public static HegeWorldAPIPlugin getInstance() {
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
    Impact.registerCommand(new NamespacedKey(this, "hwchunk"), new ChunkInfoCommand());
    Impact.registerCommand(new NamespacedKey(this, HWDebugCommand.LABEL), new HWDebugCommand());
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
}
