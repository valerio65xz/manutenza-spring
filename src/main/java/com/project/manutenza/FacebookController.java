package com.project.manutenza;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FacebookController {

    private Facebook facebook;

    //Questo costruttore funziona poiché viene effettuato automaticamente un binding all'API Spring Social Facebook
    //fonte: https://spring.io/guides/gs/accessing-facebook/#scratch
    public FacebookController(Facebook facebook) {
        this.facebook = facebook;
    }

    //Indirizzamento dal template di avvenuta connessione. Devo completare il profilo
    //Model è un oggetto passato da Spring per gestire le comunicazioni MVC
    @RequestMapping("/facebookConnected")
    public String facebookConnected(Model model){

        //Imposto gli attributi per il template
        model.addAttribute("firstName", facebook.userOperations().getUserProfile().getFirstName());
        model.addAttribute("lastName", facebook.userOperations().getUserProfile().getLastName());
        model.addAttribute("email", facebook.userOperations().getUserProfile().getEmail());

        return "editProfile";
    }

    //METODO PER DEBUG
    public String getUserData() {
        String nome = facebook.userOperations().getUserProfile().getFirstName();
        String cognome = facebook.userOperations().getUserProfile().getLastName();
        String email = facebook.userOperations().getUserProfile().getEmail();

        //Ritorno al controller principale i dati raccolti. Se qualcosa è null, significa che c'è stato un errore
        if ((nome==null)||(cognome==null)||(email==null)) return "Errore nell'ottenimento dei dati";
        else return "nome: "+nome+" cognome: "+cognome+" email: "+email;
    }

}
