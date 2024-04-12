package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Table {

    private final String id;
    private String nom;
    private String systeme;
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
        this.propositions = propositions;
        this.prochaineDate = prochaineDate;
        this.rappels = rappels;
        cleanProchaineDate();
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSysteme(String systeme) {
        this.systeme = systeme;
    }

    public void addUser(User joueur) {
        joueurs.add(joueur);
    }

    public void addRappel(Rappel r) {
        rappels.add(r);
    }

    public void confirmerProchaineDate(LocalDate prochaineDate) {
        this.prochaineDate = prochaineDate;
        for (User joueur : joueurs) {
            mdJ.addDateOccupee(prochaineDate);
            joueur.addDateOccupee(prochaineDate);
        }
    }

    public void relancerJoueurs() {

    }

    void cleanProchaineDate() {
        prochaineDate = prochaineDate.isBefore(LocalDate.now()) ? null : prochaineDate;
    }


}
