import sys
from sys import argv
import math
import os

antes = argv[1]
depois = argv[2]
img_antes = open(antes,"r") 
img_depois = open(depois,"r")
data_antes = img_antes.read()
data_depois = img_depois.read()


def entropia(valores,dimensao):
	
	entropia = 0
	probs=[]
	
	for valor in valores:
		probs.append(valor/dimensao)
	
	for valor in probs:
		entropia += valor*math.log(valor,2)			
		
	return -entropia

def entropiaCond(valores,dimensao):

	ret = 0
	probs=[]
	probsCond=[]
	count=0
	out=0

			
	for valor in dimensao[0:2]:#p(0) e p(1)
		
		probs.append(valor/dimensao[2])
		
	for i in probs:
		out+=i * entropia(valores[count:count+2],dimensao[2])
		count+=2

	return out


def entropiaNormal(data):

	countZero=0
	countUm=0
	countTotal = 0
	valores=[]
	
	data = data.split()
	for elemento in data:

		if elemento == "0":
			countZero+=1
			countTotal+=1

		elif elemento == "1":
			countUm+=1
			countTotal+=1

	valores.append(countUm)
	valores.append(countZero)

	return entropia(valores,countTotal)

def entropiaCondicional(data):
	zerozero=0
	zeroum=0
	umzero=0
	umum=0
	countUm = 0
	countZero = 0
	countTotal=0
	valores=[]
	count=[]
	lel=[]
	data = data.split()

	alfabeto = {chr(i):i for i in range(58,255)}
	simbolo = {chr(i):i for i in range(32,47)}
	array=[]

	for elemento in data:
		
		if not(elemento[0] in alfabeto or elemento[0] in simbolo):
			if len(valores)<2:
				valores.append(elemento)
			else:
				array.append(elemento)
			
	valores.clear()

	esquerda=array[0]
	if esquerda==0:
		countZero+=1
	else:
		countUm+=1

	for direita in array[1:]:
		str_conc = esquerda + direita
		
		if str_conc == "00":
			zerozero+=1

		elif str_conc == "01":
			zeroum+=1

		elif str_conc == "10":
			umzero+=1

		elif str_conc == "11":
			umum+=1

		if direita=="0":
			countZero+=1
		else:
			countUm+=1
			
		esquerda = direita
		countTotal +=1
	
	valores.append(zerozero)
	valores.append(zeroum)
	valores.append(umzero)
	valores.append(umum)
	
	count.append(countZero)
	count.append(countUm)
	count.append(countTotal)

	return entropiaCond(valores,count)


print("Entropia da imagem antes de ser comprimida:  ",entropiaNormal(data_antes),"\nEntropia da imagem depois ser descodificada: ",entropiaNormal(data_depois),"\nEntropia condicionar da imagem antes de ser comprimida: ",entropiaCondicional(data_antes),"\nEntropia condicional da imagem depois de ser comprimida: ", entropiaCondicional(data_depois))

img_antes.close()
img_depois.close()
