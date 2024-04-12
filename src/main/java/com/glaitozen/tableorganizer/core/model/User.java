package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {

    private final String id;
    private final String nom;
    private Set<LocalDate> datesOccupees;

    public User() {
        this("default name");
    }

    public User(String nom) {
        this(UUID.randomUUID().toString(), nom, new HashSet<>());
    }

    public User(String id, String nom, Set<LocalDate> datesOccupees) {
        this.id = id;
        this.nom = nom;
        this.datesOccupees = datesOccupees;
        cleanAnciennesDates();
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Set<LocalDate> getDatesOccupees() {
        return datesOccupees;
    }

    public boolean isAvailable(LocalDate date) {
        return !datesOccupees.contains(date);
    }

    public void addDateOccupee(LocalDate date) {
        datesOccupees.add(date);
    }

    void cleanAnciennesDates() {
        final LocalDate clearingLimit = LocalDate.now();
        datesOccupees = datesOccupees.stream()
                .filter(d -> d.isAfter(clearingLimit))
                .collect(Collectors.toSet());
    }
}
