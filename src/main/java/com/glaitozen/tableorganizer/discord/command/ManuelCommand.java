package com.glaitozen.tableorganizer.discord.command;

import com.glaitozen.tableorganizer.discord.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ManuelCommand implements SlashCommand {

    @Override
    public String getName() {
        return "manuel";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title("📖 Manuel du bot TableOrganizer")
                .description("Voici les commandes disponibles pour gérer vos tables de JdR.")
                .addField("🎲 Tables",
                        "`/creer_table nom systeme mdj`\nCréer une nouvelle table\n" +
                                "`/table nom`\nVoir les détails d'une table\n" +
                                "`/modifier_table nom systeme`\nModifier une table\n" +
                                "`/rm_table nom`\nSupprimer une table",
                        false)
                .addField("👥 Joueurs",
                        "`/add_joueur table joueur`\nAjouter un joueur\n" +
                                "`/rm_joueur table joueur`\nRetirer un joueur",
                        false)
                .addField("📅 Dates",
                        "`/proposer_dates table date`\nProposer une date\n" +
                                "`/propositions_date table`\nVoir les propositions\n" +
                                "`/confirmer_date table date`\nConfirmer une date\n" +
                                "`/annuler_date table`\nAnnuler la prochaine date",
                        false)
                .addField("⏰ Rappels",
                        "`/add_rappel table rappel`\nAjouter un rappel (1, 2 ou 3 jours avant)\n" +
                                "`/rm_rappel table rappel`\nRetirer un rappel",
                        false)
                .color(Color.BLUE)
                .build();

        return event.reply()
                .withEphemeral(true) // visible uniquement par l’utilisateur
                .withEmbeds(embed)
                .then();
    }
}
