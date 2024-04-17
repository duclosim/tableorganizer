package com.glaitozen.tableorganizer.discord.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.glaitozen.tableorganizer.discord.command.defaults.CommandExecutor;
import com.glaitozen.tableorganizer.discord.command.defaults.CommandInfo;
import com.glaitozen.tableorganizer.discord.component.DiscordMessage;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@Component
public class FactCommand implements CommandExecutor {

  @CommandInfo("fact")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest.get(RestServiceType.ANIME_API_URL + "/fact")
          .header("Accept", "application/json")
          .asJsonAsync()
          .get();

      String factMessage = httpResponse.getBody().getObject().getString("fact");

      MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#ff6666"))
          .setTitle("Did you know that..")
          .setDescription(factMessage)
          .build();

      commandSender.sendEmbedMessage(messageEmbed);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

}
