package com.project.manutenza;

import com.project.manutenza.entities.Proposta;
import org.springframework.data.repository.CrudRepository;

/** Repository per la gestione della proposta nel DB */
public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    /** Ritorna una proposta per un determinato ID
     * @param   id   id della proposta
     * @return   la proposta trovata*/
    Proposta findById(Long id);

}
