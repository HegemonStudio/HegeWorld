package com.hegemonstudio.hegeworld.modules.general;

import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.modules.general.commands.*;
import com.hegemonstudio.hegeworld.modules.general.listeners.PlayerBlockListener;
import com.hegemonstudio.hegeworld.modules.general.listeners.PlayerDeathListener;
import com.hegemonstudio.hegeworld.modules.general.listeners.PlayerJoinListener;

public final class HegeWorldModule extends HWModule {
  @Override
  public void onEnable() {
    registerListener(new PlayerJoinListener());
    registerListener(new PlayerDeathListener());
    registerListener(new PlayerBlockListener());
    registerListener(new PlayerDeathListener());

    registerCommand(new ChunkInfoCommand());
    registerCommand(new HWDebugCommand());
    registerCommand(new CreateCommand());
    registerCommand(new TPWCommand());
    registerCommand(new SpawnItemCommand());

    registerCommand(new GiveCommand());
    // TODO tp
    // TODO gamemode
    // TODO debugmode
    // TODO registries
    // TODO module
  }
}
