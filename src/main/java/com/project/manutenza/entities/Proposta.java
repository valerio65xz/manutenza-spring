package com.project.manutenza.entities;

import javax.persistence.*;

/**
 * Entity per la gestione di una Proposta. E' direttamente collegata alla tabella del DB "proposta"
 */
@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private boolean accettato;
    private double prezzo;
    private Long manutente_id;
    private Long richiesta_id;

    public Proposta(){}

    public Proposta(boolean accettato, double prezzo, Long manutente_id, Long richiesta_id) {
        this.accettato = accettato;
        this.prezzo = prezzo;
        this.manutente_id = manutente_id;
        this.richiesta_id = richiesta_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public Long getManutente_id() {
        return manutente_id;
    }

    public void setManutente_id(Long manutente_id) {
        this.manutente_id = manutente_id;
    }

    public Long getRichiesta_id() {
        return richiesta_id;
    }

    public void setRichiesta_id(Long richiesta_id) {
        this.richiesta_id = richiesta_id;
    }
}
