package com.aplicacao.trabalho.servico;

import com.aplicacao.trabalho.model.Ocupacao;
import com.aplicacao.trabalho.model.Utilizador;
import com.aplicacao.trabalho.model.Utilizador_Detalhes;
import com.aplicacao.trabalho.repositorio.Ocupacao_Repositorio;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class Ocupacao_Servico_Imp 
{
    @Autowired
    private Ocupacao_Repositorio repositorio;
    
    @Autowired
    private Utilizador_Servico  u_servico;
    
    @Autowired
    private UserDetailsService uds;
    
    public List<Ocupacao> getAllOcupacao()
    {
        List<Ocupacao> lista = new ArrayList<>();
        repositorio.findAll()
                .forEach(lista::add);
        return lista;
    }
    
    public List<String> getAllOcupacaoNome()
    {
        List<String> lista_nomes = new ArrayList<>();
        List<Ocupacao> ocupacoes = getAllOcupacao();
        
        for(int i=0; i<ocupacoes.size(); i++)
        {
            lista_nomes.add(ocupacoes.get(i).getNome());
        }
        
        return lista_nomes;
    }
    
    public List<Ocupacao> getOcupacoes(String nome)
    {
        List<Ocupacao> lista = new ArrayList<>();
        repositorio.findByNome(nome)
                .forEach(lista::add);
        
        return lista;
    }

    public Optional<Ocupacao> getOcupacao(Long id)
    {
        
        return repositorio.findById(id);
    }
    
    public void addOcupacao(Ocupacao ocupacao, String username)
    {
        
        Timestamp stamp = new Timestamp(System.currentTimeMillis());  
        ocupacao.setDate(new Date(stamp.getTime()));
        
        Utilizador_Detalhes userDetails = (Utilizador_Detalhes) uds.loadUserByUsername(username);
        Utilizador utilizador = userDetails.getUtilizador();
        
        utilizador.addOcup(ocupacao);
        ocupacao.setUtilizador(utilizador);
        
        repositorio.save(ocupacao);
        u_servico.updateUser(utilizador);
        
    }

    public void updateOcupacao(Ocupacao ocupacao, String nome) 
    {
        repositorio.save(ocupacao);
    }

    public void deleteOcupacao(Long id) 
    {
        //rever isto para meter find by cod_postal
        repositorio.deleteById(id);
    }
}