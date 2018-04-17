package com.project.manutenza;

import com.project.manutenza.entities.Messaggio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//Interfaccia per salvaraggio nel DB
public interface MessaggioRepository extends CrudRepository<Messaggio, Long> {

    //Qui posso definire dei metodi di ricerca, ad esempio per destinatario
    //ATTENZIONE: IL NOME DEL METODO DEVE CORRISPONDERE A QUELLO DELL'ATTRIBUTO CHE DEVE TROVARE!
    List<Messaggio> findByDestinatario(String destinatario);

}