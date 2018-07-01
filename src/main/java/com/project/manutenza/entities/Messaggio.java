package com.project.manutenza.entities;

import java.util.Date;

/**
 * Entità [locale non del DB) per gestire i messaggi. Ogni messaggio apparterrà ad una determinata Chat.
 * Per ogni messaggio si specifica il corpo del messaggio, la mail di chi l'ha inviato (utente o manutente) e il
 * timestamp della creazione del messaggio.
 */
public class Messaggio {

    private String messaggio;
    private String email;
    private Date timestamp;

    public Messaggio(){
        //Empty for WebService Constructor
    }

    public Messaggio(String messaggio, Date timestamp) {
        this.messaggio = messaggio;
        this.email = email;
        this.timestamp = timestamp;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
