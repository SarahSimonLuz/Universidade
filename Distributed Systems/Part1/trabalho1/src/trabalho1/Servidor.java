package trabalho1;

import java.rmi.RemoteException;

public class Servidor{
    
    public static void main(String[] args) throws Exception{
        int regPort = 1099; //default RMIRegistry port
        
	if (args.length!=1) {
	    System.err.println("Missing parameter: port number");	
	    System.exit(1);
	}
        
	try {
	    regPort= Integer.parseInt( args[0] );
            
            //objecto para aceder a' bd
            AcessoBDInterface obj = new AcessoBD();
        
            java.rmi.registry.LocateRegistry.createRegistry(regPort);

            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(regPort);

            registry.rebind("pedido", obj);

            System.err.println("Bound RMI object in registry");

            System.err.println("SERVIDOR pronto...");
            
	}
	catch (NumberFormatException | RemoteException e) {
	    e.printStackTrace();
	}
    }
}
	
