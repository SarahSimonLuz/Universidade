package com.aplicacao.trabalho.web;

import com.aplicacao.trabalho.model.Utilizador;
import com.aplicacao.trabalho.servico.Utilizador_Servico;
import com.aplicacao.trabalho.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Utilizador_Controller 
{
    @Autowired
    private Utilizador_Servico  u_servico;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("userForm", new Utilizador());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String processRegistrationForm(@ModelAttribute("userForm") Utilizador userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) 
        {
            return "registration";
        }

        u_servico.saveUser(userForm);
        
        return "redirect:/home";
    }
    
    @RequestMapping(value = "/login")
    public String login(Model model, String error, String logout) 
    {
        if (error != null)
        {
            model.addAttribute("error", "Your username and password is invalid.");
        System.out.println("error "+ model.toString());
        }

        if (logout != null)
        {
            model.addAttribute("message", "You have been logged out successfully.");
            System.out.println("logout "+ model.toString());
        }

        return "login";
    }
    
}