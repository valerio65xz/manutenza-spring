package com.project.manutenza;

import com.project.manutenza.entities.Messaggio;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Component
public class ChatReceiver {

    //Per vecchio metodo quando inviavo solo un messaggio
    /*
    @JmsListener(destination = "chat", containerFactory = "myFactory")
    public void receiveMessage(String s) {
            System.out.println("Received message: "+s);

            //TEMPORANEO
            Messaggio message = new Messaggio("Mittente1", "Destinatario1", s, "Timestamp1");
            ManUtenzaApplication.saveMessage(message);
    }
    */

    @JmsListener(destination = "chat", containerFactory = "myFactory")
    public void receiveObjectMessage(Messaggio message) {
        message.setTimestamp(new Date());
        ManUtenzaApplication.saveMessage(message);
        System.out.println("Received message: "+message.getMessaggio());
        System.out.println("Timestamp: "+message.getTimestamp());

        //Salva nel DB il messaggio
        ManUtenzaApplication.getRepository().save(message);
    }

}