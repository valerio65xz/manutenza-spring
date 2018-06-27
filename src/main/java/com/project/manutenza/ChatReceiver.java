package com.project.manutenza;

import com.project.manutenza.entities.Chat;
import com.project.manutenza.entities.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Component
public class ChatReceiver {

    @JmsListener(destination = "chat", containerFactory = "myFactory")
    public void receiveObjectMessage(Chat chat) {

        //Questo flag serve per capire quando è la prima volta che la chat viene presentata. In questo caso, la salvo direttamente
        boolean firstTime=true;

        //Scorro tutte le chat. Quando trovo la chat corrispondente, aggiungo il messaggio all'arrayList
        for (int i=0; i<ManUtenzaApplication.listaChat.size(); i++){
            if ((ManUtenzaApplication.listaChat.get(i).getIdProposta()==chat.getIdProposta())){
                ManUtenzaApplication.listaChat.get(i).getListaMessaggi().addAll(chat.getListaMessaggi());
                firstTime=false;
                System.out.println("Aggiunto alla chat con id: "+ManUtenzaApplication.listaChat.get(i).getIdProposta()+" un nuovo messaggio");
            }
        }

        //Se firstTime=true, aggiungo la chat intera
        if (firstTime){
            ManUtenzaApplication.listaChat.add(chat);
            System.out.println("Aggiunta una nuova chat alla coda con id: "+chat.getIdProposta());
        }

    }

}