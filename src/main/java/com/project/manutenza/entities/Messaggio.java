package com.project.manutenza.entities;

public class Messaggio {

    private String mittente;
    private String destinatario;
    private String messaggio;
    private String timestamp;

    public Messaggio(){
        //Empty for WebService Constructor
    }

    public Messaggio(String mittente, String destinatario, String messaggio, String timestamp) {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
