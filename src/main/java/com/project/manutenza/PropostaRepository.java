package com.project.manutenza;

import com.project.manutenza.entities.Proposta;
import org.springframework.data.repository.CrudRepository;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Proposta findById(Long id);

}
