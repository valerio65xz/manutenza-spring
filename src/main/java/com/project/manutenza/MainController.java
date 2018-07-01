package com.project.manutenza;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.project.manutenza.entities.AndroidInfo;
import com.project.manutenza.entities.Proposta;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/** Controller principale del Web Service Spring */
@Controller
public class MainController {

    /** La repository per manipolare la propsta */
    @Autowired
    private PropostaRepository propostaRepository;

    /** ID della proposta da convalidare per evitare pagaenti duplicati */
    private long idPropostaDuplicata = -1;

    /** Reindirizzamento pagina principale.
     * @return il template home */
    @RequestMapping("/")
    public String home(){
        return "home";
    }

    /** Processamento partita IVA, tramite il codice dello stato e della partita IVA, ne si determina l'esistenza o meno.
     * @param   memberStateCode   il codice dello stato (sostanzialmente "IT")
     * @param    number   numero della partita IVA
     * @return   true se la partita IVA esiste, false se non esiste. In caso di errore, il messaggio dell'errore.*/
    @RequestMapping("/IVA/check")
    @ResponseBody
    public String checkPartitaIVA(@RequestParam("memberStateCode") String memberStateCode, @RequestParam("number") String number){

        //Chiamata Unirest presa grazie a Postman. Aggiunta la dependency nel POM
        HttpResponse<String> response;
        try {
            response = Unirest.post("http://ec.europa.eu/taxation_customs/vies/vatResponse.html")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Cache-Control", "no-cache")
                    .body("memberStateCode="+memberStateCode+"&number="+number)
                    .asString();

            //Debug per visualizzare la pagina
            //return response.getBody();

            //In questo modo ottengo il flag se la partita IVA è valida o meno. Se valida, si troverà la stringa "Yes, valid VAT number"
            //Dentro il body, Altrimenti, non sarà valida.
            return ""+response.getBody().contains("Yes, valid VAT number");

        } catch (UnirestException | NullPointerException e) {
            e.printStackTrace();
            return ""+e.getMessage();
        }
    }

    /** Esecuzione della query per prelevare l'elenco delle proposte per quel relativo manutente
     * @param   email   email del manutente
     * @return   l'ArrayList contenente l'elenco delle proposte */
    @RequestMapping("/androidQuery")
    @ResponseBody
    public ArrayList<AndroidInfo> androidQuery(@RequestParam("email") String email){

        //Connetto al DB
        AndroidQuery query = new AndroidQuery();
        String response = query.connectToDB();

        //Controllo se mi sono connesso, e allora cerco il manutente
        if (response.equals("success")){

            //Controllo allora se esiste quel manutente per quella mail, e se si ne prendo le info JSON. Altrimenti ritorno null
            if (query.manutenteExists(email)) return query.getInfoAndSave();
            else return null;
        }
        else return null;

    }

    /** Convalida di una proposta dopo la lettura del QRCode dell'utente. Il lavoro viene definito "completato", pertanto
     * viene pagato il mauntente per il prezzo del lavoro e viene aggiornato il DB impostando la richiesta di quella
     * proposta come completata.
     * @param   id   id della proposta da convalidare
     * @param   email   email del manutente da pagare
     * @return   "success" in caso di successo, altrimenti il messaggio di errore */
    @RequestMapping("/validateJob")
    @ResponseBody
    public String validateJob(@RequestParam("id") Long id, @RequestParam("email") String email){

        //Cerco nel DB se ho una proposta con l'ID ricevuto
        Proposta proposta = propostaRepository.findById(id);
        if (proposta==null){
            System.out.println("Non esiste alcuna proposta con id: "+id);
            return "Non esiste alcuna proposta con id: "+id;
        }
        else System.out.println("Proposta ID: "+proposta.getId()+" Prezzo: "+proposta.getPrezzo());

        //Controllo se ho già eseguito il pagamento per tale proposta. In tal caso ritorno success. Altrimenti cambio ID
        if (idPropostaDuplicata == proposta.getId()) return "success";
        else idPropostaDuplicata =  proposta.getId();

        //Conversione double in una stringa con . come virgola e 2 cifre decimali. Formato PayPal di pagamento
        String price = String.format ("%.2f", proposta.getPrezzo());
        price=price.replace(',', '.');

        //Tramite la prima chiamata Unirest, ottengo l'access token per fare il pagamento
        //PER GENERARE L'HEADER AUTHORIZATION, BISOGNA FARE LA RICHIESTA MANUALE SU POSTMAN E SELEZIONARE
        //AUTHORIZATION = BASIC AUTH
        HttpResponse<String> response = null;
        try {
            response = Unirest.post("https://api.sandbox.paypal.com/v1/oauth2/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic QWRxOFJtTzh1WExNdHdscFZRWmFtMjUwT3I5aXZGV0JCZjBaRUNpV2Q4NmxFUzRTZ1VlMDhBQ1JvSm1qalZHX2lDZVZiSXFzRW5CYW9nZGw6RU9RM01qaHNBZjA1RHNDNk8zSmVaTE1xMWVrSndybGJoRm9mc2ZRN2tpR0NPc2JZOElOYTlHTW04VDJ2Z0p0VGdMMVpDRUVlQlZLYzhXbF8=")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "559955a3-b37d-4b44-9bfe-fc04db34355b")
                    .body("grant_type=client_credentials")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            System.out.println("Errore nel prelievo del token.");
            return "Errore nel prelievo del token.";
        }

        //Sto casino per prendermi il token
        JSONObject jsonObject = new JSONObject(response);
        String body = jsonObject.getString("body");
        JSONObject bodyJson = new JSONObject(body);
        String accessToken = bodyJson.getString("access_token");

        //Adesso con la seconda chiamata Unirest effettuo in effetti il pagamento al manutente
        //Stesso discorso: su postman il token va su Authorization e si mette "bearer token"
        try {
            response = Unirest.post("https://api.sandbox.paypal.com/v1/payments/payouts")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer "+accessToken)
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "32f3dff3-b92b-48c4-b639-2fa950fe76ff")
                    .body("{\n  \"sender_batch_header\": {\n  \"email_subject\": \"Pagamento di lavoro concluso.\"\n  },\n  \"items\": [\n  {\n    \"recipient_type\": \"EMAIL\",\n    \"amount\": {\n    \"value\": \""+price+"\",\n    \"currency\": \"EUR\"\n    },\n    \"note\": \"Grazie per aver utilizzato manutenza!\",\n    \"receiver\": \""+email+"\"\n  }\n  ]\n}")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            System.out.println("Errore nel pagamento.");
            return "Errore nel pagamento.";
        }

        //Infine effettuo la query al DB per aggiornare lo stato della richiesta
        AndroidQuery query = new AndroidQuery();
        String responseDB = query.connectToDB();

        //Controllo se mi sono connesso, e allora effettuo l'update. In caso di false o di non success, vi sarà errore
        if (responseDB.equals("success")){
            if (!query.completeRequest(proposta.getId().intValue())) return "Errore nel salvataggio nel DB. Il pagamento però è completato";
        }
        else return "Errore nel salvataggio nel DB. Il pagamento però è completato";

        //Se tutto è andato per il verso giusto, avrò un success
        return "success";

    }
}
