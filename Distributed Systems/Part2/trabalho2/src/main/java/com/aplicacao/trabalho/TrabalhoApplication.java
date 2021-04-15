package com.aplicacao.trabalho;

import com.aplicacao.trabalho.repositorio.Ocupacao_Repositorio;
import com.aplicacao.trabalho.repositorio.Utilizador_Repositorio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {Utilizador_Repositorio.class, Ocupacao_Repositorio.class})
public class TrabalhoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrabalhoApplication.class, args);
	}

}
