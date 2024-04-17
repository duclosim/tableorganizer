package com.glaitozen.tableorganizer.discord.command.defaults;

import com.glaitozen.tableorganizer.discord.component.DiscordMessage;

public interface CommandExecutor {

  void execute(DiscordMessage dMessage, CommandSender commandSender);

}
