package com.hegemonstudio.hegeworld.modules.crafting.commands;

import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.hegemonstudio.hegeworld.HegeWorld.hwGetRecipe;
import static com.hegemonstudio.hegeworld.HegeWorld.hwGetRecipes;


public class CraftCommand extends MPlayerCommand {
  public CraftCommand() {
    super("craft");
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    if (argc == 0) {
      TextComponent.Builder builder = Component.text();
      printRecipes(player, builder);
      player.sendMessage(builder.build());
      return;
    }

    if (args.length > 1) {
      error(args.length - 1, "Invalid argument!");
      responseSound(Sound.ENTITY_VILLAGER_NO);
      return;
    }

    String recipeId = args[0];
    HWRecipe recipe = hwGetRecipe(recipeId, CraftingSource.INVENTORY);
    craftRecipe(player, recipeId, recipe);
  }

  private void printRecipes(@NotNull Player player, @NotNull TextComponent.Builder builder) {
    builder.append(
        Component.text("Craftings:")
            .color(NamedTextColor.DARK_GRAY)
    );
    for (HWRecipe recipe : hwGetRecipes(CraftingSource.INVENTORY)) {
      createRecipeElement(builder, recipe);
    }
  }

  private void craftRecipe(@NotNull Player player, @NotNull String recipeId, @Nullable HWRecipe recipe) {
    // TODO crafting
    if (recipe == null) {
      player.sendMessage("CANNOT CRAFT " + recipeId + " | NOT FOUND");
      return;
    }
    player.sendMessage("CRAFT " + recipe.getRecipeId());
  }

  private void createRecipeElement(@NotNull TextComponent.Builder builder, @NotNull HWRecipe recipe) {
    builder.append(
        Component.translatable(recipe.getTranslatableName())
            .color(NamedTextColor.GREEN)
            .clickEvent(ClickEvent.runCommand("/craft " + recipe.getRecipeId()))
            .hoverEvent(HoverEvent.showText(Component.text(recipe.getRecipeId().toString())))
    );
  }
}

