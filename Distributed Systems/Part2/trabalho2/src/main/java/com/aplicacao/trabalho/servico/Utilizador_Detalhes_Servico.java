package com.aplicacao.trabalho.servico;

import com.aplicacao.trabalho.model.Utilizador;
import com.aplicacao.trabalho.model.Utilizador_Detalhes;
import com.aplicacao.trabalho.repositorio.Utilizador_Repositorio;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Utilizador_Detalhes_Servico implements UserDetailsService
{
    @Autowired
    Utilizador_Repositorio u_repositorio;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) 
    {
        Optional<Utilizador> utilizador = u_repositorio.findByUsername(username);

        utilizador.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        
        UserDetails userD = utilizador.map(Utilizador_Detalhes::new).get();
        return userD;
    }
    
}
