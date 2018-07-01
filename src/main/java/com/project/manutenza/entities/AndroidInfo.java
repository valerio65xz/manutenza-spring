package com.project.manutenza.entities;

/**
 * Entità [locale non del DB) custom per gestire la query del client Android. Le informazioni saranno utili al manutente
 * per poter convalidare una proposta di lavoro
 */
public class AndroidInfo {

    /** ID della richiesta relativa alla proposta */
    private int id_richiesta;
    /** Titolo della richiesta relativa alla proposta */
    private String titolo_richiesta;
    /** Categoria della richiesta relativa alla proposta */
    private String categoria_richiesta;
    /** Foto della richiesta relativa alla proposta */
    private String foto_richiesta;
    /** ID della proposta da convalidare */
    private int id_proposta;
    /** Prezzo della proposta da convalidare */
    private float prezzo_proposta;
    /** Nome dell'utente alla quale è stato effettuato il lavoro */
    private String nome_utente;

    /** Costruttore */
    public AndroidInfo() {

    }

    /** Costruttore specificando tutti gli attributi */
    public AndroidInfo(int id_richiesta, String titolo_richiesta, String categoria_richiesta, String foto_richiesta, int id_proposta, float prezzo_proposta, String nome_utente) {
        this.id_richiesta = id_richiesta;
        this.titolo_richiesta = titolo_richiesta;
        this.categoria_richiesta = categoria_richiesta;
        this.foto_richiesta = foto_richiesta;
        this.id_proposta = id_proposta;
        this.prezzo_proposta = prezzo_proposta;
        this.nome_utente = nome_utente;
    }

    /** Metodo get() per l'ID della richiesta
     * @return  l'ID della richiesta */
    public int getId_richiesta() {
        return id_richiesta;
    }

    /** Metodo set() per l'ID della richiesta
     * @param   id_richiesta    l'id della richiesta da impostare */
    public void setId_richiesta(int id_richiesta) {
        this.id_richiesta = id_richiesta;
    }

    /** Metodo get() per il titolo della richiesta
     * @return  il titolo della richiesta */
    public String getTitolo_richiesta() {
        return titolo_richiesta;
    }

    /** Metodo set() per il titolo della richiesta
     * @param   titolo_richiesta    il titolo della richiesta da impostare */
    public void setTitolo_richiesta(String titolo_richiesta) {
        this.titolo_richiesta = titolo_richiesta;
    }

    /** Metodo get() per la categoria della richiesta
     * @return  la categoria della richiesta */
    public String getCategoria_richiesta() {
        return categoria_richiesta;
    }

    /** Metodo set() per la categoria della richiesta
     * @param   categoria_richiesta    la categoria della richiesta da impostare */
    public void setCategoria_richiesta(String categoria_richiesta) {
        this.categoria_richiesta = categoria_richiesta;
    }

    /** Metodo get() per la foto della richiesta
     * @return  la foto della richiesta */
    public String getFoto_richiesta() {
        return foto_richiesta;
    }

    /** Metodo set() per la foto della richiesta
     * @param   foto_richiesta    la foto della richiesta da impostare */
    public void setFoto_richiesta(String foto_richiesta) {
        this.foto_richiesta = foto_richiesta;
    }

    /** Metodo get() per l'ID della proposta
     * @return  l'ID della proposta */
    public int getId_proposta() {
        return id_proposta;
    }

    /** Metodo set() per l'ID della proposta
     * @param   id_proposta    l'id della proposta da impostare */
    public void setId_proposta(int id_proposta) {
        this.id_proposta = id_proposta;
    }

    /** Metodo get() per il prezzo della proposta
     * @return  il prezzo della proposta */
    public float getPrezzo_proposta() {
        return prezzo_proposta;
    }

    /** Metodo set() per il prezzo della proposta
     * @param   prezzo_proposta    il prezzo della proposta da impostare */
    public void setPrezzo_proposta(float prezzo_proposta) {
        this.prezzo_proposta = prezzo_proposta;
    }

    /** Metodo get() per il nome dell'utente
     * @return  il nome dell'utente */
    public String getNome_utente() {
        return nome_utente;
    }

    /** Metodo set() per il nome dell'utente della proposta
     * @param   nome_utente    il nome dell'utente della proposta da impostare */
    public void setNome_utente(String nome_utente) {
        this.nome_utente = nome_utente;
    }
}


