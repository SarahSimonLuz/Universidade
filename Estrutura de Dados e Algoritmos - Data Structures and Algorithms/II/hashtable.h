#include <stdbool.h>

#define MAX_TAMANHO_PAISES 1361

struct pais
{
	char idpais[3];
	int n_total;
	int n_ativos;
	int n_completou;
	int n_abandonou;
	int indice;

};

struct paises{
    struct pais *tabela[MAX_TAMANHO_PAISES];
};

struct paises *criar_hashTable();
struct paises *novo_pais();
void free_hashtable(struct paises *ht);
int procurar(struct paises *ht, char *codigo);
int inserir(struct paises *ht, char *codigo, int total, int ativos, int completou, int abandonou);
int hashCode(char *codigo);
unsigned int hashCode1(char *codigo, int tamanho);
int hashCode2(char *codigo, int tamanho, int primo);
int double_hashing(int contador, char *identificador, int tamanho, int primo);
void obter_dados(struct paises *ht, char *codigo);
void atualizar_info_paises(struct paises *ht, char *codigo, int modo);