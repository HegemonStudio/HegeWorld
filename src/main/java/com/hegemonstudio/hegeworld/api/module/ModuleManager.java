package com.hegemonstudio.hegeworld.api.module;

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
  public void addModule(T module) {
    modules.put(module.getClass(), module);
    module.setEnabled(true);
  }

  @Override
  public void removeModule(Class<? extends T> moduleClass) {
    getModule(moduleClass).ifPresent(module -> module.setEnabled(false));
    modules.remove(moduleClass);
  }

  @Override
  public boolean hasModule(Class<? extends T> moduleClass) {
    return modules.containsKey(moduleClass);
  }

  @Override
  public Optional<T> getModule(Class<? extends T> moduleClass) {
    return Optional.ofNullable(modules.get(moduleClass));
  }

  @Override
  public Collection<T> getModules() {
    return modules.values();
  }

  @Override
  public void setModuleEnable(Class<? extends T> moduleClass, boolean enabled) {
    getModule(moduleClass).ifPresent((module) -> {
      if (module.isEnabled() == enabled) return;
      module.setEnabled(enabled);
    });
  }

  @Override
  public void enableModule(Class<? extends T> moduleClass) {
    setModuleEnable(moduleClass, true);
  }

  @Override
  public void disableModule(Class<? extends T> moduleClass) {
    setModuleEnable(moduleClass, false);
  }
}
