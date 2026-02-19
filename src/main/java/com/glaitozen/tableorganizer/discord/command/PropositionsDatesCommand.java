package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.PropositionDeDate;
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

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class PropositionsDatesCommand implements SlashCommand {

    private final TableService tableService;

    public PropositionsDatesCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "propositions_date";
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

        if (table.getPropositions().isEmpty()) {
            return event.reply()
                    .withEphemeral(false)
                    .withContent("ℹ️ Aucune proposition de date pour la table **" + tableName + "**.")
                    .then();
        }

        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title("📅 Propositions de dates — " + tableName)
                .color(Color.GREEN);

        table.getPropositions().stream()
                .sorted(Comparator.comparing(PropositionDeDate::date))
                .forEach(ppd -> {
                    String participants = ppd.users().isEmpty()
                            ? "Aucun participant"
                            : ppd.users().stream().map(User::nom).collect(Collectors.joining(", "));
                    embedBuilder.addField(ppd.date().toString(), participants, false);
                });

        return event.reply()
                .withEphemeral(false)
                .withEmbeds(embedBuilder.build())
                .then();
    }
}
