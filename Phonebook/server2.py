import socket, select
import traceback # para informação de excepções
from time import sleep
import pickle

SOCKET_LIST = []    # lista de sockets abertos
RECV_BUFFER = 4096  # valor recomendado na doc. do python
PORT = 5000

lista_contactos={}


##### protocol #############
		
def decode (inmsg):
	
	inmsg = inmsg.split()

	if inmsg[0] == "SETNUMBER":
		arr = ["-set"]
		arr.append(inmsg[1])
		arr.append(inmsg[2])
		return arr

	if inmsg[0] == "DELETENUMBER":
		arr = ["-del"]
		arr.append(inmsg[1])
		arr.append(inmsg[2])
		return arr

	if inmsg[0] == "DELETECLIENT":
		arr = ["-del"]
		arr.append(inmsg[1])
		return arr

	if inmsg[0] == "GETNUMBER":
		return [inmsg[1]]

	if inmsg[0] == "REVERSE":
		return [inmsg[1]]

############################

def pickle_read(lista_contactos):
	count=0
	try:
		pickle_in = open("contactos.pickle",'rb')
		return pickle.load(pickle_in)
	except:
		count+=1

	if count==1:
		return lista_contactos
	
	

def pickle_write(lista_contactos):
	pickle_out = open("contactos.pickle",'wb')
	pickle.dump(lista_contactos, pickle_out)

# função que trata dados do cliente

def faz_coisas(data, sock):

	input_info = decode(data)

	print(data)
	#print("Client %s\n\tMessage: '%s'" % (sock, data))
	#input_info = format(data)

	if len(input_info)==1:
		if input_info[0][1].isdigit():
			getNome(input_info[0])
		else:
			getPhone(input_info[0])

	elif input_info[0] == "-set":
		setNum(input_info[1],input_info[2])
	
	elif input_info[0] == "-del":
		if len(input_info)==2:
			delContacto(input_info[1])
		else:
			delNumero(input_info[1],input_info[2])

def getPhone(info): #receber nome devolver numero(s). READ do pickle
	
	string="CLIENTHASNUMBERS " + info
	lista={}
	lista = pickle_read(lista_contactos)
	
	if info in lista:
		for i in lista[info]:
			string += " " + i
	else:
		string = "NOTFOUND " + info

	
	print(lista)
	print(string)

	sock.sendall(string.encode())

def getNome(info): #recebe numero, devolve a quem pertence (pode ser >1)
	
	string="CLIENTHASNAMES " + info
	lista={}
	lista = pickle_read(lista_contactos)

	for i in lista:
		
		if info in lista[i]:
			
			string += " " + i 
		
	if len(string)==len("CLIENTHASNAMES " + info):
		string = "NOTFOUND " + info
	
	
	print(lista)
	print(string)
	sock.sendall(string.encode())
		
		

def setNum(nome,num): #definir contacto
	
	
	string = ""
	lista={}
	lista = pickle_read(lista_contactos)
	 
	if nome in lista:
		if num in lista[nome]:
			string = "ERROR: " +nome + " already has " + num + " as a contact."
			
		else:
			print(lista[nome])
			lista[nome].append(num)
			string = "NUMBERSET " + nome + " " + num
			
	else:
		lista[nome] = [num]
		string = "NUMBERSET " + nome + " " + num

	pickle_write(lista)
	print(lista)
	print(string)
	sock.sendall(string.encode())
	
	

def delContacto(nome): #eliminar contacto
	
	string = ""
	lista={}
	lista = pickle_read(lista_contactos)

	if nome in lista:
		novo = lista
		del novo[nome]
		pickle_write(novo)
		string = "DELETED " + nome
	else:
		string= "NOTFOUND " + nome

	print(string)
	print(lista)
	sock.sendall(string.encode())

def delNumero(nome,num): #eliminar numero pertencente ao nome
	
	string = ""
	lista={}
	lista = pickle_read(lista_contactos)
	 
	
	if num in lista[nome]:
		lista[nome].remove(num) 
		pickle_write(lista)
		string = "DELETED " + nome + " " + num
	else:
		string= "NOTFOUND " + nome

	print(string)
	print(lista)
	sock.sendall(string.encode())
	
          
if __name__ == "__main__":
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind(("0.0.0.0", PORT))  # aceita ligações de qualquer lado
    server_socket.listen(10)
    server_socket.setblocking(0) # o socket deixa de ser blocking
    
    # Adicionamos o socket à lista de sockets a monitorizar
    SOCKET_LIST.append(server_socket)
    
    print("Server started on port %d" % (PORT,))

    timecount = 0
    while True:  # ciclo infinito

        # apagamos os sockets que "morreram" entretanto
        for sock in SOCKET_LIST:
            if sock.fileno() < 0:
                SOCKET_LIST.remove(sock)

        # agora, esperamos que haja dados em algum dos sockets que temos
        rsocks,_,_ = select.select(SOCKET_LIST,[],[], 5)

        if len(rsocks) == 0: # timeout
            timecount += 5
            print("Timeout on select() -> %d secs" % (timecount))
            if timecount % 60 == 0:  # passou um minuto
                timecount = 0
            continue
        
        for sock in rsocks:  # percorrer os sockets com nova informação
             
            if sock == server_socket: # há uma nova ligação
                newsock, addr = server_socket.accept()
                newsock.setblocking(0)
                SOCKET_LIST.append(newsock)
                
                print("New client - %s" % (addr,))
                 
            else: # há dados num socket ligado a um cliente
                try:
                    data = sock.recv(RECV_BUFFER).decode()
                    if data:
                        faz_coisas(data, sock)
                        
                    else: # não há dados, o cliente fechou o socket
                        print("Client disconnected 1")
                        sock.close()
                        SOCKET_LIST.remove(sock)
                        
                except Exception as e: # excepção ao ler o socket, o cliente fechou ou morreu
                    print("Client disconnected")
                    print("Exception -> %s" % (e,))
                    print(traceback.format_exc())
                    
                    sock.close()
                    SOCKET_LIST.remove(sock)
                    
                    
