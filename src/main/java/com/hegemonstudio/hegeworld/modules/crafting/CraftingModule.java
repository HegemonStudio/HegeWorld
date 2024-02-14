package com.hegemonstudio.hegeworld.modules.crafting;

import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HwRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import com.hegemonstudio.hegeworld.modules.crafting.commands.CraftCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static com.hegemonstudio.hegeworld.HegeWorld.hwAddRecipe;
import static com.hegemonstudio.hegeworld.HegeWorld.hwItem;

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
    HwRecipe flintAxe = new HwRecipe();
    flintAxe.setIngredients(new ItemStack(Material.FLINT, 3), new ItemStack(Material.STICK, 2));
    flintAxe.setResult(new ItemStack(Material.STONE_AXE));
    flintAxe.allowCraft(CraftingSource.HANDCRAFTING);

    hwAddRecipe("flint_axe", flintAxe);


    // Stone Pickaxe
    HwRecipe stonePickaxe = new HwRecipe();
    stonePickaxe.setIngredients(new ItemStack(Material.STONE_BUTTON, 5), new ItemStack(Material.STICK, 2));
    stonePickaxe.setResult(new ItemStack(Material.STONE_PICKAXE));
    stonePickaxe.allowCraft(CraftingSource.HANDCRAFTING);

    hwAddRecipe("stone_pickaxe", stonePickaxe);


    //Iron Axe
    HwRecipe ironAxe = new HwRecipe();
    ironAxe.setIngredients(hwItem("iron ingot", 4), hwItem("stick", 3));
    ironAxe.setResult(hwItem("iron axe"));
    ironAxe.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("iron_axe", ironAxe);


    //iron Pickaxe
    HwRecipe ironPickaxe = new HwRecipe();
    ironPickaxe.setIngredients(hwItem("iron ingot", 4), hwItem("stick", 3));
    ironPickaxe.setResult(hwItem("iron pickaxe"));
    ironPickaxe.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("iron_pickaxe", ironPickaxe);


    //bow
    HwRecipe bow = new HwRecipe();
    bow.setIngredients(hwItem("stick", 4), hwItem("string", 4));
    bow.setResult(hwItem("bow"));
    bow.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("bow", bow);


    //spear
    HwRecipe spear = new HwRecipe();
    spear.setIngredients(hwItem("stick", 4), hwItem("flint", 3));
    spear.setResult(hwItem("trident"));
    spear.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("spear", spear);


    // WorkBench
    HwRecipe workBench = new HwRecipe();
    workBench.setIngredients(hwItem("oak log", 20));
    workBench.setResult((hwItem("crafting table")));
    workBench.allowCraft(CraftingSource.HANDCRAFTING);

    hwAddRecipe("workbench", workBench);


    //Campfire
    HwRecipe campfire = new HwRecipe();
    campfire.setIngredients(hwItem("oak log", 4), hwItem("coal", 2));
    campfire.setResult(hwItem("campfire"));
    campfire.allowCraft(CraftingSource.HANDCRAFTING);

    hwAddRecipe("campfire", campfire);


    //Chest
    HwRecipe chest = new HwRecipe();
    chest.setIngredients(hwItem("oak log", 8));
    chest.setResult(hwItem("chest"));
    chest.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("chest", chest);


    // Hw do class, hw do metod
    // wpisz sobie hw i masz cala liste metod


    /// Materials
    // TODO planks -> to building
    // TODO concrete -> to building

    /// Equipment
    // TODO building hammer -> building tool
    // TODO backpack?

    /// Structure
    // TODO chest
    // TODO totem of authority

    HwRecipe blastFurnace = new HwRecipe();
    blastFurnace.setIngredients(hwItem("clay", 50), hwItem("stone", 10));
    blastFurnace.setResult(hwItem("blast furnace"));
    blastFurnace.allowCraft(CraftingSource.WORKBENCH);

    hwAddRecipe("blast_furnace", blastFurnace);
  }

}
