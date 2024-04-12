package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PropositionDeDate {

    private final String id;
    private final LocalDate date;
    private final Set<User> users;

    public PropositionDeDate(String id, LocalDate date, Set<User> users) {
        this.id = id;
        this.date = date;
        this.users = users;
    }

    public PropositionDeDate(LocalDate date) {
        this(UUID.randomUUID().toString(), date, new HashSet<>());
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
