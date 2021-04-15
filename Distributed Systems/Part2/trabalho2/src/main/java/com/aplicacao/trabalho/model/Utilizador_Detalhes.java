package com.aplicacao.trabalho.model;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Utilizador_Detalhes implements UserDetails 
{
    private Long utilizador_id;
    private String username;
    private String password;
    private String cargo;
    private List<Ocupacao> ocupacoes;

    public Utilizador_Detalhes(Utilizador utilizador) 
    {
        this.utilizador_id = utilizador.getUtilizador_id();
        this.username = utilizador.getUsername();
        this.password = utilizador.getPassword();
        this.ocupacoes = utilizador.getOcupacoes();
        this.cargo = utilizador.getCargo();
    }

    public Utilizador getUtilizador()
    {
        Utilizador utilizador = new Utilizador(this.utilizador_id,this.username,this.password,this.cargo,this.ocupacoes);
          return utilizador;      
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return null;   
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
