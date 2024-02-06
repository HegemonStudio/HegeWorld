package com.hegemonstudio.hegeworld.api.module;

import java.util.Collection;
import java.util.Optional;

public interface ModuleManagerBase<T extends ModuleBase> {

  void addModule(T module);

  void removeModule(Class<? extends T> moduleClass);

  boolean hasModule(Class<? extends T> moduleClass);

  Optional<T> getModule(Class<? extends T> moduleClass);

  Collection<T> getModules();

  void setModuleEnable(Class<? extends T> moduleClass, boolean enabled);

  void enableModule(Class<? extends T> moduleClass);

  void disableModule(Class<? extends T> moduleClass);


}
