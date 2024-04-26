package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record PropositionDeDate(String id, LocalDate date, Set<User> users) {
    public PropositionDeDate(LocalDate date) {
        this(UUID.randomUUID().toString(), date, new HashSet<>());
    }

    public void addMdJEtJoueurs(User mdJ, Collection<User> joueurs) {
        users.add(mdJ);
        users.addAll(joueurs);
    }
}
