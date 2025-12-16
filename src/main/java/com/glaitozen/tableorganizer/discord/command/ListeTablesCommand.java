package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ListeTablesCommand implements SlashCommand {

    private final TableService service;

    public ListeTablesCommand(TableService service) {
        this.service = service;
    }

    @Override
    public String getName() {
        return "liste_tables";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        List<String> tables = service.getAllTableNames();

        return event.reply()
                .withEphemeral(true)
                .withContent("Voici les tables disponibles :\n" + String.join("\n", tables))
                .then();
    }
}
