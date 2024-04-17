package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.discord.command.defaults.CommandExecutor;
import com.glaitozen.tableorganizer.discord.command.defaults.CommandInfo;
import com.glaitozen.tableorganizer.discord.component.DiscordMessage;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class EchoCommand implements CommandExecutor {

  @CommandInfo(value = "echo", minArguments = 1, maxArguments = 1, usage = "<message>")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {

    MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#ffffff"))
          .setTitle(dMessage.getSentBy() + " said..")
          .setDescription(dMessage.getData())
          .build();

    commandSender.sendEmbedMessage(messageEmbed);
  }
}
