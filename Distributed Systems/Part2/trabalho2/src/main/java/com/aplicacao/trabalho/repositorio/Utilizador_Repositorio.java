package com.aplicacao.trabalho.repositorio;

import com.aplicacao.trabalho.model.Ocupacao;
import com.aplicacao.trabalho.model.Utilizador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Utilizador_Repositorio extends JpaRepository<Utilizador, Long> 
{
    Optional<Utilizador> findByUsername(String username);
    //List<Ocupacao> findByNome(String nome);
}
