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
public class AddJoueurCommand implements SlashCommand {

    private final TableService tableService;

    public AddJoueurCommand(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public String getName() {
        return "add_joueur";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String tableName = event.getOption("table")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        var discordUser = event.getOption("joueur")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(val -> val.asUser().block())
                .orElse(null);

        if (tableName == null || discordUser == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Paramètres manquants : il faut préciser une table et un joueur.")
                    .then();
        }

        Table table = tableService.findByName(tableName);
        if (table == null) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("❌ Table **" + tableName + "** introuvable.")
                    .then();
        }
        User joueur = new User(discordUser.getUsername());
        table.addJoueur(joueur);

        tableService.save(table);

        return event.reply()
                .withEphemeral(false)
                .withContent("👥 Joueur **" + joueur.nom() +
                        "** ajouté à la table **" + tableName + "**.")
                .then();
    }
}
