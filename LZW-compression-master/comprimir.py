import sys
from sys import argv

nome_file = argv[1]
ficheiro = open(nome_file,"r") #os elementos são int
data = ficheiro.read()

dicionario = {"0":0,"1":1}
string = "" #representa o elemento anterior
dataComp=[]
dataInfo=[]
data_comprimida=[]
count=1

alfabeto = {chr(i):i for i in range(58,255)}
simbolos = {chr(i):i for i in range(32,47)}

data = data.split()

for elemento in data:
	
	if not (elemento[0] in alfabeto or elemento[0] in simbolos):
		if len(dataInfo)<2:
			dataInfo.append(elemento)
		else:
			dataComp.append(elemento)

for elemento in dataComp:
	string_conc = string + elemento
	if string_conc in dicionario:
		string = string_conc
	else:
		data_comprimida.append(dicionario[string])
		count+=1
		dicionario[string_conc] = count
		string = elemento


data_comprimida.append(dicionario[string])

ficheiro_output = open(nome_file.split(".")[0]+".lzw","w")
for i in dataInfo:
	string = str(i) + " "
	ficheiro_output.write(string)

ficheiro_output.write("\n")

for i in data_comprimida:
	string = str(i) + " "
	ficheiro_output.write(string)



def desempenho(antes,comprimida):
	
	desempenho = ((len(antes) - len(comprimida))/len(antes)) *100

	return desempenho

print("Desempenho:",desempenho(data,data_comprimida),"%")

ficheiro_output.close()
ficheiro.close()



	

		

