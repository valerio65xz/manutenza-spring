package com.project.manutenza;

import com.project.manutenza.entities.Messaggio;
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

	//Attributo e metodo per i messaggi
	//ArrayList per i messaggi
	private static ArrayList<Messaggio> listaMessaggi = new ArrayList<>();

	//Salvataggio di un messaggio
	public static void saveMessage(Messaggio message){
		listaMessaggi.add(message);
	}

	public static ArrayList<Messaggio> getMessageList(){
		return listaMessaggi;
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

	public static void main(String[] args){
		SpringApplication.run(ManUtenzaApplication.class, args);
	}
}