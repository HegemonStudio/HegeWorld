package com.hegemonstudio.hegeworld.api.module;

public interface ModuleBase {

  String getModuleName();

  void onEnable();

  void onDisable();

  boolean isEnabled();
  boolean isDisabled();

  void setEnabled(boolean enabled);

}
