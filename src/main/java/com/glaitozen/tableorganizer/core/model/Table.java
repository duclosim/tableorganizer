package com.glaitozen.tableorganizer.core.model;

import com.glaitozen.tableorganizer.utils.DateUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Table {

    private final String id;
    private final String nom;
    private final String systeme;
    private final User mdJ;
    private final Set<User> joueurs;
    private final Set<PropositionDeDate> propositions;
    private LocalDate prochaineDate;
    private final Set<Rappel> rappels;

    public Table(String id, String nom, String systeme, User mdJ, Set<User> joueurs, Set<PropositionDeDate> propositions,
                 LocalDate prochaineDate, Set<Rappel> rappels) {
        this.id = id;
        this.nom = nom;
        this.systeme = systeme;
        this.mdJ = mdJ;
        this.joueurs = joueurs;
        this.propositions = DateUtils.cleanPastPropositionDeDates(propositions);
        this.prochaineDate = DateUtils.cleanPastDate(prochaineDate);
        this.rappels = rappels;
    }

    public Table(String nom, String systeme, User mdJ) {
        this(UUID.randomUUID().toString(), nom, systeme, mdJ, new HashSet<>(), new HashSet<>(), null,
                EnumSet.noneOf(Rappel.class));
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getSysteme() {
        return systeme;
    }

    public User getMdJ() {
        return mdJ;
    }

    public Set<User> getJoueurs() {
        return joueurs;
    }

    public Set<PropositionDeDate> getPropositions() {
        return propositions;
    }

    public LocalDate getProchaineDate() {
        return prochaineDate;
    }

    public Set<Rappel> getRappels() {
        return rappels;
    }

    public Set<LocalDate> getRappelsDates() {
        if (prochaineDate == null) {
            return Collections.emptySet();
        }
        return rappels.stream()
                .map(r -> r.calculeDateRappel(prochaineDate))
                .collect(Collectors.toSet());
    }

    public boolean isDateAvailable(LocalDate date) {
        return joueurs.stream().allMatch(j -> j.isAvailable(date)) && mdJ.isAvailable(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id) && Objects.equals(nom, table.nom) && Objects.equals(systeme, table.systeme)
                && Objects.equals(mdJ, table.mdJ) && Objects.equals(joueurs, table.joueurs)
                && Objects.equals(propositions, table.propositions) && Objects.equals(prochaineDate, table.prochaineDate)
                && Objects.equals(rappels, table.rappels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, systeme, mdJ, joueurs, propositions, prochaineDate, rappels);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", systeme='" + systeme + '\'' +
                ", mdJ=" + mdJ +
                ", joueurs=" + joueurs +
                ", propositions=" + propositions +
                ", prochaineDate=" + prochaineDate +
                ", rappels=" + rappels +
                '}';
    }

    public void addJoueur(User joueur) {
        joueurs.add(joueur);
    }

    public void removeJoueur(User joueur) {
        joueurs.remove(joueur);
    }

    void addProposition(LocalDate date) {
        PropositionDeDate proposition = new PropositionDeDate(date);
        proposition.users().add(mdJ);
        joueurs.forEach(joueur -> proposition.users().add(joueur));
        propositions.add(proposition);
    }

    public void addPropositions(Set<LocalDate> dates) {
        dates.forEach(this::addProposition);
        relancerJoueurs();
    }

    public void addRappel(Rappel r) {
        rappels.add(r);
    }

    public void removeRappel(Rappel r) {
        rappels.remove(r);
    }

    public void confirmerProchaineDate(LocalDate prochaineDate) {
        this.prochaineDate = prochaineDate;
        mdJ.addDateOccupee(prochaineDate);
        joueurs.forEach(joueur -> joueur.addDateOccupee(prochaineDate));
    }

    public void relancerJoueurs() {
        if (!propositions.isEmpty()) {
            for (User joueur : joueurs) {
                joueur.notifierPropositions(propositions);
            }
        }
    }

}
