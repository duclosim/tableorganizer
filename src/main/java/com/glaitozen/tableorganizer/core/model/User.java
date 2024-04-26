package com.glaitozen.tableorganizer.core.model;

import com.glaitozen.tableorganizer.utils.DateUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record User(String id, String nom, Set<LocalDate> datesOccupees) {

    public User(String id, String nom, Set<LocalDate> datesOccupees) {
        this.id = id;
        this.nom = nom;
        this.datesOccupees = datesOccupees;
        clearDateOccupees();
    }

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

    public void addDateOccupeeCollection(Collection<LocalDate> dateSet) {
        datesOccupees.addAll(dateSet);
    }

    public void clearDateOccupees() {
        datesOccupees.retainAll(DateUtils.cleanPastDateCollection(datesOccupees));
    }

    //    public void notifierPropositions(Set<PropositionDeDate> propositions) {
//         TODO
//    }
}
