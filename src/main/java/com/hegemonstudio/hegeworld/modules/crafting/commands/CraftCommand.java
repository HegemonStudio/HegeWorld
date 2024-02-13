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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
  
  private Collection<HwRecipe> sortedRecipes(@NotNull Player player) {
    return hwGetRecipes(CraftingSource.HANDCRAFTING).stream()
        .sorted(Comparator.comparingInt((recipe) -> canCraft(player, recipe) ? 1 : 0))
        .collect(Collectors.toList());
  }

  private void printRecipes(@NotNull Player player, @NotNull TextComponent.Builder builder) {
    builder.append(
        Component.text(" \uD83D\uDEE0 HANDCRAFTING \uD83D\uDEE0")
            .color(NamedTextColor.GOLD)
    );
    builder.appendNewline();
    for (HwRecipe recipe : sortedRecipes(player)) {
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
    );
    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1);
    player.performCommand("craft");
  }

  private void createRecipeElement(@NotNull TextComponent.Builder builder, @NotNull HwRecipe recipe) {
    boolean canCraft = canCraft(player(), recipe);
    if (canCraft) {
      builder.append(Component.text("\uD83D\uDD28 ").color(NamedTextColor.GREEN));
    }
    builder.append(
        Component.translatable(recipe.getTranslatableName())
            .color(canCraft ? NamedTextColor.GREEN : NamedTextColor.RED)
            .clickEvent(ClickEvent.runCommand("/craft " + recipe.getRecipeId()))
            .hoverEvent(HoverEvent.showText(createRecipeHoverElement(recipe)))
            .decoration(TextDecoration.UNDERLINED, canCraft)
    );
  }

  private @NotNull Component createRecipeHoverElement(@NotNull HwRecipe recipe) {
    Player player = player();
    TextComponent.Builder builder = Component.text();
    boolean canCraft = canCraft(player(), recipe);
    builder.append(Component.translatable(recipe.getTranslatableName()).color(canCraft ? NamedTextColor.GREEN : NamedTextColor.RED));
    builder.appendNewline();
    builder.append(Component.text("Needed items:").color(NamedTextColor.GRAY));
    List<ItemStack> ingredients = recipe.getIngredients();
    ingredients.sort(Comparator.comparingInt(is -> (ItemStackUtil.getSameItems(player, is) >= is.getAmount() ? 1 : 0)));
    // ingredients.sort((is1, is2) -> (ItemStackUtil.getSameItems(player, is1) >= is1.getAmount() ? 1 : 0) - (ItemStackUtil.getSameItems(player, is2) >= is2.getAmount() ? 1 : 0));

    for (ItemStack ingredient : ingredients) {
      boolean hasIngredient = ItemStackUtil.getBooleanOfMaterial(player(), ingredient, ingredient.getAmount());
      TextColor color = hasIngredient ? NamedTextColor.GREEN : NamedTextColor.RED;
      char symbol = hasIngredient ? '✔' : '❌';
      builder.appendNewline();
      builder.appendSpace();
      builder.append(Component.text(symbol + " " + ingredient.getAmount() + "x ").append(Component.translatable(ingredient.translationKey()).append(Component.text(hasIngredient ? "" : " (" + ItemStackUtil.getSameItems(player(), ingredient) + ")")).color(color)));
    }
    builder.appendNewline();
    builder.appendNewline();
    if (canCraft) {
      builder.append(Component.text("\uD83D\uDD28 Click to craft").color(NamedTextColor.GREEN).decoration(TextDecoration.UNDERLINED, true));
    } else {
      builder.append(Component.text("❌ Not enough items").color(NamedTextColor.RED));
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

