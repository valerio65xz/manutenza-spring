package com.project.manutenza;

import com.project.manutenza.entities.Chat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;

/** Classe di avvio principale di Spring */
@SpringBootApplication
public class ManUtenzaApplication extends SpringBootServletInitializer {

	/** ArrayList contente le chat temporanee ancora non scaricate sia dall'utente che dal manutente */
	protected static ArrayList<Chat> listaChat = new ArrayList<>();

	/** Metodo di configurazione */
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

	/** Metodo di avvio */
	public static void main(String[] args){
		SpringApplication.run(ManUtenzaApplication.class, args);
	}


}