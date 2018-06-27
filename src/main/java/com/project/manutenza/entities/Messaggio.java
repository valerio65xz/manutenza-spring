package com.project.manutenza.entities;

import java.util.Date;

public class Messaggio {

    private String messaggio;
    private Date timestamp;

    public Messaggio(){
        //Empty for WebService Constructor
    }

    public Messaggio(String messaggio, Date timestamp) {
        this.messaggio = messaggio;
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
}
