package com.aplicacao.trabalho.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ocupacao")
public class Ocupacao implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ocup_id")
    private Long ocup_id;

    private String latitude;
    private String longitude;
    
    private String nome;
    private String nivel_ocupacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
   // private Long data; //para a data um time stamp
    
    //varias instancias desta entity estão mapped para um instancia do utilizador
    @ManyToOne
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;

    public Ocupacao(Long ocup_id, String longitude, String latitude, String nome, 
            String nivel_ocupacao, Utilizador utilizador) {
        this.ocup_id = ocup_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nome = nome;
        this.nivel_ocupacao = nivel_ocupacao;
        this.utilizador = utilizador;
    }

    public Ocupacao() {}
    

    public Long getOcup_id() {
        return ocup_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getNome() {
        return nome;
    }

    public String getNivel_ocupacao() {
        return nivel_ocupacao;
    }
    
    public Date getDate() {
        return date;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setOcup_id(Long ocup_id) {
        this.ocup_id = ocup_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNivel_ocupacao(String nivel_ocupacao) {
        this.nivel_ocupacao = nivel_ocupacao;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }


    
}
