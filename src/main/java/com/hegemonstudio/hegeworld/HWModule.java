package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.module.ModuleBase;
import com.impact.lib.Impact;
import com.impact.lib.api.command.MCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Listener;

import java.util.Optional;

public abstract class HWModule implements ModuleBase {

  protected HegeWorldPlugin plugin = HegeWorldPlugin.GetInstance();
  protected HWModuleManager moduleManager = HegeWorldPlugin.GetModuleManager();
  private boolean enabled = false;

  protected Optional<HWModule> getModule(Class<? extends HWModule> moduleClass) {
    return moduleManager.getModule(moduleClass);
  }

  @Override
  public String getModuleName() {
    return getClass().getSimpleName();
  }

  @Override
  public abstract void onEnable();

  @Override
  public void onDisable() {

  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    // TODO detect registry changed
    // ExampleModule: Registered x tasks, y items, z blocks.
    this.enabled = enabled;
    if (enabled) {
      HWLogger.Log(Component.text("Enabling module '" + getModuleName() + "'"));
      try {
        onEnable();
      } catch (Exception exception) {
        exception.printStackTrace();
        HWLogger.Err(Component.text("Cannot enable module '" + getModuleName() + "'"));
        setEnabled(false);
      }
      return;
    }
    HWLogger.Log(Component.text("Disabling module '" + getModuleName() + "'"));
    try {
      onDisable();
    } catch (Exception exception) {
      exception.printStackTrace();
      HWLogger.Err(Component.text("Error while disabling module '" + getModuleName() + "'"));
    }
  }

  protected void registerCommand(MCommand<?> command) {
    Impact.registerCommand(HegeWorldPlugin.CreateKey(command.getLabel()), command);
  }

  protected void registerListener(Listener listener) {
    Impact.addListener(listener, plugin);
  }

}
