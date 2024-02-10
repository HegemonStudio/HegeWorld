package com.hegemonstudio.hegeworld.modules.crafting.commands;

import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.hegemonstudio.hegeworld.HegeWorld.hwGetRecipe;
import static com.hegemonstudio.hegeworld.HegeWorld.hwGetRecipes;


public class CraftCommand extends MPlayerCommand {
  public CraftCommand() {
    super("craft");
  }

  @Override
  public void perform(@NotNull Player sender, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    HWPlayer player = HWPlayer.of(sender);

    if (argc == 0) {
      Collection<HWRecipe> recipes = hwGetRecipes(CraftingSource.INVENTORY);
      TextComponent.Builder builder = Component.text();
      builder.append(
          Component.text("Craftings:")
              .color(NamedTextColor.DARK_GRAY)
      );
      // Append all recipes
      for (HWRecipe recipe : recipes) {
        builder.appendNewline();
        builder.append(
            Component.translatable(recipe.getTranslatableName())
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("/" + getLabel() + " " + recipe.getRecipeId()))
        );
      }
      player.sendMessage(builder.build());
      return;
    }

    HWRecipe recipe = hwGetRecipe(args[0], CraftingSource.INVENTORY);
    if (recipe == null) {
      // TODO nie ma takiego craftingu
      player.sendMessage("CANNOT CRAFT " + args[0] + " | NOT FOUND");
      return;
    }
    // TODO jest taki crafting
    player.sendMessage("CRAFT " + recipe.getRecipeId());
  }
}

