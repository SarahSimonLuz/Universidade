package com.aplicacao.trabalho.repositorio;

import com.aplicacao.trabalho.model.Ocupacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

//para meter isto a funcionar com o cod postal e o mesmo sendo um String, alterar os tipos. ficando, CrudRepository<Ocupacao, String>
public interface Ocupacao_Repositorio extends JpaRepository<Ocupacao, Long> 
{
    List<Ocupacao> findByNome(String nome);
    //List<Ocupacao> findByUsername_id();
}
