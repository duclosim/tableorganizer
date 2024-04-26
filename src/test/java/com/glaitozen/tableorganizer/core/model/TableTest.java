package com.glaitozen.tableorganizer.core.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.glaitozen.tableorganizer.core.model.Rappel.DEUX_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.TROIS_JOURS_AVANT;
import static com.glaitozen.tableorganizer.core.model.Rappel.UN_JOUR_AVANT;
import static org.assertj.core.api.Assertions.assertThat;
import static testutils.RandomLocalDateFactory.MAX_PLUS_DAYS;
import static testutils.factory.model.TableFactory.createTable;
import static testutils.factory.model.TableFactory.createTableSansConfirmerProchaineDate;

class TableTest {

    @Test
    void getRappelsDates_with_null_prochaine_date_should_return_an_empty_Set() {
        // GIVEN
        Table table = createTableSansConfirmerProchaineDate("oeil noir", new User("Maître de l'Oeil"));
        // WHEN
        Set<LocalDate> result = table.getRappelsDates();
        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    void getRappelsDates_with_well_planned_next_date_should_build_a_set_with_planned_rappels_based_on_contained_enum_instance() {
        // GIVEN
        Table table = createTable();
        table.confirmerProchaineDate(LocalDate.now().plusDays(MAX_PLUS_DAYS + 147));
        // WHEN
        Set<LocalDate> result = table.getRappelsDates();
        // THEN
        for (Rappel rappel : table.getRappels()) {
            switch (rappel) {
                case UN_JOUR_AVANT -> assertThat(result).contains(table.getProchaineDate().minusDays(UN_JOUR_AVANT.getNbJours()));
                case DEUX_JOURS_AVANT -> assertThat(result).contains(table.getProchaineDate().minusDays(DEUX_JOURS_AVANT.getNbJours()));
                case TROIS_JOURS_AVANT -> assertThat(result).contains(table.getProchaineDate().minusDays(TROIS_JOURS_AVANT.getNbJours()));
            }
        }
    }

    @Test
    void isDateAvailable_with_all_occupied_dates_should_return_false() {
        // GIVEN
        Table table = createTable();
        Set<LocalDate> occupiedDates = table.getJoueurs().stream().map(User::datesOccupees).flatMap(Set::stream).collect(Collectors.toSet());
        occupiedDates.addAll(table.getMdJ().datesOccupees());
        // WHEN
        boolean accResult = occupiedDates.stream().noneMatch(table::isDateAvailable);
        // THEN
        assertThat(accResult).isTrue();
    }

    @Test
    void addJoueur_should_add_it_to_embedded_set_and_add_it_to_all_proposition_de_date_users_set() {
        // GIVEN
        Table table = createTable();
        final User joueur = new User("Joueur 1");
        // WHEN
        table.addJoueur(joueur);
        // THEN
        assertThat(table.getJoueurs()).contains(joueur);
        table.getPropositions().forEach(pdd -> assertThat(pdd.users()).contains(joueur));
    }

    @Test
    void removeJoueur_should_remove_it_from_embedded_set_and_remove_it_from_all_proposition_de_date_users_set() {
        // GIVEN
        Table table = createTable();
        final User joueur = table.getJoueurs().stream().findFirst().orElse(null);
        // WHEN
        table.removeJoueur(joueur);
        // THEN
        assertThat(table.getJoueurs()).doesNotContain(joueur);
        table.getPropositions().forEach(pdd -> assertThat(pdd.users()).doesNotContain(joueur));
    }

    @Test
    void addProposition_with_an_already_taken_date_should_add_it_to_the_propositions_and_add_all_users_and_master_to_its_users_list() {
        // GIVEN
        Table table = createTable();
        final LocalDate newFreeDate = table.getMdJ().datesOccupees().stream().findFirst().get();
        // WHEN
        table.addProposition(newFreeDate);
        // THEN
        Optional<PropositionDeDate> newPropositionDeDate = table.getPropositions().stream().filter(pdd -> pdd.date().equals(newFreeDate)).findAny();
        assertThat(newPropositionDeDate).isNotPresent();
    }

    @Test
    void addProposition_with_a_free_date_should_add_it_to_the_propositions_and_add_all_users_and_master_to_its_users_list() {
        // GIVEN
        Table table = createTable();
        final LocalDate newFreeDate = LocalDate.now().plusDays(MAX_PLUS_DAYS + 25);
        // WHEN
        table.addProposition(newFreeDate);
        // THEN
        Optional<PropositionDeDate> newPropositionDeDate = table.getPropositions().stream().filter(pdd -> pdd.date().equals(newFreeDate)).findAny();
        assertThat(newPropositionDeDate).isPresent();
        assertThat(newPropositionDeDate.get().users()).contains(table.getMdJ());
        assertThat(newPropositionDeDate.get().users()).containsAll(table.getJoueurs());
    }

    @Test
    void addPropositions() {
        // TODO ajouter le test avec la relance des joueurs
    }

    @Test
    void confirmerProchaineDate_on_already_taken_date_should_do_nothing() {
        // GIVEN
        Table table = createTableSansConfirmerProchaineDate("Masques de Nyarlathotep", new User("Dieu Pi"));
        final LocalDate prochaineDate = table.getJoueurs().stream().findAny().get().datesOccupees().stream().findAny().get();
        // WHEN
        table.confirmerProchaineDate(prochaineDate);
        // THEN
        assertThat(table.getProchaineDate()).isNull();
    }

    @Test
    void confirmerProchaineDate_on_a_free_date_should_set_it_and_set_it_as_taken_on_mdj_and_joueurs_dates_occupees() {
        // GIVEN
        Table table = createTableSansConfirmerProchaineDate("Masques de Nyarlathotep", new User("Dieu Pi"));
        final LocalDate prochaineDate = LocalDate.now().plusDays(MAX_PLUS_DAYS + 25);
        // WHEN
        table.confirmerProchaineDate(prochaineDate);
        // THEN
        assertThat(table.getProchaineDate()).isEqualTo(prochaineDate);
        assertThat(table.getMdJ().datesOccupees()).contains(prochaineDate);
        table.getJoueurs().forEach(joueur -> assertThat(joueur.datesOccupees()).contains(prochaineDate));
    }
}
