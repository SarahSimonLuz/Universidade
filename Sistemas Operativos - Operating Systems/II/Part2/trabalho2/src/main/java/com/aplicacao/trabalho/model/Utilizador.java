/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aplicacao.trabalho.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "utilizador")
public class Utilizador 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "utilizador_id")
    private Long utilizador_id;
    
    @Column(name = "username")
    private String username;
    private String password;
    private String cargo = "user";
    
    @Transient
    private String passwordConfirm;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="utilizador",cascade = CascadeType.ALL)
    private List<Ocupacao> ocupacoes;

    
    public Utilizador(Long utilizador_id, String username, String password, String passwordConfirm, List<Ocupacao> ocupacoes) {
        this.utilizador_id = utilizador_id;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.ocupacoes = ocupacoes;
    }

    
    public Utilizador() {}
    
    
    public void addOcup(Ocupacao ocupacao)
    {
        ocupacoes.add(ocupacao);
    }

  
    public Long getUtilizador_id() {
        return utilizador_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

        public String getCargo() {
        return cargo;
    }

    public List<Ocupacao> getOcupacoes() {
        return ocupacoes;
    }

    public void setUtilizador_id(Long utilizador_id) {
        this.utilizador_id = utilizador_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOcupacoes(List<Ocupacao> ocupacoes) {
        this.ocupacoes = ocupacoes;
    }
    
     public void setCargo(String cargo) {
        this.cargo = cargo;
    }
     
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    @Override
    public String toString() 
    {
        return "Utilizador{" + "utilizador_id=" + utilizador_id + ", username=" + username + ", password=" + password + ", cargo=" + cargo + ", ocupacoes=" + ocupacoes + '}';
    }
    

}

