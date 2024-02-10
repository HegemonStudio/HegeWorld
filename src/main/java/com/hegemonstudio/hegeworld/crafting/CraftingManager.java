package com.hegemonstudio.hegeworld.crafting;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CraftingManager {

  private final transient Map<NamespacedKey, HWRecipe> recipes = new ConcurrentHashMap<>();

  public void addRecipe(@NotNull NamespacedKey key, @NotNull HWRecipe recipe) {
    recipe.setRecipeId(key);
    recipes.put(key, recipe);
  }

  public void removeRecipe(@NotNull NamespacedKey key) {
    recipes.remove(key);
  }

  public @NotNull Optional<HWRecipe> getRecipe(@NotNull NamespacedKey recipeKey) {
    return Optional.ofNullable(recipes.get(recipeKey));
  }

  public @NotNull Optional<HWRecipe> getRecipe(@NotNull String recipeKey) {
    NamespacedKey key = NamespacedKey.fromString(recipeKey);
    if (key == null) return Optional.empty();
    return Optional.ofNullable(recipes.get(key));
  }

  public @NotNull Optional<HWRecipe> getRecipe(@NotNull NamespacedKey recipeKey, @NotNull CraftingSource source) {
    Optional<HWRecipe> recipe = getRecipe(recipeKey);
    if (recipe.isEmpty()) return recipe;
    if (recipe.get().canCraft(source)) return recipe;
    return Optional.empty();
  }

  public @NotNull Optional<HWRecipe> getRecipe(@NotNull String recipeKey, @NotNull CraftingSource source) {
    Optional<HWRecipe> recipe = getRecipe(recipeKey);
    if (recipe.isEmpty()) return recipe;
    if (recipe.get().canCraft(source)) return recipe;
    return Optional.empty();
  }

  public @NotNull Collection<HWRecipe> getAll() {
    return recipes.values();
  }

  public @NotNull Collection<HWRecipe> getAllBySources(CraftingSource... sources) {
    Collection<HWRecipe> collection = new HashSet<>();
    for (HWRecipe recipe : getAll()) {
      for (CraftingSource source : sources) {
        if (recipe.canCraft(source)) {
          collection.add(recipe);
          break;
        }
      }
    }
    return collection;
  }

}
