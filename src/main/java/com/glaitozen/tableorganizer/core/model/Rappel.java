package com.glaitozen.tableorganizer.core.model;

import java.time.LocalDate;

public enum Rappel {
    UN_JOUR_AVANT(1), DEUX_JOURS_AVANT(2), TROIS_JOURS_AVANT(3);

    private int nbJours;

    Rappel(int nbJours) {
        this.nbJours = nbJours;
    }

    public int getNbJours() {
        return nbJours;
    }

    public LocalDate calculeDateRappel(LocalDate date) {
        return date.minusDays(nbJours);
    }
}
