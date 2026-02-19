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
public class AnnulerProchaineDate implements SlashCommand {

    private final TableService tableService;

    public AnnulerProchaineDate(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "annuler_prochaine_date";
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

        if (table.getProchaineDate() == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Aucune date prévue pour la table **" + tableName + "**.")
                    .then();
        }

        table.annulerProchaineDate();
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("❌ Prochaine date annulée pour la table **" + tableName + "**.")
                .then();
    }
}
