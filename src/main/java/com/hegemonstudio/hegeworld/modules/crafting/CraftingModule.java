package com.hegemonstudio.hegeworld.modules.crafting;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.crafting.CraftingManager;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.modules.crafting.commands.CraftCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * HegeWorld module that adds crafting functionality
 * @since 1.0-SNAPSHOT
 */
public class CraftingModule extends HWModule {

  @Override
  public void onEnable() {
    loadRecipes();
    registerCommand(new CraftCommand());
  }

  /**
   * Loads all crafting recipes
   */
  private void loadRecipes() {
    CraftingManager cm = HegeWorldPlugin.GetCraftingManager();

    // Flint Axe
    HWRecipe flintAxe = new HWRecipe();
    flintAxe.setIngredients(new ItemStack(Material.FLINT, 3), new ItemStack(Material.STICK, 2));
    flintAxe.setResult(new ItemStack(Material.STONE_AXE));
    flintAxe.allowCraft(CraftingSource.INVENTORY);

    cm.addRecipe(HegeWorldPlugin.CreateKey("flint_axe"), flintAxe);

    // Stone Pickaxe
    HWRecipe stonePickaxe = new HWRecipe();
    stonePickaxe.setIngredients(new ItemStack(Material.STONE_BUTTON, 5), new ItemStack(Material.STICK, 2));
    stonePickaxe.setResult(new ItemStack(Material.STONE_PICKAXE));
    stonePickaxe.allowCraft(CraftingSource.INVENTORY);

    cm.addRecipe(HegeWorldPlugin.CreateKey("stone_pickaxe"), stonePickaxe);
  }

}
