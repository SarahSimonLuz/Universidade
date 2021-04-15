#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "disco.h"

#define BDNOMEFICHEIROALUNO "alunos.bin"
#define BDNOMEFICHEIROPAIS "pais.bin"
#define MAX_ID_ALUNO 7
#define MAX_ID_PAIS 3

#define MAX_TAMANHO_PAISES 1361

int main(void)
{
	//codigo, codigo pais
	char operacao, identificador[MAX_ID_ALUNO], codigo[MAX_ID_PAIS];

	//Abre o ficheiro
	FILE *ficheiro_alunoBD = openFile(BDNOMEFICHEIROALUNO);
	FILE *ficheiro_paisBD = openFile(BDNOMEFICHEIROPAIS);
	

	struct paises *ht_paises = criar_hashTable();
	//obter a informacao dos paises que esta' guardada em disco e guarda na hashtable criada
	obter_info_paises(ficheiro_paisBD, ht_paises);
	
	while(scanf("%c", &operacao) != EOF)
	{
		switch(operacao)
		{
			case 'I':
				scanf("%s %s", identificador, codigo);
				insere_aluno(ficheiro_alunoBD, ht_paises, identificador, codigo);
				break;

			case 'R':
				scanf("%s", identificador);
				operacao_aluno(ficheiro_alunoBD, ht_paises, identificador,5);
				break;

			case 'T':
				scanf("%s", identificador);
				operacao_aluno(ficheiro_alunoBD, ht_paises, identificador,6);
				break;

			case 'A':
				scanf("%s", identificador);
				operacao_aluno(ficheiro_alunoBD, ht_paises, identificador,7);
				break;

			case 'P':
				scanf("%s", codigo);
				obter_dados(ht_paises, codigo);
				break;

			case 'X':
				atualizar_BD(ficheiro_paisBD, ht_paises);
				fclose(ficheiro_paisBD);
				fclose(ficheiro_alunoBD);
				return 0;
				break;

			default:
				continue;
		}
	}
	return 0;
}
