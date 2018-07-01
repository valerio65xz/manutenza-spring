package com.project.manutenza;

import com.project.manutenza.entities.Chat;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/** Classe Component per la ricezione di messaggi dalla coda di messaggi JMS.
 * Riceve un'oggetto Chat. */
@Component
public class ChatReceiver {

    /** Preleva un oggetto chat dalla coda di messaggi. Una volta prelevato, viene aggiunta alla coda temporanea di chat
     * la chat se è la prima volta che viene presentata. Altrimenti vengono aggiunti i messaggi all'ArrayList di quella
     * relativa chat.
     * @param   chat    la chat ricevuta dalla coda JMS */
    @JmsListener(destination = "chat", containerFactory = "myFactory")
    public void receiveObjectMessage(Chat chat) {

        //Questo flag serve per capire quando è la prima volta che la chat viene presentata. In questo caso, la salvo direttamente
        boolean firstTime=true;

        //Scorro tutte le chat. Quando trovo la chat corrispondente, aggiungo il messaggio all'arrayList
        for (int i=0; i<ManUtenzaApplication.listaChat.size(); i++){
            if ((ManUtenzaApplication.listaChat.get(i).getIdProposta()==chat.getIdProposta())){
                ManUtenzaApplication.listaChat.get(i).getListaMessaggi().addAll(chat.getListaMessaggi());
                firstTime=false;
                System.out.println("Nuovo messaggio aggiunto alla chat: "+ManUtenzaApplication.listaChat.get(i).getIdProposta()+" un nuovo messaggio");
            }
        }

        //Se firstTime=true, aggiungo la chat intera
        if (firstTime){
            ManUtenzaApplication.listaChat.add(chat);
            System.out.println("Aggiunta una nuova chat alla coda con id: "+chat.getIdProposta());
        }

    }

}