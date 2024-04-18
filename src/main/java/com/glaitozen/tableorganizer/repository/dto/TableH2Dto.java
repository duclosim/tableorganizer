package com.glaitozen.tableorganizer.repository.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_jdr")
public class TableH2Dto {

    @Id
    private String id;
    private String nom;
    private String systeme;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserH2Dto mdJ;

    @ManyToMany
    private List<UserH2Dto> joueurs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<PropositionDeDateH2Dto> propositions = new ArrayList<>();

    private LocalDate prochaineDate;
    private boolean rappelUn;
    private boolean rappelDeux;
    private boolean rappelTrois;

    public TableH2Dto() {
    }

    public TableH2Dto(String id, String nom, String systeme, UserH2Dto mdJ, List<UserH2Dto> joueurs,
                      List<PropositionDeDateH2Dto> propositions, LocalDate prochaineDate, boolean rappelUn,
                      boolean rappelDeux, boolean rappelTrois) {
        this.id = id;
        this.nom = nom;
        this.systeme = systeme;
        this.mdJ = mdJ;
        this.joueurs = joueurs;
        this.propositions = propositions;
        this.prochaineDate = prochaineDate;
        this.rappelUn = rappelUn;
        this.rappelDeux = rappelDeux;
        this.rappelTrois = rappelTrois;
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

    public String getSysteme() {
        return systeme;
    }

    public void setSysteme(String systeme) {
        this.systeme = systeme;
    }

    public UserH2Dto getMdJ() {
        return mdJ;
    }

    public void setMdJ(UserH2Dto mdJ) {
        this.mdJ = mdJ;
    }

    public List<UserH2Dto> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<UserH2Dto> joueurs) {
        this.joueurs = joueurs;
    }

    public List<PropositionDeDateH2Dto> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<PropositionDeDateH2Dto> propositions) {
        this.propositions = propositions;
    }

    public LocalDate getProchaineDate() {
        return prochaineDate;
    }

    public void setProchaineDate(LocalDate prochaineDate) {
        this.prochaineDate = prochaineDate;
    }

    public boolean isRappelUn() {
        return rappelUn;
    }

    public void setRappelUn(boolean rappelUn) {
        this.rappelUn = rappelUn;
    }

    public boolean isRappelDeux() {
        return rappelDeux;
    }

    public void setRappelDeux(boolean rappelDeux) {
        this.rappelDeux = rappelDeux;
    }

    public boolean isRappelTrois() {
        return rappelTrois;
    }

    public void setRappelTrois(boolean rappelTrois) {
        this.rappelTrois = rappelTrois;
    }
}
