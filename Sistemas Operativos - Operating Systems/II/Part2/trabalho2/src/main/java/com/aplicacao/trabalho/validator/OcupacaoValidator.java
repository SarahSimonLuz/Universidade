package com.aplicacao.trabalho.validator;

import com.aplicacao.trabalho.model.Ocupacao;
import com.aplicacao.trabalho.servico.Seguranca_Servico;
import com.aplicacao.trabalho.servico.Utilizador_Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OcupacaoValidator implements Validator
{
    @Override
    public boolean supports(Class<?> aClass) 
    {
        return Ocupacao.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) 
    {
        Ocupacao ocupacao = (Ocupacao) obj;
        
        if (ocupacao.getNome().length() < 3 || ocupacao.getNome().length() > 32) 
        {
            errors.rejectValue("ocupacao", "Size.ocupForm.nome");
        }

    }
    
}
