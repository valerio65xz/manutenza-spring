package com.project.manutenza;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@CrossOrigin
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
        return object;
    }


    //Reindirizzamento pagina principale
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

}
