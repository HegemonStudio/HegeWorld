package com.hegemonstudio.hegeworld.crafting;

import com.hegemonstudio.hegeworld.api.util.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HWRecipe {

  @Getter
  @Setter
  private List<ItemStack> ingredients;
  @Getter
  @Setter
  private ItemStack result;

  private Set<CraftingSource> craftingSources = new HashSet<>();

  public HWRecipe() {
    ingredients = new ArrayList<>();
    result = new ItemStack(Material.AIR);
  }

  public HWRecipe(List<ItemStack> ingredients, ItemStack result) {
    this.ingredients = ingredients;
    this.result = result;
  }

  public void allowCraft(CraftingSource source) {
    craftingSources.add(source);
  }

  public void disallowCraft(CraftingSource source) {
    craftingSources.remove(source);
  }

  public boolean canCraft(CraftingSource source) {
    return craftingSources.contains(source);
  }

  @Override
  public @NotNull String toString() {
    return "HWRecipe{"  + ChatUtil.Format(result.getType()) + "}";
  }

}
