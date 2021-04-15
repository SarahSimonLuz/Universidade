package com.aplicacao.trabalho.validator;

import com.aplicacao.trabalho.model.Utilizador;
import com.aplicacao.trabalho.servico.Utilizador_Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator 
{
    @Autowired
    private Utilizador_Servico uti_servico;

    @Override
    public boolean supports(Class<?> aClass) 
    {
        return Utilizador.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) 
    {
        Utilizador user = (Utilizador) obj;
        
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) 
        {
            errors.rejectValue("username", "Size.userForm.username");
        }
        
        if (uti_servico.findByUsername(user.getUsername()).isPresent()) 
        {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        
        if (user.getPassword().length() < 4  || user.getPassword().length() > 32) 
        {
            errors.rejectValue("password", "Passwors incorreta");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) 
        {
            errors.rejectValue("passwordConfirm", "Passwords diferentes");
        }
    }
}
