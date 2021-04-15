package com.aplicacao.trabalho.servico;

import com.aplicacao.trabalho.model.Ocupacao;
import com.aplicacao.trabalho.model.Utilizador;
import com.aplicacao.trabalho.model.Utilizador_Detalhes;
import com.aplicacao.trabalho.repositorio.Utilizador_Repositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Utilizador_Servico
{
    @Autowired
    private Utilizador_Repositorio u_repositorio;
    
    @Autowired
    private UserDetailsService uds;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public void saveUser(Utilizador user) 
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        u_repositorio.save(user);
    }
    
    public void updateUser(Utilizador user) 
    {
        u_repositorio.save(user);
    }

    public Optional<Utilizador> findByUsername(String username) {
        return u_repositorio.findByUsername(username);
    }
    
    public List<Ocupacao> findAllOcup(String username)
    {
        Utilizador_Detalhes userDetails = (Utilizador_Detalhes) uds.loadUserByUsername(username);
        
        Utilizador utilizador = userDetails.getUtilizador();
        
        return utilizador.getOcupacoes();
    }

    public void deleteOcup(String username, Long ocup_id) 
    {
        Utilizador_Detalhes userDetails = (Utilizador_Detalhes) uds.loadUserByUsername(username);
        
        Utilizador utilizador = userDetails.getUtilizador();
        List<Ocupacao> ocupacoes = utilizador.getOcupacoes();
        
        
        for(int i = 0; i<ocupacoes.size(); i++)
        {
            if(ocupacoes.get(i).getOcup_id().equals(ocup_id))
            {
                ocupacoes.remove(i);
            }
        }
        
        updateUser(utilizador);
    }
    
}
