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

@Component
public class ConfirmerDateCommand implements SlashCommand {

    private final TableService tableService;

    public ConfirmerDateCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "confirmer_date";
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

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        if (!table.isDateAvailable(date)) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("⚠️ Certains participants ne sont pas disponibles le **" + date + "**. La date n'a pas été confirmée.")
                    .then();
        }

        table.confirmerProchaineDate(date);
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("✅ Prochaine session de **" + tableName + "** confirmée pour le **" + date + "**.")
                .then();
    }
}
