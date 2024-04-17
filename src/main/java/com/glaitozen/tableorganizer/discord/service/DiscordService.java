package com.glaitozen.tableorganizer.discord.service;

import com.glaitozen.tableorganizer.discord.command.defaults.Command;
import com.glaitozen.tableorganizer.discord.command.defaults.CommandSender;
import com.glaitozen.tableorganizer.discord.component.DiscordMessage;
import com.glaitozen.tableorganizer.discord.component.MessageComponent;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscordService {

  private final MessageComponent messageComponent;

  public DiscordService(MessageComponent messageComponent) {
    this.messageComponent = messageComponent;
  }

  @Override
  public void startBot() throws LoginException, InterruptedException {
    this.jda = JDABuilder.createLight(properties.getDiscordToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
    .setEventManager(new AnnotatedEventManager())
    .setActivity(Activity.playing(properties.getPrefix()))
    .build();
  }

  @Override
  public void shutdownBot() {
    this.jda.shutdown();
  }

  @Override
  public void registerListeners(Object... listeners) {
    this.jda.addEventListener(listeners);
  }

  @Override
  public JDA getJda() {
    return this.jda;
  }

  @Override
  public void registerCommand(Command botCommand) {
    this.jda.addEventListener(new AnnotatedEventManager() {

      @SubscribeEvent
      public void messageReceivedEvent(MessageReceivedEvent event) {
        processMessage(event, botCommand);
      }

    });
  }

  public void processMessage(MessageReceivedEvent event, Command command) {
    String messageContent = event.getMessage().getContentRaw();

    if (messageContent.startsWith(properties.getPrefix() + command.getName())) {
      DiscordMessage dMessage = new DiscordMessage(event, command, properties.getPrefix());

      this.processCommand(dMessage);
    }
  }

  private void processCommand(DiscordMessage dMessage) {
    MessageChannel messageChannel = dMessage.getEvent().getChannel();
    CommandSender commandSender = new CommandSender(messageChannel, messageComponent);
    Command command = dMessage.getCommandData();

    List<String> args = Arrays.stream(dMessage.getEvent().getMessage().getContentRaw().split(" ")).skip(1)
      .collect(Collectors.toList());

    if ((args.size() < command.getMinArguments()) || (args.size() > command.getMaxArguments())) {
      String usage = properties.getPrefix() + command.getName() + " " + command.getUsage();
      MessageEmbed messageEmbed = new EmbedBuilder()
        .setColor(Color.RED)
        .setFooter(messageComponent.get("invalid-pattern", usage), null)
        .build();
      messageChannel.sendMessage(messageEmbed).queue();
      return;
    }

    dMessage.getCommandData().getExecutor().execute(dMessage, commandSender);
  }

}
