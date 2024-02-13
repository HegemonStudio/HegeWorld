package com.hegemonstudio.hegeworld.modules.crafting.commands;

import com.hegemonstudio.hegeworld.api.util.ItemStackUtil;
import com.hegemonstudio.hegeworld.crafting.CraftingSource;
import com.hegemonstudio.hegeworld.crafting.HwRecipe;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.hegemonstudio.hegeworld.HegeWorld.*;


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
    HwRecipe recipe = hwGetRecipe(recipeId, CraftingSource.HANDCRAFTING);
    craftRecipe(player, recipeId, recipe);
  }

  private void printRecipes(@NotNull Player player, @NotNull TextComponent.Builder builder) {
    builder.append(
        Component.text("Craftings:")
            .color(NamedTextColor.DARK_GRAY)
    );
    for (HwRecipe recipe : hwGetRecipes(CraftingSource.HANDCRAFTING)) {
      builder.appendNewline();
      builder.appendSpace();
      createRecipeElement(builder, recipe);
    }
    builder.appendNewline();
  }

  private void craftRecipe(@NotNull Player player, @NotNull String recipeId, @Nullable HwRecipe recipe) {
    if (recipe == null) {
      player.sendMessage(Component.text("Crafting not found.").color(NamedTextColor.RED));
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f,1.0f);
      return;
    }

    if (!canCraft(player, recipe)) {
      player.sendMessage(Component.text("You don't have enough items to craft.").color(NamedTextColor.RED));
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f,1.0f);
      return;
    }

    for (ItemStack item : recipe.getIngredients()) {
      ItemStackUtil.consumeItem(player, item.getAmount(), item);
    }
    hwPlayerGiveItem(player, recipe.getResult());

    player.sendMessage(Component.text("\uD83D\uDD28 Crafted ")
        .append(Component.translatable(recipe.getTranslatableName()))
        .color(TextColor.fromCSSHexString("#0bff00"))
        .decoration(TextDecoration.UNDERLINED, true)
    );
    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1);
  }

  private void createRecipeElement(@NotNull TextComponent.Builder builder, @NotNull HwRecipe recipe) {
    boolean canCraft = canCraft(player(), recipe);
    builder.append(
        Component.translatable(recipe.getTranslatableName())
            .color(canCraft ? NamedTextColor.GREEN : NamedTextColor.RED)
            .clickEvent(ClickEvent.runCommand("/craft " + recipe.getRecipeId()))
            .hoverEvent(HoverEvent.showText(createRecipeHoverElement(recipe)))
    );
  }

  private @NotNull Component createRecipeHoverElement(@NotNull HwRecipe recipe) {
    TextComponent.Builder builder = Component.text();
    builder.append(Component.text("Ingredients:"));
    for (ItemStack ingredient : recipe.getIngredients()) {
      boolean hasIngredient = ItemStackUtil.getBooleanOfMaterial(player(), ingredient, ingredient.getAmount());
      TextColor color = hasIngredient ? NamedTextColor.GREEN : NamedTextColor.RED;
      char symbol = hasIngredient ? '✔' : '✖';
      int amount = hasIngredient ? ingredient.getAmount() : -(ingredient.getAmount() - ItemStackUtil.getSameItems(player(), ingredient));
      builder.appendNewline();
      builder.append(Component.text(symbol + " " + ingredient.getAmount() + "x ").append(Component.translatable(ingredient.translationKey()).append(Component.text(hasIngredient ? "" : " (" + ItemStackUtil.getSameItems(player(), ingredient) + ")")).color(color)));
    }
    return builder.build();
  }

  private boolean canCraft(@NotNull Player player, @NotNull HwRecipe recipe) {
    for (ItemStack ingredient : recipe.getIngredients()) {
      if (!ItemStackUtil.getBooleanOfMaterial(player, ingredient, ingredient.getAmount())) return false;
    }
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
    if (args.length == 1)
      return hwGetRecipeSelectors(CraftingSource.HANDCRAFTING);
    return List.of("Invalid argument");
  }
}

