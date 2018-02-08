package com.project.manutenza;

import javax.jms.ConnectionFactory;

import com.project.manutenza.entities.Messaggio;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableJms
public class ChatSender {

    //Faccio l'injection del contesto dell'app, che mi servirà per prelevare il JMS Template
    @Autowired
    private ApplicationContext appContext;

    //Bean che permette la configurazione e connessione alla ConnectionFactory
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    //Serializzazione del messaggio in JSON
    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    //Ogni volta che invio un messaggio, ricarico la pagina stessa per inviare un nuovo messaggio
    @RequestMapping("/sender/sendMessage")
    @ResponseBody
    public String sendMessage(@RequestParam("message") String s) {

        //Prelevo il template JMS tramite il contesto (
        JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);

        // Invio del messaggio. Attualmente controllo con questo metodo che ci sia effettivamente del testo, una stringa
        // Vuota o con soli spazi genera eccezioni di vario tipo. In caso sostituire poi con dei controlli a front-end
        if ((s!=null)&&(StringUtils.hasText(s))){
            jmsTemplate.convertAndSend("chat", s);
            return "Messaggio inviato";
        }
        else return "Messaggio non inviato. Formattazione non valida.";

    }

    //Stesso metodo ma quando invio il messaggio dal web server in JSON
    @RequestMapping("/sender/sendObjectMessage")
    @ResponseBody
    public String sendObjectMessage(@RequestBody Messaggio message) {

        //Prelevo il template JMS tramite il contesto (
        JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);

        // Invio del messaggio. Attualmente controllo con questo metodo che ci sia effettivamente del testo, una stringa
        // Vuota o con soli spazi genera eccezioni di vario tipo. In caso sostituire poi con dei controlli a front-end
        if (message!=null){
            jmsTemplate.convertAndSend("chat", message);
            return "Messaggio inviato";
        }
        else return "Messaggio non inviato. Formattazione non valida.";

    }

}