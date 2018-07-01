package com.project.manutenza;

import com.project.manutenza.entities.AndroidInfo;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe che mi crea la connessione al DB ed esegue la query principale per restituire l'elenco di proposte attive
 * per quel manutente al client Android.
 */
public class AndroidQuery {

    /** Oggetto per inizializzare e terminare la connessione con il database */
    private Connection connection;
    /** ID del manutente relativo alle proposte */
    private int manutente_id;

    /** Nel costruttore inizializzo la connessione al database
     * @return  una stringa che identifica il successo o il fallimento della connessione */
    public String connectToDB(){
        connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manutenza2","postgres", "root");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: "+e.getMessage();
        }
    }

    /** Controlla se esiste o meno nel database un manutente per quella mail.
     * @param   email   l'email del manutente
     * @return  true se esiste il manutente, false altrimenti.
     * */
    public boolean manutenteExists(String email){

        Statement stmt;
        manutente_id=-1;
        try {

            //Creo lo statement query per la select
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT manutente.id"
                    +" FROM utente, manutente"
                    +" WHERE utente.id = manutente.id AND utente.email='"+email+"'" );

            //Vedo se esiste il record
            while ( rs.next() ) {
                manutente_id = rs.getInt("id");
            }

            //Chiudo le connessioni al DB
            rs.close();
            stmt.close();

            //Controllo il risultato
            if (manutente_id==-1) return false;
            else return true;

        } catch ( Exception e ) {
            System.out.println("ERRORE: "+e.getMessage());
            return false;
        }
    }

    /**  Prelevo le informazioni relative all'elenco di proposte per quel manutente, effettuando la relativa query al database.
     * @return  ArrayList contentente l'elenco proposte di quel manutente.
     * Se non vi è alcuna proposta, viene ritornato un array null*/
    public ArrayList<AndroidInfo> getInfoAndSave(){
        Statement stmt;
        try {

            //Creo lo statement query per la select
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT richiesta.id AS id_richiesta, richiesta.titolo AS titolo_richiesta, richiesta.categoria AS categoria_richiesta, foto.link AS foto_richiesta, proposta.id AS id_proposta, proposta.prezzo AS prezzo_proposta, utente.nome AS nome_utente"
                    +" FROM richiesta, richiesta_foto, foto, proposta, utente"
                    +" WHERE utente.id = richiesta.utente_id AND richiesta.id = richiesta_foto.richiesta_id AND richiesta_foto.listafoto_id = foto.id AND richiesta.id = proposta.richiesta_id AND proposta.manutente_id="+manutente_id
                    +" AND richiesta.stato='A'" );

            //Creo l'array dell'oggetto Android che conterrà le informazioni
            ArrayList<AndroidInfo> androidInfos = new ArrayList<>();

            //Per ogni record se esiste
            while ( rs.next() ) {

                //Prelevo le informazioni
                int id_richiesta = rs.getInt("id_richiesta");
                String  titolo_richiesta = rs.getString("titolo_richiesta");
                String categoria_richiesta  = rs.getString("categoria_richiesta");
                String foto_richiesta = rs.getString("foto_richiesta");
                int id_proposta = rs.getInt("id_proposta");
                float prezzo_proposta = rs.getFloat("prezzo_proposta");
                String nome_utente = rs.getString("nome_utente");

                //E creo un oggetto AndroidInfo e lo salvo nell'array
                AndroidInfo info = new AndroidInfo(id_richiesta, titolo_richiesta, categoria_richiesta, foto_richiesta, id_proposta, prezzo_proposta, nome_utente);
                androidInfos.add(info);
            }

            //Chiudo le connessioni al DB
            rs.close();
            stmt.close();
            connection.close();

            //Ritorno l'arraylist al metodo del controller principale da poi fornire tramite Web Service
            return androidInfos;

        } catch ( Exception e ) {
            System.out.println("ERRORE: "+e.getMessage());
            return null;
        }
    }

    /** Modifica lo stato della richesta da 'A' a 'C' dopo l'effettivo pagamento da ManUtenza al relativo manutente.
     * @param  id  l'id della richiesta
     * @return  true se la modifica è andata a buon fine, false altrimenti.*/
    //Metodo per modificare lo stato della richiesta dopo un pagamento
    public boolean completeRequest(int id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE richiesta" +
                            " SET stato='C'" +
                            " FROM proposta" +
                            " WHERE proposta.richiesta_id = richiesta.id AND proposta.id = "+id
            );

            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
