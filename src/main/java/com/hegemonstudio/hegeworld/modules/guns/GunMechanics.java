package com.hegemonstudio.hegeworld.modules.guns;

import com.hegemonstudio.hegeworld.api.effect.HWEffect;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class GunMechanics {

  public static void Shoot(@NotNull Player player) {
    World world = player.getWorld();

    Location start = player.getEyeLocation();//.subtract(0, 0.6f, 0);
    Vector dir = player.getLocation().getDirection().normalize();


    RayTraceResult result = world.rayTrace(start, dir, 40, FluidCollisionMode.NEVER, true, 0.1, (e) -> !e.equals(player) && e instanceof LivingEntity);
    Location hit = start;
    if (result == null) {
      hit = start.clone().add(dir.multiply(40));
      //return; // SHOT IN AIR
    } else {
      hit = result.getHitPosition().toLocation(world);
    }


    world.playSound(start, Sound.ENTITY_IRON_GOLEM_REPAIR,1,2);
//    world.playSound(hit, Sound.ENTITY_IRON_GOLEM_REPAIR,1,2);

    if (result != null) {
      Entity hitEntity = result.getHitEntity();
      if (hitEntity != null) {
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1,2);
        if (hitEntity instanceof Player) {
          Player hitPlayer = (Player) hitEntity;
          hitPlayer.playSound(hitPlayer.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 0.6f, 2);
        }
        LivingEntity livingEntity = (LivingEntity) hitEntity;
        livingEntity.damage(1, player);
      }
    }

    HWEffect.DrawLineParticle(start.clone().subtract(0, 0.6f, 0), hit, Particle.SMOKE_NORMAL, 0.8f);
    HWEffect.DrawLineParticle(start.clone().subtract(0, 0.6f, 0), hit, Particle.ASH, 0.8f);
//    HWEffect.DrawLineParticle(start.clone().subtract(0, 0.6f, 0), hit, Particle.ELECTRIC_SPARK, 0.1f);

    Vector velocity = player.getVelocity().clone();
    player.teleport(new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), (float) (player.getYaw() + (((Math.random() * 2) - 1) * 2)), player.getPitch() - 1));
    player.setVelocity(velocity);
  }

}
