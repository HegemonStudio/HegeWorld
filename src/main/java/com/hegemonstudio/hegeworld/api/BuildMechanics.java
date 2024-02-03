package com.hegemonstudio.hegeworld.api;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BuildMechanics {

  private BuildMechanics() {}

  // Not Minecraft generated
  public static final String GENERATED_KEY = "generated";

  // Multi blocks
  public static final String MULTIBLOCK_KEY = "multiblock";

  // Building
  public static final String STRUCTURE_TYPE_KEY = "struct";

  //<editor-fold> Generated Block
  public static boolean IsGenerated(@NotNull Block block) {
    return block.hasMetadata(GENERATED_KEY);
  }

  public static @NotNull Block SelectAsGenerated(@NotNull Block block) {
    if (IsGenerated(block)) return block;
    block.setMetadata(GENERATED_KEY, HegeWorldPlugin.CreateMetadata(true));
    return block;
  }

  public static @NotNull Block DeselectAsGenerated(@NotNull Block block) {
    if (!IsGenerated(block)) return block;
    block.removeMetadata(GENERATED_KEY, HegeWorldPlugin.getInstance());
    return block;
  }
  //</editor-fold>

}
