package com.hegemonstudio.hegeworld.modules.bulding;

import com.hegemonstudio.hegeworld.module.HWModule;

import static com.hegemonstudio.hegeworld.HegeWorld.hwRegisterItem;

public class BuildingModule extends HWModule {

  public void onEnable() {
    hwRegisterItem("foundation", new FoundationPlacerItem());
  }

}
