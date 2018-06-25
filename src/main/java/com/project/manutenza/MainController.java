package com.project.manutenza;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.project.manutenza.entities.AndroidInfo;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class MainController {

    //Prova per Web Service RESTful
    //Consente un assemblamento della stringa su un parametro
    private static final String template = "Hello, %s!";

    //Consente di stabilire un ID richiesta, che si autoincrementa ad ogni richiesta HTTP
    private final AtomicLong counter = new AtomicLong();


    /* AREA RISERVATA AL DEBUG WEBSERIVCE REST
    //@RequestParam si prende i parametri GET dal link, e POST dall'header
    @RequestMapping("/greeting")
    @ResponseBody
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
        //return new Greeting(1, "prova");
    }

    //Costruisco il greeting dall'oggetto JSON Passato
    @RequestMapping("/JGreeting")
    @ResponseBody
    public String greetingFromJSON(@RequestBody Greeting greeting) {
        return greeting.getContent();
    }
    */

    //Costruisco l'oggetto e ne ritorno la sua codifica in JSON, per poi fare il test inviandola tramite POST HTTP
    @RequestMapping("/toJSON")
    @ResponseBody
    public MiaStruttura toJSON() {
        ArrayList<String> prova = new ArrayList<>();
        prova.add("numero uno");
        prova.add("secondo in carica");
        prova.add("finiamo col terzo");
        return new MiaStruttura(counter.incrementAndGet(), "Pinco Pallin0", (float)6.66, prova);
    }

    //L'oggetto arriva dal POST HTTP e decodificato mediante annotazione @RequestBody.
    //Testo l'oggetto prelevando il primo elemento dell'attributo ArrayList
    @RequestMapping("/toObject")
    @ResponseBody
    public String toObject(@RequestBody MiaStruttura miaStruttura) {
        return miaStruttura.getIndirizzi().get(1);
    }
    @RequestMapping("/ajaxTest")
    @ResponseBody
    public Object ajaxTest(@RequestBody Object object) {
        System.out.println("ci entro!");
        //return "home";
        return object;
    }

    @RequestMapping("/ajaxTestPost")
    public String ajaxTestPost(@RequestParam("scope") String risposta){
        return "home";
    }

    //Reindirizzamento pagina principale. Prendo dei dati PayPal grezzi
    @RequestMapping("/")
    public String home(){
        return "home";
    }

    //Reindirizzamento pagina paypal
    @RequestMapping("/paypal")
    public String paypal(){
        return "paypal";
    }

    //Reindirizzamento verifica partita IVA
    @RequestMapping("/IVA")
    public String partitaIVA(){
        return "partitaIVA";
    }

    //Processamento partita IVA
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

    //Indirizzamento al template di invio messaggio
    @RequestMapping("/sender")
    public String sender(){
        return "sender";
    }

    //Richiesta al DB della query per spedire ad Android
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

    //Convalida della prestazione tramite QR CODE. Manutenza darà i soldi al manutente relativo
    @RequestMapping("/validateJob")
    @ResponseBody
    public String validateJob(){

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
            return "Errore nel prelievo del token.";
        }

        //Sto casino per prendermi il token
        JSONObject jsonObject = new JSONObject(response);
        String body = jsonObject.getString("body");
        JSONObject bodyJson = new JSONObject(body);
        String accessToken = bodyJson.getString("access_token");

        //Adesso con la seconda chiamata Unirest effettuo in effetti il pagamento al manutente
        //Stesso discorso: su postman il token va su Authorization e si mette "bearer token"
        //Sostituire SOLDI E EMAIL
        try {
            response = Unirest.post("https://api.sandbox.paypal.com/v1/payments/payouts")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer "+accessToken)
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "32f3dff3-b92b-48c4-b639-2fa950fe76ff")
                    .body("{\n  \"sender_batch_header\": {\n  \"email_subject\": \"Pagamento di lavoro concluso.\"\n  },\n  \"items\": [\n  {\n    \"recipient_type\": \"EMAIL\",\n    \"amount\": {\n    \"value\": \"10.01\",\n    \"currency\": \"EUR\"\n    },\n    \"note\": \"Grazie per aver utilizzato manutenza!\",\n    \"receiver\": \"manutente@manutenza.it\"\n  }\n  ]\n}")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return "Errore nel pagamento.";
        }

        return "success";

    }
}
