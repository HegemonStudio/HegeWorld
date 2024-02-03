package com.hegemonstudio.hegeworld.api.tasks;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TaskManager {

  private static final Map<String, HWTask> TASKS = new HashMap<>();

  static void AddTask(@NotNull String uid, HWTask task) {
    TASKS.put(uid, task);
  }

  public static void AddTickTask(@NotNull String uid, @NotNull HWTask task) {
    task.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(HegeWorldPlugin.getInstance(), task, 0, 1L);
    AddTask(uid, task);
  }

  public static Optional<HWTask> GetTask(@Nullable String uid) {
    return Optional.ofNullable(TASKS.get(uid));
  }

  public static void CancelTask(@Nullable String uid) {
    GetTask(uid).ifPresent((task) -> {
      Bukkit.getScheduler().cancelTask(task.id);
    });
  }

}
