package com.hegemonstudio.hegeworld.api.effect;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HWEffect {

  public static double DEFAULT_STEP = 0.5D;

  public static void DrawLineDust(@NotNull Location start, @NotNull Location end) {
    DrawLineDust(start, end, new Particle.DustOptions(Color.LIME, 1), DEFAULT_STEP);
  }

  public static void DrawLineDust(@NotNull Location start, @NotNull Location end, @NotNull Particle.DustOptions options) {
    DrawLineDust(start, end, options, DEFAULT_STEP);
  }

  public static void DrawLineDust(@NotNull Location start, @NotNull Location end, @NotNull Particle.DustOptions options, double step) {
    assert step > 0;
    World world = start.getWorld();
    assert start.getWorld().equals(end.getWorld());
    double distance = start.distance(end);
    Vector startVec = start.toVector();
    Vector endVec = end.toVector();
    Vector vector = endVec.clone().subtract(startVec).normalize().multiply(step);
    double covered = 0;
    for (; covered < distance; startVec.add(vector)) {
      world.spawnParticle(Particle.REDSTONE, startVec.getX(), startVec.getY(), startVec.getZ(), 1, options);
      covered += step;
    }
  }

  public static void DrawLineParticle(@NotNull Location start, @NotNull Location end, @NotNull Particle particle) {
    DrawLineParticle(start, end, particle, DEFAULT_STEP);
  }

  public static void DrawLineParticle(@NotNull Location start, @NotNull Location end, @NotNull Particle particle, double step) {
    assert step > 0;
    World world = start.getWorld();
    assert start.getWorld().equals(end.getWorld());
    double distance = start.distance(end);
    Vector startVec = start.toVector();
    Vector endVec = end.toVector();
    Vector vector = endVec.clone().subtract(startVec).normalize().multiply(step);
    double covered = 0;
    for (; covered < distance; startVec.add(vector)) {
      world.spawnParticle(particle, startVec.getX(), startVec.getY(), startVec.getZ(), 0, 0, 0, 0.01f);
      covered += step;
    }
  }

  public static void HighlightChunk(@Nullable Chunk chunk, @NotNull Particle particle, double y) {
    HighlightChunk(chunk, particle, y, DEFAULT_STEP);
  }

  public static void HighlightChunk(@Nullable Chunk chunk, @NotNull Particle particle, double y, double space) {
    if (chunk == null) return;
    World world = chunk.getWorld();
    int blockX = 16 * chunk.getX();
    int blockZ = 16 * chunk.getZ();

    DrawLineParticle(new Location(world, blockX, y, blockZ), new Location(world, blockX, y, blockZ + 16), particle, space);
    DrawLineParticle(new Location(world, blockX, y, blockZ), new Location(world, blockX + 16, y, blockZ), particle, space);
    DrawLineParticle(new Location(world, blockX + 16, y, blockZ), new Location(world, blockX + 16, y, blockZ + 16), particle, space);
    DrawLineParticle(new Location(world, blockX, y, blockZ + 16), new Location(world, blockX + 16, y, blockZ + 16), particle, space);

    DrawLineParticle(new Location(world, blockX, y, blockZ), new Location(world, blockX, y + 16, blockZ), particle, space);
    DrawLineParticle(new Location(world, blockX + 16, y, blockZ), new Location(world, blockX + 16, y + 16, blockZ), particle, space);
    DrawLineParticle(new Location(world, blockX + 16, y, blockZ + 16), new Location(world, blockX + 16, y + 16, blockZ + 16), particle, space);
    DrawLineParticle(new Location(world, blockX, y, blockZ + 16), new Location(world, blockX, y + 16, blockZ + 16), particle, space);
  }

  public static void HighlightChunk(@Nullable Chunk chunk, @NotNull Particle.DustOptions dust, double y) {
    HighlightChunk(chunk, dust, y, DEFAULT_STEP);
  }

  public static void HighlightChunk(@Nullable Chunk chunk, @NotNull Particle.DustOptions dust, double y, double space) {
    if (chunk == null) return;
    World world = chunk.getWorld();
    int blockX = 16 * chunk.getX();
    int blockZ = 16 * chunk.getZ();

    DrawLineDust(new Location(world, blockX, y, blockZ), new Location(world, blockX, y, blockZ + 16), dust, space);
    DrawLineDust(new Location(world, blockX, y, blockZ), new Location(world, blockX + 16, y, blockZ), dust, space);
    DrawLineDust(new Location(world, blockX + 16, y, blockZ), new Location(world, blockX + 16, y, blockZ + 16), dust, space);
    DrawLineDust(new Location(world, blockX, y, blockZ + 16), new Location(world, blockX + 16, y, blockZ + 16), dust, space);

    DrawLineDust(new Location(world, blockX, y, blockZ), new Location(world, blockX, y + 16, blockZ), dust, space);
    DrawLineDust(new Location(world, blockX + 16, y, blockZ), new Location(world, blockX + 16, y + 16, blockZ), dust, space);
    DrawLineDust(new Location(world, blockX + 16, y, blockZ + 16), new Location(world, blockX + 16, y + 16, blockZ + 16), dust, space);
    DrawLineDust(new Location(world, blockX, y, blockZ + 16), new Location(world, blockX, y + 16, blockZ + 16), dust, space);
  }

  public static void HighlightRegion(@NotNull Location start, @NotNull Location end, @NotNull Particle particle, double space) {

  }

  public static void HighLightBlock(@NotNull Block block, @NotNull Particle particle, double space) {
    BoundingBox box = block.getBoundingBox();
    Location B = new Location(block.getWorld(), box.getMinX(), box.getMinY(), box.getMinZ());
    Location A = B.clone().add(box.getWidthX(), 0, 0);
    Location C = B.clone().add(0, 0, box.getWidthZ());
    Location D = B.clone().add(box.getWidthX(), 0, box.getWidthZ());
    // Platform
    DrawLineParticle(B, A, particle, space);
    DrawLineParticle(B, C, particle, space);
    DrawLineParticle(A, D, particle, space);
    DrawLineParticle(C, D, particle, space);

    Location B2 = B.clone().add(0, box.getHeight(), 0);
    Location A2 = A.clone().add(0, box.getHeight(), 0);
    Location C2 = C.clone().add(0, box.getHeight(), 0);
    Location D2 = D.clone().add(0, box.getHeight(), 0);

    DrawLineParticle(B2, A2, particle, space);
    DrawLineParticle(B2, C2, particle, space);
    DrawLineParticle(A2, D2, particle, space);
    DrawLineParticle(C2, D2, particle, space);

    DrawLineParticle(A, A2, particle, space);
    DrawLineParticle(B, B2, particle, space);
    DrawLineParticle(C, C2, particle, space);
    DrawLineParticle(D, D2, particle, space);
  }

}
