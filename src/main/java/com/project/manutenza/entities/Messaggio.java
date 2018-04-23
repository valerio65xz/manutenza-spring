package com.project.manutenza.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

//Dichiaro entity per salvare nel DB
@Entity
public class Messaggio {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private String mittente;
    private String destinatario;
    private String messaggio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamptz")
    @DateTimeFormat(pattern="dd.MM HH:mm") //this is for display and parsing, not storage.
    private Date timestamp;

    public Messaggio(){
        //Empty for WebService Constructor
    }

    public Messaggio(String mittente, String destinatario, String messaggio, Date timestamp) {
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.messaggio = messaggio;
        this.timestamp = timestamp;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
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
}
