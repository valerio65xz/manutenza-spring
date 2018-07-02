package com.project.manutenza;

import com.project.manutenza.entities.Chat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/** Classe Controller per la gestione di chat e messaggi. Vengono prelevate le chat relative ad un ID, ad un utente
 * o ad un manutente. Vengono rimosse se ee solo se sia l'utente che manutente hanno scaricato le chat relative,
 * in tutti e 3 i casi.*/
@Controller
public class MessageController {

    /** Restituisce la chat per quel determinato ID della proposta
     * @param   id   id della proposta
     * @return   la relativa chat. */
    @RequestMapping("/checkMessageById")
    @ResponseBody
    public Chat checkMessageById(@RequestParam("idProposta") int id){

        //ArrayList temporaneo per l'output
        Chat outputChat = null;

        //Scorro la lista di tutte le chat e prelevo solo quella per quell'ID, rimuovendola dalla coda
        for (int i=0; i<ManUtenzaApplication.listaChat.size(); i++){
            if (ManUtenzaApplication.listaChat.get(i).getIdProposta()==id){

                //Prendo la chat da ritornare in output e la rimuovo dalla coda
                outputChat = ManUtenzaApplication.listaChat.get(i);
                ManUtenzaApplication.listaChat.remove(i);

                //Dato che trovo la proposta, inutile continuare il ciclo
                break;
            }
        }

        return outputChat;
    }

    /** Restituisco le chat relative ad un determinato utente
     * @param   utenteEmail   email dell'utente
     * @return   ArrayList contenente le chat relative a quell'utente */
    @RequestMapping("/checkMessageByUtente")
    @ResponseBody
    public ArrayList<Chat> checkMessageByUtente(@RequestParam("utente") String utenteEmail){

        //ArrayList per l'output e per l'eliminazione
        ArrayList<Chat> outputChat = new ArrayList<>();
        ArrayList<Chat> deleteChat = new ArrayList<>();

        //Scorro la lista di tutte le chat e prelevo quelle con la mail di quell'utente
        for (int i=0; i<ManUtenzaApplication.listaChat.size(); i++){

            //Aggiungo alla coda dell'output le chat trovate e imposto il flag di lettura dell'utente
            if (ManUtenzaApplication.listaChat.get(i).getUtenteEmail().equals(utenteEmail)){
                outputChat.add(ManUtenzaApplication.listaChat.get(i));
                ManUtenzaApplication.listaChat.get(i).setReadByUtente(true);
            }

            //Se tale chat è letta sia da utente che manutente, provvedo ad aggiungerla all'arraylist di eliminazione
            if ((ManUtenzaApplication.listaChat.get(i).isReadByUtente())&&(ManUtenzaApplication.listaChat.get(i).isReadByManutente()))
                deleteChat.add(ManUtenzaApplication.listaChat.get(i));

        }

        //Rimuovo all'ultimo se devo rimuovere. Rimuovendo dentro il ciclo for, accorciavo l'array non potendo lavorare correttamente
        ManUtenzaApplication.listaChat.removeAll(deleteChat);

        return outputChat;
    }

    /** Restituisco le chat relative ad un determinato manutente
     * @param   manutenteEmail   email del manutente
     * @return   ArrayList contenente le chat relative a quel manutente */
    @RequestMapping("/checkMessageByManutente")
    @ResponseBody
    public ArrayList<Chat> checkMessageByManutente(@RequestParam("manutente") String manutenteEmail){

        //ArrayList per l'output e per l'eliminazione
        ArrayList<Chat> outputChat = new ArrayList<>();
        ArrayList<Chat> deleteChat = new ArrayList<>();

        //Scorro la lista di tutte le chat e prelevo quelle con la mail di quell'manutente
        for (int i=0; i<ManUtenzaApplication.listaChat.size(); i++){

            //Aggiungo alla coda dell'output le chat trovate e imposto il flag di lettura dell'manutente
            if (ManUtenzaApplication.listaChat.get(i).getManutenteEmail().equals(manutenteEmail)){
                outputChat.add(ManUtenzaApplication.listaChat.get(i));
                ManUtenzaApplication.listaChat.get(i).setReadByManutente(true);
            }

            //Se tale chat è letta sia da utente che manutente, provvedo ad aggiungerla all'arraylist di eliminazione
            if ((ManUtenzaApplication.listaChat.get(i).isReadByManutente())&&(ManUtenzaApplication.listaChat.get(i).isReadByManutente()))
                deleteChat.add(ManUtenzaApplication.listaChat.get(i));

        }

        //Rimuovo all'ultimo se devo rimuovere. Rimuovendo dentro il ciclo for, accorciavo l'array non potendo lavorare correttamente
        ManUtenzaApplication.listaChat.removeAll(deleteChat);

        return outputChat;
    }

}
