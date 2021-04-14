import socket
import sys
from sys import argv
from time import sleep
import pickle

cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cliente.connect(('localhost',5000))

# funções #

def format(inp):
	
	formarray = []

	# cheks  #
	if not "getphone" in inp:
		return[]

	# removes the getphone string from the input#
	inp = inp.replace("getphone",'')

	# removes spaces form the begin of the input#
	while ( inp.startswith(' ')):
		inp = inp[1:]

	# removes spaces form the begin of the input#
	while ( inp.endswith(' ')):
		inp = inp[:-1]

	# var extraction #
	while inp != "":

		if inp[0] == '-':
			
			n = ""

			while inp[0] != ' ':
				n += inp[0]
				inp = inp[1:]
				if inp == "":
					break

			formarray.append(n)
			continue

		# add if name #
		if inp[0] == '"':
			
			inp = inp[1:]

			n = ""

			while inp[0] != '"':
				n += inp[0]
				inp = inp[1:]

			inp = inp[1:]

			formarray.append(n)
			continue
		
		# add if contact #
		if inp[0].isdigit():

			n = ""

			while inp[0].isdigit():
				n += inp[0]
				inp = inp[1:]
				if inp == "":
					break
			
			formarray.append(n)
			continue

		# ignore if is space #
		if inp[0] == " ":
			inp = inp[1:]
					
	return formarray

# protocol #

def encode (msg):

	if(msg[0] == "-set"):
		return "SETNUMBER " + msg[1] + " " + msg[2]

	if(msg[0] == "-del"):
		if (len(msg) > 2):
			return "DELETENUMBER " + msg[1] + " " + msg[2]
		else:
			return "DELETECLIENT " + msg[1]

	if(msg[0].isdigit()):
		return "REVERSE " + msg[0]
	else:
		return "GETNUMBER " + msg[0]
		
def decode (outmsg, inmsg):
	
	outmsg = outmsg.split()
	inmsg = inmsg.split()

	if (outmsg[0] == "SETNUMBER"):
		if(inmsg[0] == "NUMBERSET"):
			return inmsg[1] + " number set to " + inmsg[2]
		else:
			return "ERROR:Number no set"

	if (outmsg[0] == "DELETENUMBER"):
		if(inmsg[0] == "DELETED"):
			return inmsg[1] + " number " + inmsg[2] + " deleted from database"
		else:
			return "ERROR:Number no deleted"

	if (outmsg[0] == "DELETECLIENT"):
		if(inmsg[0] == "DELETED"):
			return inmsg[1] + " deleted from database"
		else:
			return "ERROR:Number no deleted"

	if (outmsg[0] == "GETNUMBER"):
		if(inmsg[0] == "CLIENTHASNUMBERS"):
			
			msg = ""

			for i in range(2,len(inmsg)):
				msg += inmsg[1] + " has number " + inmsg[i] +'\n'

			return msg[:-1]

		else:
			return "ERROR:Name not found"

	if (outmsg[0] == "REVERSE"):
		if(inmsg[0] == "CLIENTHASNAMES"):
			
			msg = ""

			for i in range(2,len(inmsg)):
				msg += inmsg[1] + " is the number for " + inmsg[i] +'\n'

			return msg[:-1]

		else:
			return "ERROR:Number not found"
		


# MAIN #

if __name__ == "__main__":
	
	try:

		while True:
			inp = format( input("$ ") )

			inp = encode(inp)

			cliente.sendall(inp.encode())

			rcv = cliente.recv(1024).decode()

			print(decode(inp, rcv))

	except Exception as e:
		print(e)



#pedir input mandar ao server, sendAll
#timesleep, to server ao client ou cliente server, junta a info se mandar dois seguidos
#usar funcao decode e encode quando receber e mandar 
