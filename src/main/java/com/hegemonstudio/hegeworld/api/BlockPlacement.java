package com.hegemonstudio.hegeworld.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BlockPlacement {

  private BlockPlacement() {

  }

  public static void PlacePlatform(@NotNull Location center, @NotNull Material material, int size, Consumer<Block> onPlace) {
    Location location = center.clone().subtract(size/2, 0, size/2);
    for (int x = 0; x < size; x += 1) {
      for (int z = 0; z < size; z += 1) {
        Block block = location.clone().add(x, 0, z).getBlock();
        block.setType(material);
        onPlace.accept(block);
      }
    }
  }

}
