package com.aplicacao.trabalho.servico;

import com.aplicacao.trabalho.model.Utilizador_Detalhes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Seguranca_Servico
{
    
    public String findLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       
        if (auth.getPrincipal() instanceof Utilizador_Detalhes) {
            return ((Utilizador_Detalhes)auth.getPrincipal()).getUsername();
        }
        return null;
    }

}
 
