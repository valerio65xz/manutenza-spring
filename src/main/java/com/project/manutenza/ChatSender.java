package com.project.manutenza;

import javax.jms.ConnectionFactory;

import com.project.manutenza.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/** Classe Controller per l'invio di messaggi (oggetti Chat) alla coda JMS. Tale classe viene richiamata ogni volta
 * che si vuole inviare un messaggio. */
@Controller
@EnableJms
public class ChatSender {

    /** Contesto dell'applicazione */
    //Faccio l'injection del contesto dell'app, che mi servirà per prelevare il JMS Template
    @Autowired
    private ApplicationContext appContext;

    /** Bean che permette la configurazione e connessione alla ConnectionFactory
     * @param  connectionFactory  la connection factory alla quale verranno inviatti i messaggi
     * @param configurer configuratore di default per il container factory
     * @return la connection factory configurata */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    /** Viene serializzato il messaggio in formato JSON per consentirne l'invio.
     * @return l'oggetto MessageConverter che effettuerà la conversione */
    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /** Riceve la chat da serializzare e inviare alla coda JMS
     * @param   chat   la chat da inviare
     * @return  una stringa che determina se l'oggetto è stato inviato oppure no.*/
    //Metodo che riceve in un body JSON l'oggetto chat. Lo spedisce alla destination factory
    @RequestMapping("/sender/sendObjectMessage")
    @ResponseBody
    public String sendObjectMessage(@RequestBody Chat chat) {

        //Prelevo il template JMS tramite il contesto (
        JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);

        // Invio del messaggio. Controllo che l'oggetto non sia null, altrimenti vi sono eccezioni
        if (chat!=null){
            jmsTemplate.convertAndSend("chat", chat);
            return "Messaggio inviato";
        }
        else return "Messaggio non inviato. Formattazione non valida.";

    }

}