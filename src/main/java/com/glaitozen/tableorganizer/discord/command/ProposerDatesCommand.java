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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProposerDatesCommand implements SlashCommand {

    private static final List<String> DATE_OPTIONS = List.of("date1", "date2", "date3", "date4", "date5");

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

        if (tableName == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètre manquant : table.")
                    .then();
        }

        Set<LocalDate> dates = new HashSet<>();
        for (String optName : DATE_OPTIONS) {
            String dateStr = event.getOption(optName)
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse(null);
            if (dateStr == null) continue;
            try {
                LocalDate date = LocalDate.parse(dateStr);
                if (!date.isAfter(LocalDate.now())) {
                    return event.reply()
                            .withEphemeral(true)
                            .withContent("❌ La date **" + dateStr + "** doit être dans le futur.")
                            .then();
                }
                dates.add(date);
            } catch (DateTimeParseException e) {
                return event.reply()
                        .withEphemeral(true)
                        .withContent("❌ Format invalide pour **" + dateStr + "**. Utilisez `AAAA-MM-JJ` (ex: 2025-06-15).")
                        .then();
            }
        }

        if (dates.isEmpty()) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Aucune date fournie.")
                    .then();
        }

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        long avantCount = table.getPropositions().size();
        table.addPropositions(dates);
        long ajoutees = table.getPropositions().size() - avantCount;
        tableService.save(table);

        String message = ajoutees == dates.size()
                ? "📅 " + ajoutees + " date(s) proposée(s) pour la table **" + tableName + "**."
                : "📅 " + ajoutees + "/" + dates.size() + " date(s) proposée(s) pour la table **" + tableName +
                  "** (certaines dates ignorées car des participants ne sont pas disponibles).";

        return event.reply()
                .withEphemeral(false)
                .withContent(message)
                .then();
    }
}
