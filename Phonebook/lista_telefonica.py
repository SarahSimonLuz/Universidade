import socket, select
import traceback 

######################__VAR__########################

SOCKET_LIST = []    
RECV_BUFFER = 4096  
PORT = 5000
MAX_TIMEOUT = 5
BUFFER_RECV = 4096

FILE_NAME = "Data.txt"

######################__func__#######################

def get_line(f, l):

    for line in f:
        if l == 0:
            return line
        l -= 1

def manager( msg , socket, dic):

    if "GETNUMBER" in msg:
    
        return

    if "SETNUMBER" in msg:

        # Extract Vars #

        msg = msg.replace("SETNUMBER ",'')

        i = 0

        for i in range(len(msg)):
            if msg[i] == ' ':
                break

        name = msg[:i]
        number = msg[i+1:]

        # checks #

        if name in dic:

            

        else:



        return
    
    if "DELETENUMBER" in msg:
    
        return

    if "DELETECLIENT" in msg:
    
        return

    if "REVERCE" in msg:
    
        return

    if "EXIT" in msg:
        raise Exception("exit Exeption")

    socket.send("Comand not found\n".encode())

    pass


def open_file(fileName):
    
    # openfile in read/write mode
    f = open(fileName, 'rw+') 

    pointer = 0

    names = {}

    for l in f:
        name = ""
        
        for c in l:
            if c == ' ':
                break
            else:
                name += c

        names[name] = pointer
        pointer += 1

    return f, pointer, names

    

######################__main__#######################

if __name__ == "__main__":

    # File IO #
    f, pointer, namedic = open_file(FILE_NAME)

    # Set Server Socket #
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind(("0.0.0.0", PORT))
    server_socket.listen(10)
    server_socket.setblocking(0)

    SOCKET_LIST.append(server_socket)

    # Loop #

    while True:

        # delete unexisting sockets #

        for socket in SOCKET_LIST:
            if socket.fileno() < 0:
                SOCKET_LIST.remove(socket)

        rsocket,_,_ = select.select(SOCKET_LIST, [], [], MAX_TIMEOUT)

        if len(rsocket) == 0:
            continue

        for socket in rsocket:

            if socket == server_socket:
                
                newsocket,_ = server_socket.accept()
                SOCKET_LIST.append(newsocket)
                addr,_ = newsocket.getsockname()
                print("New Client at : %s" % (addr))
                continue
            
            try:

                msg = socket.recv(BUFFER_RECV).decode()

                msg = msg[:-1]
                addr,_ = socket.getsockname()  
                
                print("Client at %s send: %s" % (addr, msg))

                manager(msg, socket, f, pointer)

            except Exception as e: 
                
                socket.close()
                SOCKET_LIST.remove(socket)
                se = str(e)

                if se == "exit Exception":
                    print("Client Closed Connection")
                else:
                    print("Error : %s" % (se))

        pass

    f.close()
