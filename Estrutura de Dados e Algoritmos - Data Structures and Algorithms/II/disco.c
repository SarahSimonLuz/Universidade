#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include<stdbool.h>

#include "disco.h"

#define MAX_TAMANHO_PAISES 1361
#define MAX_TAMANHO_ALUNOS 20000003
#define PRIMO_PAIS 7
#define PRIMO_ALUNO 3

//aloca o espaco necessario para uma struct aluno e devolve o pointer da memoria 
struct aluno *criar_aluno()
{
	struct aluno *temp = malloc( sizeof(struct aluno));
	
	strcpy(temp->idaluno, "");
	strcpy(temp->idpais, "");
	temp->ativo = false;
	temp->concluido = false;
	temp->abandonou = false;
	temp->usado = false;
	return temp;
}

//dado um pointer de uma struct aluno, vai definir o identificador, codigo do pais e estabelecer as flags.
//devolve o pointer para a struct aluno
struct aluno *adicionar_info(struct aluno *temp, char *identificador, char *codigo)
{
	strcpy(temp->idaluno, identificador);
	strcpy(temp->idpais, codigo);
	temp->ativo = true;
	temp->concluido = false;
	temp->abandonou = false;
	temp->usado = true;
	return temp;
}

//abre o ficheiro, caso nao exista um, vai cria-lo 
FILE *openFile(const char *filename)
{
	FILE *temp = fopen(filename, "rb+");
	
	if(temp == NULL)
	{
		temp = fopen(filename, "wb+");
	}
	
	return temp;
}

//le do ficheiro a informacao de uma struct aluno, a sua posicao e'calculada com o auxilio do indice.
//devolve 0 se nenhum aluno foi lido com sucesso, devolve 1 quando um aluno e'lido com sucesso
int ler_aluno(FILE *ficheiro,int indice, struct aluno *temp )
{
	int ret;
	fseek(ficheiro, indice*sizeof(struct aluno), SEEK_SET);
	ret = fread(temp, sizeof(struct aluno), 1, ficheiro);

	return ret;
}

//escreve no ficheiro a informacao de um aluno, a sua posicao e' calculada com o auxilio de um indice
void escrever_aluno(FILE *ficheiro,int indice, struct aluno *temp)
{
	fseek(ficheiro, indice*sizeof(struct aluno), SEEK_SET);
	fwrite(temp, sizeof(struct aluno), 1, ficheiro);
}

//vai procurar no ficheiro a posicao da struct aluno dado o seu identificador.
//se nao encontrar devolve -1, se encontrar devolve o indice
int procurar_mem(FILE *ficheiro, struct aluno *temp, char *identificador)
{
	//obtem o hashcode do identificador que está a tentar encontrar 
	int indice = hashCode1(identificador, MAX_TAMANHO_ALUNOS);
	int contador = 1;

	ler_aluno(ficheiro, indice, temp);

	//procurar aluno
	while(temp->usado == true)
	{
		//compara o identificador encontrado com o identificador dado para ver se encontrou o aluno procurado
		if(strcmp(temp->idaluno, identificador)==0)
			return indice;

		//calcular o novo indice 
		indice = double_hashing(contador, identificador,MAX_TAMANHO_ALUNOS,PRIMO_ALUNO);
		contador++;

		ler_aluno(ficheiro, indice, temp);
	}

	//nao foi encontrado aluno
	return -1;
}

