package com.hegemonstudio.hegeworld.crafting;

import com.hegemonstudio.hegeworld.api.util.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class HWRecipe {

  @Getter
  private List<ItemStack> ingredients;

  @Setter
  private ItemStack result;

  @Setter
  private String translatableName;

  private final Set<CraftingSource> craftingSources = new HashSet<>();

  public HWRecipe() {
    ingredients = new ArrayList<>();
    result = new ItemStack(Material.AIR);
  }

  public HWRecipe(@NotNull List<ItemStack> ingredients, @NotNull ItemStack result) {
    this.ingredients = ingredients;
    this.result = result;
  }

  public void setIngredients(@NotNull ItemStack... ingredients) {
    this.ingredients = Arrays.stream(ingredients).collect(Collectors.toList());
  }

  public void setIngredients(@NotNull List<ItemStack> ingredients) {
    this.ingredients = ingredients;
  }

  public void allowCraft(CraftingSource @NotNull ... sources) {
    for (CraftingSource source : sources) {
      craftingSources.add(source);
    }
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
   * @return New instance of item stack.
   */
  public @NotNull ItemStack getResult() {
    return this.result.clone();
  }

  @Override
  public @NotNull String toString() {
    return "HWRecipe{" + ChatUtil.Format(result.getType()) + "}";
  }

}
