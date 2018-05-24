package com.project.manutenza;

import com.project.manutenza.entities.Messaggio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class MessageController {

    //Link per ritornare i messaggi di quel mittente e destinatario
    @RequestMapping("/checkMessage")
    @ResponseBody
    public ArrayList<Messaggio> checkMessage(@RequestParam("mittente") String mittente, @RequestParam("destinatario") String destinatario){

        //ArrayList temporaneo per l'output
        ArrayList<Messaggio> outputMessaggi = new ArrayList<>();

        //Scorro la lista di tutti i messaggi e prelevo quelli su mittente e destinatario.
        for (Messaggio message : ManUtenzaApplication.getMessageList()){
            //if (message.getDestinatario().equals(destinatario)) outputMessaggi.add(message);
            if ((message.getDestinatario().equals(destinatario))&&(message.getMittente().equals(mittente))) outputMessaggi.add(message);
        }

        //Faccio la rimozione in un colpo dei messaggi copiati.
        ManUtenzaApplication.getMessageList().removeAll(outputMessaggi);

        return outputMessaggi;
    }

    //Stesso metodo, ma solo per destinatario
    @RequestMapping("/checkMessageForReceiver")
    @ResponseBody
    public ArrayList<Messaggio> checkMessageForReceiver(@RequestParam("destinatario") String destinatario){

        //ArrayList temporaneo per l'output
        ArrayList<Messaggio> outputMessaggi = new ArrayList<>();

        //Scorro la lista di tutti i messaggi e prelevo quelli su destinatario.
        for (Messaggio message : ManUtenzaApplication.getMessageList())
            if (message.getDestinatario().equals(destinatario)) outputMessaggi.add(message);

        //Faccio la rimozione in un colpo dei messaggi copiati.
        ManUtenzaApplication.getMessageList().removeAll(outputMessaggi);

        return outputMessaggi;
    }

}
