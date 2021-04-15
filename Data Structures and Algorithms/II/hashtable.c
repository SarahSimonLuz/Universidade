#include "hashtable.h"
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <stdio.h>

#define MAX_TAMANHO_PAISES 1361
#define PRIMO_PAIS 7

//cria uma hashtable e devolve o pointer para a mesma hashtable
struct paises *criar_hashTable()
{
    struct paises *novo = malloc(sizeof(struct paises));
    if(novo == NULL)
        return NULL;

    for(int i=0; i < MAX_TAMANHO_PAISES; i++)
        novo->tabela[i] = NULL;

    return novo;
}

//recebe uma hashtable e depois da free
void free_hashtable(struct paises *ht)
{
	for(int i = 0; i< MAX_TAMANHO_PAISES; i++)
		free(ht->tabela[i]);
	free(ht);
}

//recebe um pointer de uma hashtable e o codigo do pais que esta' a procurar. se o pais for encontrado devolve o indice, caso contrario devolve -1
int procurar(struct paises *ht, char *codigo)
{
	int indice = hashCode1(codigo, MAX_TAMANHO_PAISES);
	int contador = 1;

	while(ht->tabela[indice]!= NULL)
	{
		if(strcmp(ht->tabela[indice]->idpais,codigo) == 0)
			return indice;
		

		indice = double_hashing(contador, codigo, MAX_TAMANHO_PAISES, PRIMO_PAIS);
		contador++;
	}

	return -1;
}

//recebe um pointer de uma hashtable, o codigo do pais que quer inserir e informacao que tem de adicionar. devolve o indice onde o pais foi adicionado
int inserir(struct paises *ht, char *codigo, int total, int ativos, int completou, int abandonou)
{
	int indice = hashCode1(codigo,MAX_TAMANHO_PAISES);
	int contador = 1;

	while(ht->tabela[indice] != NULL && strcmp(ht->tabela[indice]->idpais,codigo) != 0)
	{
		indice = double_hashing(contador, codigo, MAX_TAMANHO_PAISES, PRIMO_PAIS);
		contador++;
	}

	struct pais *temp = malloc(sizeof(struct pais));

	strcpy(temp->idpais, codigo);
	temp->n_total = total;
	temp->n_ativos = ativos;
	temp->n_completou = completou;
	temp->n_abandonou = abandonou;
	temp->indice = indice;

	ht->tabela[indice] = temp;
	
	return indice;
}

// Algoritmo de hash - djb2
//Retirado de http://www.cse.yorku.ca/~oz/hash.html
unsigned int hashCode1(char *codigo, int tamanho)
{
	unsigned int hash = 5381;
	int c;

	while ((c = *codigo++))
		hash = ((hash << 5) + hash) + c;

	return hash % tamanho;
}

int hashCode2(char *codigo, int tamanho, int primo)
{
	return primo - ((hashCode1(codigo, tamanho)) % primo);
}

//em caso de colisao, double hashing do identificador
int double_hashing(int contador, char *identificador, int tamanho, int primo)
{
	return (hashCode1(identificador, tamanho) + contador * hashCode2(identificador, tamanho, primo)) % tamanho;
}

//obter os dados de um dado pais com o codigo do mesmo
void obter_dados(struct paises *ht, char *codigo)
{
	int indice = procurar(ht, codigo);

	if (indice == -1 || ht->tabela[indice]->n_total == 0)
		printf("+ sem dados sobre %s\n", codigo);

	else
		printf("+ %s - correntes: %d, diplomados: %d, abandonaram: %d, total: %d\n", codigo, ht->tabela[indice]->n_ativos, ht->tabela[indice]->n_completou, ht->tabela[indice]->n_abandonou, ht->tabela[indice]->n_total);
}	

//atualiza a informacao de um pais depois de terem sido feitas operacoes. caso o pais ainda nao tenha sido inserido, e' inserido
void atualizar_info_paises(struct paises *ht, char *codigo, int modo)
{
	int indice = procurar(ht, codigo);

	if(indice == -1)
		indice = inserir(ht, codigo,0,0,0,0);

	switch(modo)
	{
		case 0:
			ht->tabela[indice]->n_total += 1;
			ht->tabela[indice]->n_ativos += 1;
			
			break;
		//remover
		case 5:
			ht->tabela[indice]->n_total -= 1;
			ht->tabela[indice]->n_ativos -= 1;
			
			break;
		//completou
		case 6:
			ht->tabela[indice]->n_ativos -= 1;
			ht->tabela[indice]->n_completou += 1;
			break;
		//abandonou
		case 7:
			ht->tabela[indice]->n_ativos -= 1;
			ht->tabela[indice]->n_abandonou += 1;
			break;

		default:
			break;
	}
}
