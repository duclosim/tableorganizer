package com.glaitozen.tableorganizer.repository.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserH2Dto {

    @Id
    private String id;
    private String nom;
    private String datesOccupees;

    public UserH2Dto() {
    }

    public UserH2Dto(String id, String nom, String datesOccupees) {
        this.id = id;
        this.nom = nom;
        this.datesOccupees = datesOccupees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDatesOccupees() {
        return datesOccupees;
    }

    public void setDatesOccupees(String datesOccupees) {
        this.datesOccupees = datesOccupees;
    }
}