//inserir aluno novo, criando um indice a partir do seu identificador 
void insere_aluno(FILE *ficheiro, struct paises *ht, char *identificador, char *codigo)
{	
	struct aluno *temp = criar_aluno();
	int indice = hashCode1(identificador, MAX_TAMANHO_ALUNOS);
	int contador = 1;
	int ret;

	ret = ler_aluno(ficheiro, indice, temp);

	//encontrar uma vaga para introduzir o aluno, ou ver se ja' existe
	while(temp->usado == true && ret!=0)
	{
		//ja existiu um aluno nesta posicao, mas foi removido, ou seja, foi encontrada uma vaga
		if(temp->ativo==false && temp->concluido==false && temp->abandonou==false && strcmp(temp->idaluno, identificador)== 0)
		{
			//inserir novo aluno
			atualizar_info_paises(ht, codigo, 0);
			escrever_aluno(ficheiro,indice,adicionar_info(temp,identificador,codigo));
			free(temp);
			return;
		}

		//ja' existe um aluno com este identificador
		if(strcmp(temp->idaluno, identificador)==0)
		{
			printf("+ estudante %s existe\n", identificador);
			free(temp);
			return;
		}

		//calcular o novo indice
		indice = double_hashing(contador, identificador,MAX_TAMANHO_ALUNOS,PRIMO_ALUNO);
		contador++;

		ret = ler_aluno(ficheiro, indice, temp);

	}

	//inserir novo aluno
	atualizar_info_paises(ht, codigo, 0);
	escrever_aluno(ficheiro,indice,adicionar_info(temp,identificador,codigo));

	free(temp);
}

//operacoes: 5, remove o aluno; 6, marca o aluno como terminou; 7, marca o aluno como abandonou
void fazer_operacao(FILE *ficheiro, struct paises *ht, int indice, struct aluno *temp, int modo)
{
	temp->ativo = false;
	switch(modo)
	{
		//remover
		case 5: 
			temp->concluido = false;
			temp->abandonou = false;
			break;

		//terminou
		case 6:
			temp->concluido = true;
			break;

		//abandonou
		case 7:
			temp->abandonou = true;
			break;

		default:
			break;
	}
	
	atualizar_info_paises(ht, temp->idpais, modo);
	escrever_aluno(ficheiro, indice, temp);
}

//ve que operacao foi pedida e exetua o pedido
void operacao_aluno(FILE *ficheiro, struct paises *ht, char *identificador, int modo)
{
	struct aluno* temp = criar_aluno();
	int indice = procurar_mem(ficheiro, temp, identificador);

	if(indice > 0)
	{
		//aluno esta' desativado
		if(temp->concluido == true)
		{
			printf("+ estudante %s terminou\n", identificador);
			free(temp);
			return;
		}

		//aluno esta' desativado
		if(temp->abandonou == true)
		{
			printf("+ estudante %s abandonou\n", identificador);
			free(temp);
			return;
		}
	
		//verifica que operacao ira' correr
		if(temp->ativo == true)
		{
			fazer_operacao(ficheiro, ht, indice, temp, modo);
			free(temp);
			return;
		}
	}

	printf("+ estudante %s inexistente\n", identificador);
	free(temp);
}

//vai "buscar" ao ficheiro a informacao dos paises
void obter_info_paises(FILE *ficheiro, struct paises *ht)
{
	struct pais *aux = malloc(sizeof(struct pais));

	while(fread(aux,sizeof(struct pais),1,ficheiro) != 0) 
	{
		struct pais *temp = malloc(sizeof(struct pais));

		strcpy(temp->idpais, aux->idpais);
		temp->n_total = aux->n_total;
		temp->n_ativos = aux->n_ativos;
		temp->n_completou = aux->n_completou;
		temp->n_abandonou = aux->n_abandonou;
		temp->indice = aux->indice;

		ht->tabela[temp->indice] = temp;
	}

	free(aux);
}

//vai atualizar a informacao que esta' guardada em disco com as alteracoes feitas durante a execucao 
void atualizar_BD(FILE *ficheiro, struct paises *ht)
{
	int contador = 0;

	for(int i = 0; i < MAX_TAMANHO_PAISES; i++)
	{
		if(ht->tabela[i] != NULL)
		{
			fseek(ficheiro, sizeof(struct pais) * contador, SEEK_SET);
			fwrite(ht->tabela[i], sizeof(struct pais), 1, ficheiro);
			contador++;
		}
	}

	free_hashtable(ht);
}