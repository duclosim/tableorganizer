package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.core.service.TableService;
import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreerTableCommand implements SlashCommand {

    private final TableService tableService;

    public CreerTableCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "creer_table";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String nom = event.getOption("nom")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String systeme = event.getOption("systeme")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        var discordMdJ = event.getOption("mdj")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(val -> val.asUser().block())
                .orElse(null);

        if (nom == null || systeme == null || discordMdJ == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètres manquants.")
                    .then();
        }

        User mdJ = new User(discordMdJ.getUsername());
        Table table = new Table(nom, systeme, mdJ);
        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("🎲 Table **" + nom + "** (" + systeme + ") créée avec **" + mdJ.nom() + "** comme MdJ !")
                .then();
    }
}
