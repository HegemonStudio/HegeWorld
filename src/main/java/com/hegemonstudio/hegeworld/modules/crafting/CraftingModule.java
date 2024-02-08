package com.hegemonstudio.hegeworld.modules.crafting;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.module.HWModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftingModule extends HWModule {
  @Override
  public void onEnable() {
//    HegeWorldPlugin.GetCraftingManager().getAllBySources(CraftingSource.INVENTORY);
    // wszystkie craftingi w inventory "/craft"
//    HegeWorldPlugin.GetCraftingManager().getAll();
    // wszystkie craftingi
//    HegeWorldPlugin.GetCraftingManager().getAllBySources(CraftingSource.INVENTORY, CraftingSource.WORKBENCH);
    // wszystkie craftingi w inventory i workbenchu
    HegeWorldPlugin.GetCraftingManager()
        .addRecipe(
            HegeWorldPlugin.CreateKey("stone_axe"),
            new HWRecipe(
                List.of(
                    new ItemStack(Material.STONE, 3),
                    new ItemStack(Material.STICK, 2)
                ),
                new ItemStack(Material.STONE_AXE)
            )
        );
    // dodanie przykladowego craftingu
  }
}
