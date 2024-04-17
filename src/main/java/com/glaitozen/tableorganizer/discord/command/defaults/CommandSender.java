package com.glaitozen.tableorganizer.discord.command.defaults;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class CommandSender {

  private final MessageChannel messageChannel;
  private final MessageComponent messageComponent;

  public CommandSender(MessageChannel messageChannel, MessageComponent messageComponent) {
    this.messageChannel = messageChannel;
    this.messageComponent = messageComponent;
  }

  public void sendRawMessage(String message, Object... arguments) {
    this.messageChannel.sendMessageFormat(message, arguments).queue();
  }

  public void sendRawMessage(List<String> messages, Object... arguments) {
    messages.forEach(message -> this.sendRawMessage(message, arguments));
  }

  public void sendMessage(String key, String... arguments) {
    this.messageChannel.sendMessage(this.messageComponent.get(key, arguments)).queue();
  }

  public void sendMessage(List<String> keys, String... arguments) {
    keys.forEach(key -> this.sendMessage(key, arguments));
  }

  public String getMessage(String key, String... arguments) {
    return this.messageComponent.get(key, arguments);
  }

  public void sendEmbedMessage(MessageEmbed message) {
    this.messageChannel.sendMessage(message).queue();
  }

  public MessageChannel getMessageChannel() {
    return this.messageChannel;
  }
}
