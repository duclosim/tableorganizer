package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record PropositionDeDate(String id, LocalDate date, Set<User> users) {
    public PropositionDeDate(LocalDate date) {
        this(UUID.randomUUID().toString(), date, new HashSet<>());
    }
}
