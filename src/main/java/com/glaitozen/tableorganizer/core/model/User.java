package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record User(String id, String nom, Set<LocalDate> datesOccupees) {

    public User(String nom) {
        this(UUID.randomUUID().toString(), nom, new HashSet<>());
    }

    public User() {
        this("default name");
    }

    public boolean isAvailable(LocalDate date) {
        return !datesOccupees.contains(date);
    }

    public void addDateOccupee(LocalDate date) {
        datesOccupees.add(date);
    }

//    public void notifierPropositions(Set<PropositionDeDate> propositions) {
//         TODO
//    }
}
