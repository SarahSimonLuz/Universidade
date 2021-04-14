package com.aplicacao.trabalho.web;

import com.aplicacao.trabalho.model.Ocupacao;
import com.aplicacao.trabalho.servico.Ocupacao_Servico_Imp;
import com.aplicacao.trabalho.servico.Seguranca_Servico;
import com.aplicacao.trabalho.servico.Utilizador_Servico;
import com.aplicacao.trabalho.validator.OcupacaoValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Ocupacao_Controller 
{
    
    @Autowired
    private Utilizador_Servico  u_servico;

    @Autowired
    private Seguranca_Servico s_servico;
    
    @Autowired
    private Ocupacao_Servico_Imp o_servico;
    
    @Autowired
    private OcupacaoValidator ocupValidator;
        
    @RequestMapping(value="/reg_ocupacao")
    public String displayAddOcupacaoForm(Model model) 
    {
        model.addAttribute("ocupForm", new Ocupacao());
        return "/reg_ocupacao";
    }
    
    @RequestMapping(value = "/reg_ocupacao", method = RequestMethod.POST)
    public String processAddOcupacaoForm(@ModelAttribute("ocupForm") Ocupacao ocupForm, BindingResult bindingResult)
    {
        
        ocupValidator.validate(ocupForm, bindingResult);
        
        if (bindingResult.hasErrors()) 
        {
            return "/reg_ocupacao";
        }
        
        o_servico.addOcupacao(ocupForm, s_servico.findLoggedInUsername());
        
        return "redirect:/ocupacao";
    }
    
    @RequestMapping(value = "/registos_ocupacao")
    public String displayOcupacoesRegistadas(Model model)
    {
        model.addAttribute("title", "Lista dos registos de ocupacao efetuados");
        model.addAttribute("ocupacoes", u_servico.findAllOcup(s_servico.findLoggedInUsername()));
        
        return "/registos_ocupacao";
    }
    
    @RequestMapping(value = "/remover_ocupacao")
    public String displayRemoveOcupForm(Model model)
    {
        model.addAttribute("title", "Eliminar Registo de Ocupacao");
        model.addAttribute("ocupacoes", u_servico.findAllOcup(s_servico.findLoggedInUsername()));
        
        return "/remover_ocupacao";
    }
    
    @RequestMapping(value = "/remover_ocupacao", method = RequestMethod.POST)
    public String processRemoveOcupForm(@RequestParam(required = false) Long[] ocup_ids)
    {
        if(ocup_ids != null)
        {
             for(Long id: ocup_ids)
            {
                u_servico.deleteOcup(s_servico.findLoggedInUsername(), id);
                o_servico.deleteOcupacao(id);
            }
            return "redirect:ocupacao";
        }
        
        return "/remover_ocupacao";
    }
    
    @RequestMapping(value = "/ocupacao")
    public String displayOcupacoes(Model model){

        List <Ocupacao> ocupacoes = o_servico.getAllOcupacao();
        if(ocupacoes!=null)
        {
            model.addAttribute("ocupacoes", ocupacoes);
            return "/ocupacao";

        }
        
        return "/ocupacao";
    }

    @RequestMapping(value ={"/", "/home"})
    public String displayOcupacoesHome(Model model){

        List <Ocupacao> ocupacoes = o_servico.getAllOcupacao();
        if(ocupacoes!=null)
        {
            model.addAttribute("ocupacoes", ocupacoes);
            return "/home";

        }
        
        return "/home";
    }
}
