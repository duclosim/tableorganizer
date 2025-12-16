package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RmJoueurCommand implements SlashCommand {

    private final TableService tableService;

    public RmJoueurCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "rm_joueur";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String tableName = event.getOption("table")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        var discordUser = event.getOption("joueur")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(val -> val.asUser().block())
                .orElse(null);

        if (tableName == null || discordUser == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètres manquants : il faut préciser une table et un joueur.")
                    .then();
        }
        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }
        boolean removed = table.getJoueurs().removeIf(j -> j.nom().equals(discordUser.getUsername()));

        if (!removed) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Joueur **" + discordUser.getUsername() +
                            "** n'était pas présent dans la table **" + tableName + "**.")
                    .then();
        }
        tableService.save(table);
        return event.reply()
                .withEphemeral(false)
                .withContent("👥 Joueur **" + discordUser.getUsername() +
                        "** retiré de la table **" + tableName + "**.")
                .then();
    }
}
