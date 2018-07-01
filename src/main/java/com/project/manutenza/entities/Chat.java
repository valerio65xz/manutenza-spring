package com.project.manutenza.entities;

import java.util.ArrayList;

/**
 * Entità [locale non del DB) per gestire la Chat. Ogni chat è identificata univocamente con l'ID della proposta ad
 * essa associata. Ogni chat prevede un utente e un manutente, identificati mediante la propria mail. Per ognuno di
 * loro un flag definisce chi dei due ha letto almeno una volta la chat. Poiché la chat è aggiunta ad una coda
 * temporanea di messaggi non letti, una volta che verranno letti da entrambi, la coda verrà svuotata di tale chat.
 * Infine una chat contiene un ArrayList di Messaggi.
 */
public class Chat {

    private int idProposta;
    private String utenteEmail;
    private String manutenteEmail;
    private boolean readByUtente;
    private boolean readByManutente;
    private ArrayList<Messaggio> listaMessaggi;

    public Chat(){}

    public Chat(int idProposta, String utenteEmail, String manutenteEmail, boolean readByUtente, boolean readByManutente, ArrayList<Messaggio> listaMessaggi) {
        this.idProposta = idProposta;
        this.utenteEmail = utenteEmail;
        this.manutenteEmail = manutenteEmail;
        this.readByUtente = readByUtente;
        this.readByManutente = readByManutente;
        this.listaMessaggi = listaMessaggi;
    }

    public int getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(int idProposta) {
        this.idProposta = idProposta;
    }

    public String getUtenteEmail() {
        return utenteEmail;
    }

    public void setUtenteEmail(String utenteEmail) {
        this.utenteEmail = utenteEmail;
    }

    public String getManutenteEmail() {
        return manutenteEmail;
    }

    public void setManutenteEmail(String manutenteEmail) {
        this.manutenteEmail = manutenteEmail;
    }

    public ArrayList<Messaggio> getListaMessaggi() {
        return listaMessaggi;
    }

    public void setListaMessaggi(ArrayList<Messaggio> listaMessaggi) {
        this.listaMessaggi = listaMessaggi;
    }

    public boolean isReadByUtente() {
        return readByUtente;
    }

    public void setReadByUtente(boolean readByUtente) {
        this.readByUtente = readByUtente;
    }

    public boolean isReadByManutente() {
        return readByManutente;
    }

    public void setReadByManutente(boolean readByManutente) {
        this.readByManutente = readByManutente;
    }
}
