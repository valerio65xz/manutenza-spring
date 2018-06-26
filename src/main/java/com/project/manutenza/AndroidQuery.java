package com.project.manutenza;

import com.project.manutenza.entities.AndroidInfo;

import java.sql.*;
import java.util.ArrayList;

public class AndroidQuery {

    private Connection connection;
    private int manutente_id;

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

    //Metodo per vedere se esiste quel manutente per quell'email
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

    public ArrayList<AndroidInfo> getInfoAndSave(){
        Statement stmt;
        try {

            //Creo lo statement query per la select
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT richiesta.id AS id_richiesta, richiesta.titolo AS titolo_richiesta, richiesta.categoria AS categoria_richiesta, foto.link AS foto_richiesta, proposta.id AS id_proposta, proposta.prezzo AS prezzo_proposta, utente.nome AS nome_utente"
                    +" FROM richiesta, richiesta_foto, foto, proposta, utente, manutente"
                    +" WHERE utente.id = richiesta.utente_id AND richiesta.id = richiesta_foto.richiesta_id AND richiesta_foto.listafoto_id = foto.id AND richiesta.id = proposta.richiesta_id AND manutente.id="+manutente_id+" AND manutente.id <> utente.id"
                    +" AND proposta.accettato=true AND richiesta.stato='A'" );

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
