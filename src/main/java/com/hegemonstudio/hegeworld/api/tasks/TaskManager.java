package com.hegemonstudio.hegeworld.api.tasks;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class TaskManager {

  private static final Map<String, HWTask> TASKS = new HashMap<>();

  private TaskManager() {

  }

  public static @NotNull String OnTick(@NotNull Runnable onTick) {
    String uid = UUID.randomUUID().toString();
    ListenTick(uid, onTick);
    return uid;
  }

  public static void ListenTick(@NotNull String uid, @NotNull Runnable onTick) {
    ListenTick(uid, new HWTask() {
      @Override
      public void run() {
        onTick.run();
      }
    });
  }

  public static void ListenTick(@NotNull String uid, @NotNull HWTask task) {
    ListenTicks(uid, task, 1L);
  }

  public static void ListenTicks(@NotNull String uid, @NotNull HWTask task, long periodTicks) {
    task.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(HegeWorldPlugin.GetInstance(), task, 0L, periodTicks);
    AddTask(uid, task);
  }

  static void AddTask(@NotNull String uid, HWTask task) {
    TASKS.put(uid, task);
  }

  public static void CancelTask(@Nullable String uid) {
    GetTask(uid).ifPresent((task) -> {
      Bukkit.getScheduler().cancelTask(task.id);
      TASKS.remove(uid);
    });
  }

  public static Optional<HWTask> GetTask(@Nullable String uid) {
    return Optional.ofNullable(TASKS.get(uid));
  }

}
