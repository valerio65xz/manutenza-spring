package com.project.manutenza;

import java.util.ArrayList;

public class MiaStruttura {

    private long id;
    private String nome;
    private float numero;
    private ArrayList<String> indirizzi;

    //Il costruttore vuoto serve per la creazione dell'oggetto dalla struttura JSON inviata dalla richiesta HTTP.
    //Se non c'è, dà errore. Inoltre queste classi devono avere get e set.
    public MiaStruttura() {
    }

    public MiaStruttura(long id, String nome, float numero, ArrayList<String> indirizzi) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.indirizzi = indirizzi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getNumero() {
        return numero;
    }

    public void setNumero(float numero) {
        this.numero = numero;
    }

    public ArrayList<String> getIndirizzi() {
        return indirizzi;
    }

    public void setIndirizzi(ArrayList<String> indirizzi) {
        this.indirizzi = indirizzi;
    }
}
