package com.project.manutenza;

import com.project.manutenza.entities.Messaggio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;

@SpringBootApplication
public class ManUtenzaApplication extends SpringBootServletInitializer {

	//Attributo e metodo per i messaggi. ArrayList per i messaggi
	private static ArrayList<Messaggio> listaMessaggi = new ArrayList<>();

	//Oggetto persistence repository per salvare nel DB
	private static MessaggioRepository repository;

	//Salvataggio di un messaggio NELLA LISTA
	public static void saveMessage(Messaggio message){
		listaMessaggi.add(message);
	}

	public static ArrayList<Messaggio> getMessageList(){
		return listaMessaggi;
	}

	public static MessaggioRepository getRepository(){
		return repository;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ManUtenzaApplication.class);
	}

	//Abilitare CORS origin per tutta l'applicazione
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
				registry.addMapping(" https://www.facebook.com/**");
			}
		};
	}

	//Non so come far spuntare MessaggioRepository altrove. Per ora lo salvo e basta, e poi lo uso in ChatReceiver
	@Bean
	public CommandLineRunner demo(MessaggioRepository tempRepository) {
		return (args) -> {
			// save a couple of customers
			repository = tempRepository;
		};
	}

	public static void main(String[] args){
		SpringApplication.run(ManUtenzaApplication.class, args);
	}


}