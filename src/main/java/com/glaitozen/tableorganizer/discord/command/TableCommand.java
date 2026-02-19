package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
import com.glaitozen.tableorganizer.core.model.Rappel;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class TableCommand implements SlashCommand {

    private final TableService tableService;

    public TableCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "table";
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

        String joueurs = table.getJoueurs().isEmpty()
                ? "Aucun joueur"
                : table.getJoueurs().stream().map(User::nom).collect(Collectors.joining(", "));

        String propositions = table.getPropositions().isEmpty()
                ? "Aucune proposition"
                : table.getPropositions().stream()
                    .map(ppd -> ppd.date().toString() + " (" + ppd.users().size() + " participant(s))")
                    .collect(Collectors.joining("\n"));

        String prochaineDate = table.getProchaineDate() != null
                ? table.getProchaineDate().toString()
                : "Non définie";

        String rappels = table.getRappels().isEmpty()
                ? "Aucun rappel"
                : table.getRappels().stream().map(Rappel::name).collect(Collectors.joining(", "));

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title("🎲 " + table.getNom())
                .addField("Système", table.getSysteme(), true)
                .addField("MdJ", table.getMdJ().nom(), true)
                .addField("Joueurs", joueurs, false)
                .addField("Prochaine date", prochaineDate, true)
                .addField("Propositions", propositions, false)
                .addField("Rappels", rappels, false)
                .color(Color.BLUE)
                .build();

        return event.reply()
                .withEphemeral(false)
                .withEmbeds(embed)
                .then();
    }
}
