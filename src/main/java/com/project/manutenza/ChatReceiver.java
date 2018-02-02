package com.project.manutenza;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ChatReceiver {

    @JmsListener(destination = "chat", containerFactory = "myFactory")
    public void receiveMessage(String s) {
            System.out.println("Received message: "+s);
    }

}