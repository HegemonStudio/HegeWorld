package com.hegemonstudio.hegeworld.api.module;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager<T extends ModuleBase> implements ModuleManagerBase<T> {

  private final Map<Class<? extends ModuleBase>, T> modules;

  public ModuleManager() {
    modules = new ConcurrentHashMap<>();
  }

  @Override
  public void addModule(@NotNull T module) {
    modules.put(module.getClass(), module);
    module.setEnabled(true);
  }

  @Override
  public void removeModule(@NotNull Class<? extends T> moduleClass) {
    getModule(moduleClass).ifPresent(module -> module.setEnabled(false));
    modules.remove(moduleClass);
  }

  @Override
  public boolean hasModule(@NotNull Class<? extends T> moduleClass) {
    return modules.containsKey(moduleClass);
  }

  @Override
  public @NotNull Optional<T> getModule(@NotNull Class<? extends T> moduleClass) {
    return Optional.ofNullable(modules.get(moduleClass));
  }

  @Override
  public @NotNull Optional<T> getModule(@NotNull String classPath) throws ClassNotFoundException {
    Class<?> clazz = Class.forName(classPath);
    return getModule((Class<? extends T>) clazz);
  }

  @Override
  public @NotNull Collection<T> getModules() {
    return modules.values();
  }

  @Override
  public void setModuleEnable(@NotNull Class<? extends T> moduleClass, boolean enabled) {
    getModule(moduleClass).ifPresent((module) -> {
      if (module.isEnabled() == enabled) return;
      module.setEnabled(enabled);
    });
  }

  @Override
  public void enableModule(@NotNull Class<? extends T> moduleClass) {
    setModuleEnable(moduleClass, true);
  }

  @Override
  public void disableModule(@NotNull Class<? extends T> moduleClass) {
    setModuleEnable(moduleClass, false);
  }
}
