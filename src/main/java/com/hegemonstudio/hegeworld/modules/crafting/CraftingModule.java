package com.hegemonstudio.hegeworld.modules.crafting;

import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.modules.crafting.commands.CraftCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static com.hegemonstudio.hegeworld.HegeWorld.hwAddRecipe;
import static com.hegemonstudio.hegeworld.HegeWorld.hwGetItem;

/**
 * HegeWorld module that adds crafting functionality
 *
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
    //// INVENTORY

    /// Equipment
    // TODO spear

    /// Structure
    // TODO campfire

    // Flint Axe
    HWRecipe flintAxe = new HWRecipe();
    flintAxe.setIngredients(new ItemStack(Material.FLINT, 3), new ItemStack(Material.STICK, 2));
    flintAxe.setResult(new ItemStack(Material.STONE_AXE));
    flintAxe.allowCraft(CraftingSource.INVENTORY);

    hwAddRecipe("flint_axe", flintAxe);

    // Stone Pickaxe
    HWRecipe stonePickaxe = new HWRecipe();
    stonePickaxe.setIngredients(new ItemStack(Material.STONE_BUTTON, 5), new ItemStack(Material.STICK, 2));
    stonePickaxe.setResult(new ItemStack(Material.STONE_PICKAXE));
    stonePickaxe.allowCraft(CraftingSource.INVENTORY);

    hwAddRecipe("stone_pickaxe", stonePickaxe);

    //// WORKBENCH

    /// Materials
    // TODO planks
    // TODO concrete

    /// Equipment
    // TODO building hammer
    // TODO bow
    // TODO iron pickaxe
    // TODO iron axe
    // TODO backpack?

    /// Structure
    // TODO chest
    // TODO totem of authority

    HWRecipe blastFurnace = new HWRecipe();
    blastFurnace.setIngredients(hwGetItem("clay", 50), hwGetItem("stone", 10));
    blastFurnace.setResult(hwGetItem("blast furnace"));
    blastFurnace.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("blast_furnace", blastFurnace);
  }

}
