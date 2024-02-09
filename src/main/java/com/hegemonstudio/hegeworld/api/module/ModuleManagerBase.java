package com.hegemonstudio.hegeworld.api.module;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ModuleManagerBase<T extends ModuleBase> {

  void addModule(@NotNull T module);

  void removeModule(@NotNull Class<? extends T> moduleClass);

  boolean hasModule(@NotNull Class<? extends T> moduleClass);

  boolean isModuleEnabled(@NotNull T module);

  boolean isModuleEnabled(@NotNull Class<? extends T> moduleClass);

  boolean isModuleEnabled(@NotNull String classPath) throws ClassNotFoundException;

  boolean isModuleByNameEnabled(@NotNull String moduleName);

  @NotNull Optional<T> getModule(@NotNull Class<? extends T> moduleClass);

  @NotNull Optional<T> getModule(@NotNull String classPath) throws ClassNotFoundException;

  @NotNull Optional<T> getModuleByName(@NotNull String moduleName);

  @NotNull Collection<T> getModules();

  void setModuleEnable(@NotNull Class<? extends T> moduleClass, boolean enabled);

  void enableModule(@NotNull Class<? extends T> moduleClass);

  void disableModule(@NotNull Class<? extends T> moduleClass);

}
