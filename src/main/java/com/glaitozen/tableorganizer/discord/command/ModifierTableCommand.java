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
public class ModifierTableCommand implements SlashCommand {

    private final TableService tableService;

    public ModifierTableCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "modifier_table";
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

        String nouveauNom = event.getOption("nom")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String nouveauSysteme = event.getOption("systeme")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (nouveauNom == null && nouveauSysteme == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Aucune modification fournie.")
                    .then();
        }

        if (nouveauNom != null) {
            table.setNom(nouveauNom);
        }
        if (nouveauSysteme != null) {
            table.setSysteme(nouveauSysteme);
        }
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("✏️ Table **" + tableName + "** mise à jour.")
                .then();
    }
}
