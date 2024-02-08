package com.hegemonstudio.hegeworld.general.commands;

import com.hegemonstudio.hegeworld.HegeWorldPlugin;
import com.hegemonstudio.hegeworld.api.HWPlayer;
import com.hegemonstudio.hegeworld.crafting.HWRecipe;
import com.hegemonstudio.hegeworld.modules.crafting.CraftingModule;
import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class CraftCommand extends MPlayerCommand {
  public CraftCommand() {
    super("craft");
  }

  @Override
  public void perform(@NotNull Player sender, @NotNull Command command, int argc, @NotNull String @NotNull [] args) {
    HWPlayer player = HWPlayer.of(sender);
    Collection<HWRecipe> craftingi = HegeWorldPlugin.GetCraftingManager().getAll();
    // nie wiem kurwa co ja robie jestem w to taki tragiczny ze ło babyn
    if (argc == 0) {
      BaseComponent component = new ComponentBuilder("Craftingi:\n")
          .color(ChatColor.DARK_GRAY)
          .append(String.valueOf(craftingi.toArray()[0]))
          .color(ChatColor.GREEN)
          .append("\n")
          .append(String.valueOf(craftingi.toArray()[1]))
          .color(ChatColor.GREEN)
          .build();
      player.sendMessage(component);
    }


  }
}

