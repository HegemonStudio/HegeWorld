package com.hegemonstudio.hegeworld.modules.general.commands;

import com.impact.lib.api.command.MPlayerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends MPlayerCommand {
  public HelpCommand() {
    super("hwhelp");
  }

  @Override
  public void perform(@NotNull Player player, @NotNull Command command, int i, @NotNull String @NotNull [] args) {
    if (args.length == 0) {
      Component text = Component.text("HegeWorld Help")
          .decoration(TextDecoration.BOLD, true)
          .color(TextColor.color(TextColor.fromCSSHexString("#00ff11")))
          .appendNewline();
      Component helps = Component.text("Jaką pomoc potrzebujesz?")
          .color(TextColor.color(TextColor.fromCSSHexString("#9affee")))
          .append(Component.text("- /hwhelp komendy"));
      player.sendMessage(text);
      player.sendMessage(helps);

    }
  }
}
