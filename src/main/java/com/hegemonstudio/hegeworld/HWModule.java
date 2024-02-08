package com.hegemonstudio.hegeworld;

import com.hegemonstudio.hegeworld.api.HWLogger;
import com.hegemonstudio.hegeworld.api.module.ModuleBase;
import com.impact.lib.Impact;
import com.impact.lib.api.command.MCommand;
import com.impact.lib.api.registry.ImpactRegistries;
import com.impact.lib.api.registry.ImpactRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class HWModule implements ModuleBase {

  protected HegeWorldPlugin plugin = HegeWorldPlugin.GetInstance();
  protected HWModuleManager moduleManager = HegeWorldPlugin.GetModuleManager();
  private boolean enabled = false;

  protected Optional<HWModule> getModule(Class<? extends HWModule> moduleClass) {
    return moduleManager.getModule(moduleClass);
  }

  protected @NotNull Optional<HWModule> getModule(@NotNull String classPath) throws ClassNotFoundException {
    return moduleManager.getModule(classPath);
  }

  protected @NotNull Optional<HWModule> getModuleByName(@NotNull String moduleName) {
    return moduleManager.getModuleByName(moduleName);
  }

  protected void assertModule(@NotNull Class<? extends HWModule> moduleClass, @Nullable String message) {
    if (!moduleManager.hasModule(moduleClass) && moduleManager.getModule(moduleClass).orElseThrow().isEnabled())
      throw new AssertionError(message == null ? "Module " + moduleClass.getSimpleName() + " is not loaded or enabled!" : message);
  }

  protected void assertModule(@NotNull Class<? extends HWModule> moduleClass) {
    assertModule(moduleClass, null);
  }

  protected void assertModuleByName(@NotNull String moduleName) {
    Optional<HWModule> moduleOptional = getModuleByName(moduleName);
    if (moduleOptional.isEmpty() || !moduleOptional.get().isEnabled())
      throw new AssertionError("Module " + moduleName + " is not loaded or enabled!");
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
  public boolean isDisabled() {
    return !enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    ImpactRegistry<?>[] registries = {ImpactRegistries.COMMAND, ImpactRegistries.GUI, ImpactRegistries.CUSTOM_ITEM, ImpactRegistries.CUSTOM_BLOCK};
    Map<ImpactRegistry<?>, Integer> registryChanges = new HashMap<>();
    for (ImpactRegistry<?> registry : registries) {
      registryChanges.put(registry, registry.getAll().size());
    }
    this.enabled = enabled;
    if (enabled) {
      HWLogger.Log(Component.text("Enabling module '" + getModuleName() + "'"));
      try {
        onEnable();
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text("Registered"));
        for (ImpactRegistry<?> registry : registryChanges.keySet()) {
          int registered = registry.getAll().size() -  registryChanges.get(registry);
          if (registered > 0) {
            builder.append(Component.text(" " + registered + " " + registry.getClass().getSimpleName()));
          }
        }
        HWLogger.Log(builder.build());
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
