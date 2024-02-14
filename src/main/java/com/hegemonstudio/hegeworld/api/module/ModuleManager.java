package com.hegemonstudio.hegeworld.api.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public boolean isModuleEnabled(@NotNull T module) {
    return module.isEnabled();
  }

  @Override
  public boolean isModuleEnabled(@NotNull Class<? extends T> moduleClass) {
    Optional<T> optionalModule = getModule(moduleClass);
    return optionalModule.filter(this::isModuleEnabled).isPresent();
  }

  @Override
  public boolean isModuleEnabled(@NotNull String classPath) throws ClassNotFoundException {
    Class<? extends T> clazz = (Class<? extends T>) Class.forName(classPath);
    return isModuleEnabled(clazz);
  }

  @Override
  public boolean isModuleByNameEnabled(@NotNull String moduleName) {
    return getModuleByName(moduleName).filter(this::isModuleEnabled).isPresent();
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
  public @NotNull Optional<T> getModuleByName(@NotNull String moduleName) {
    for (T module : modules.values()) {
      if (module.getModuleName().equals(moduleName)) return Optional.of(module);
    }
    return Optional.empty();
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

  public @Nullable T nullableGet(@NotNull Class<? extends T> moduleClass) {
    return modules.get(moduleClass);
  }
}
