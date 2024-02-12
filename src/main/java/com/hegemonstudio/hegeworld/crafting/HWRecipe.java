package com.hegemonstudio.hegeworld.crafting;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hegemonstudio.hegeworld.HegeWorld.hwCraftingManager;
import static com.hegemonstudio.hegeworld.HegeWorld.hwStr;

/**
 * HegeWorld Recipe
 */
@Builder(access = AccessLevel.PUBLIC)
public class HWRecipe {


  private final Set<CraftingSource> craftingSources = new HashSet<>();
  private transient NamespacedKey recipeId;
  @Getter
  private List<ItemStack> ingredients;
  @Setter
  private ItemStack result;
  @Setter
  private String translatableName;

  public HWRecipe() {
    ingredients = new ArrayList<>();
    result = new ItemStack(Material.AIR);
  }

  public HWRecipe(@NotNull List<ItemStack> ingredients, @NotNull ItemStack result) {
    this.ingredients = ingredients;
    this.result = result;
  }

  public @NotNull NamespacedKey getRecipeId() {
    return Objects.requireNonNull(recipeId);
  }

  void setRecipeId(@Nullable NamespacedKey recipeId) {
    this.recipeId = recipeId;
  }

  public void setIngredients(@NotNull ItemStack... ingredients) {
    this.ingredients = Arrays.stream(ingredients).collect(Collectors.toList());
  }

  public void setIngredients(@NotNull List<ItemStack> ingredients) {
    this.ingredients = ingredients;
  }

  public void allowCraft(CraftingSource @NotNull ... sources) {
    Collections.addAll(craftingSources, sources);
  }

  public void disallowCraft(CraftingSource @NotNull ... sources) {
    for (CraftingSource source : sources) {
      craftingSources.remove(source);
    }
  }

  public boolean canCraft(@NotNull CraftingSource source) {
    return craftingSources.contains(source);
  }

  public String getTranslatableName() {
    if (translatableName == null) {
      return result.translationKey();
    }
    return translatableName;
  }

  /**
   * The recipe {@link ItemStack} result.
   *
   * @return New instance of item stack.
   */
  public @NotNull ItemStack getResult() {
    return this.result.clone();
  }

  @Override
  public @NotNull String toString() {
    return MessageFormat.format("{0}'{'{1}'}'", isRegistered() ? "RegisteredHwRecipe" : "HwRecipe", hwStr(result));
  }

  public boolean isRegistered() {
    if (recipeId == null) return false;
    return hwCraftingManager().containsRecipe(recipeId);
  }

}
