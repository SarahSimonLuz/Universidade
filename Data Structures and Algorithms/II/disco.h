#include <stdio.h>
#include <string.h>

#include "hashtable.h"

struct aluno
{
	char idaluno[7];
	char idpais[3];
	bool ativo;
	bool concluido;
	bool abandonou;
	bool usado;
};

struct aluno *criar_aluno();
struct aluno *adicionar_info(struct aluno *temp,char *identificador, char *codigo);
FILE *openFile(const char *filename);
int ler_aluno(FILE *ficheiro,int indice, struct aluno *temp );
void escrever_aluno(FILE *ficheiro,int indice, struct aluno *temp);

int procurar_mem(FILE *ficheiro, struct aluno *temp, char *identificador);
void insere_aluno(FILE *ficheiro, struct paises *ht, char *identificador, char *codigo);
void fazer_operacao(FILE *ficheiro, struct paises *ht, int indice, struct aluno *temp, int modo);
void operacao_aluno(FILE *ficheiro, struct paises *ht, char *identificador, int modo);

void obter_info_paises(FILE *ficheiro, struct paises *paises);
void atualizar_BD(FILE *ficheiro, struct paises *paises);