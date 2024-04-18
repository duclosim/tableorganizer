package com.glaitozen.tableorganizer.repository.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proposition_de_date")
public class PropositionDeDateH2Dto {

    @Id
    private String id;
    private LocalDate date;

    @ManyToMany
    private List<UserH2Dto> users = new ArrayList<>();

    public PropositionDeDateH2Dto() {
    }

    public PropositionDeDateH2Dto(String id, LocalDate date, List<UserH2Dto> users) {
        this.id = id;
        this.date = date;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<UserH2Dto> getUsers() {
        return users;
    }

    public void setUsers(List<UserH2Dto> users) {
        this.users = users;
    }
}
