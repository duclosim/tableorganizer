package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.Rappel;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AddRappelCommand implements SlashCommand {

    private final TableService tableService;

    public AddRappelCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "add_rappel";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        var tableName = event.getOption("table")
                .flatMap(opt -> opt.getValue())
                .map(val -> val.asString())
                .orElse(null);

        int joursAvant = event.getOption("rappel")
                .flatMap(opt -> opt.getValue())
                .map(val -> val.asLong())
                .orElse(-1);

        if (tableName == null || joursAvant < 1 || joursAvant > 3) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Le rappel doit être 1, 2 ou 3 jours avant la session.")
                    .then();
        }

        var table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }

        var rappel = switch (joursAvant) {
            case 1 -> Rappel.UN_JOUR_AVANT;
            case 2 -> Rappel.DEUX_JOURS_AVANT;
            case 3 -> Rappel.TROIS_JOURS_AVANT;
            default -> null;
        };

        if (!table.getRappels().add(rappel)) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("ℹ️ Un rappel **" + joursAvant + " jours avant** existe déjà pour cette table.")
                    .then();
        }
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("⏰ Rappel ajouté : **" + joursAvant + " jours avant** pour la table **" + tableName + "**.")
                .then();
    }
}
