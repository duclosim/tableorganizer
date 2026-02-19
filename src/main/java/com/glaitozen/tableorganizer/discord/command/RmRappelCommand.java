package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.Rappel;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RmRappelCommand implements SlashCommand {

    private final TableService tableService;

    public RmRappelCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "rm_rappel";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String tableName = event.getOption("table")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String rappelStr = event.getOption("rappel")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (tableName == null || rappelStr == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètres manquants.")
                    .then();
        }

        Rappel rappel;
        try {
            rappel = Rappel.valueOf(rappelStr);
        } catch (IllegalArgumentException e) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Rappel invalide : " + rappelStr)
                    .then();
        }

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        if (!table.getRappels().contains(rappel)) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Ce rappel n'existe pas pour la table **" + tableName + "**.")
                    .then();
        }

        table.removeRappel(rappel);
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("⏰ Rappel **" + rappelStr + "** retiré pour la table **" + tableName + "**.")
                .then();
    }
}
