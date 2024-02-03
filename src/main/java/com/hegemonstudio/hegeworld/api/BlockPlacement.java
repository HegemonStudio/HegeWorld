package com.hegemonstudio.hegeworld.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BlockPlacement {

  private BlockPlacement() {

  }

  public static void PlacePlatform(@NotNull Location center, @NotNull Material material, @NotNull Dimensions<Integer> size, Consumer<Block> onPlace) {
    Location location = center.clone().subtract(size.getX()/2, size.getY(), size.getZ()/2);
    for (int x = 0; x < size.getX(); x += 1) {
      for (int z = 0; z < size.getZ(); z += 1) {
        for (int y = 0; y < size.getY(); y += 1) {
          Block block = location.clone().add(x, y, z).getBlock();
          block.setType(material);
          onPlace.accept(block);
        }
      }
    }
  }

  public static boolean CheckPlatform(@NotNull Location center, @NotNull Dimensions<Integer> size, @NotNull Function<Block, Boolean> blockCheck) {
    Location location = center.clone().subtract(size.getX()/2, size.getY(), size.getZ()/2);
    for (int x = 0; x < size.getX(); x += 1) {
      for (int z = 0; z < size.getZ(); z += 1) {
        for (int y = 0; y < size.getY(); y += 1) {
          Block block = location.clone().add(x, -y, z).getBlock();
          if (!blockCheck.apply(block)) return false;
        }
      }
    }
    return true;
  }

}
