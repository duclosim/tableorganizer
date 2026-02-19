package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

@Component
public class ProposerDatesCommand implements SlashCommand {

    private final TableService tableService;

    public ProposerDatesCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "proposer_dates";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String tableName = event.getOption("table")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String dateStr = event.getOption("date")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (tableName == null || dateStr == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètres manquants.")
                    .then();
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Format de date invalide. Utilisez le format `AAAA-MM-JJ` (ex: 2025-06-15).")
                    .then();
        }

        if (!date.isAfter(LocalDate.now())) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ La date doit être dans le futur.")
                    .then();
        }

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        boolean disponible = table.isDateAvailable(date);
        table.addPropositions(Set.of(date));
        tableService.save(table);

        String message = disponible
                ? "📅 Date **" + date + "** proposée pour la table **" + tableName + "**."
                : "📅 Date **" + date + "** proposée pour la table **" + tableName + "**, mais certains participants ne sont pas disponibles ce jour-là.";

        return event.reply()
                .withEphemeral(false)
                .withContent(message)
                .then();
    }
}
