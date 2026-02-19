package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class RelancerJoueursCommand implements SlashCommand {

    private final TableService tableService;

    public RelancerJoueursCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "relancer_joueurs";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String tableName = event.getOption("table")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (tableName == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètre manquant : table.")
                    .then();
        }

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        if (table.getJoueurs().isEmpty()) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Aucun joueur dans la table **" + tableName + "**.")
                    .then();
        }

        String joueursMentions = table.getJoueurs().stream()
                .map(User::nom)
                .collect(Collectors.joining(", "));

        String propositionsList;
        if (table.getPropositions().isEmpty()) {
            propositionsList = "Aucune proposition de date en attente.";
        } else {
            propositionsList = table.getPropositions().stream()
                    .sorted(Comparator.comparing(PropositionDeDate::date))
                    .map(ppd -> "• " + ppd.date())
                    .collect(Collectors.joining("\n"));
        }

        String message = "📣 Relance pour la table **" + tableName + "** !\n" +
                "Joueurs : " + joueursMentions + "\n\n" +
                "**Propositions de dates en attente :**\n" + propositionsList;

        return event.reply()
                .withEphemeral(false)
                .withContent(message)
                .then();
    }
}
