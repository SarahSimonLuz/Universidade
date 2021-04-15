package trabalho1;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente{
   private AcessoBDInterface bd;
   String idCliente;
   private BufferedWriter writer;
   private File ficheiro;
   
    
    protected Cliente(AcessoBDInterface bd, String idCliente) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        this.bd = bd;
        this.idCliente = idCliente;
        
        //inicializar um objeto file 
        ficheiro = new File("/home/aluno/NetBeansProjects/trabalho1/input_"+idCliente+".txt");
        //verificar se o ficheiro com o nome input_"+idCliente ja'existe ou se ja'existe se está vazio
        if(!(ficheiro.exists() && !ficheiro.isDirectory()) || ficheiro.length() == 0)
        { 
            reFicheiro();
        }  
        
    }
    
    //criar um objecto BufferedWriter para poder escrever no ficherio 
    public void reFicheiro() throws IOException
    {
        writer = new BufferedWriter(new FileWriter("input_"+idCliente+".txt"));
    }
    
    
    public static void main(String args[]) throws IOException{
        
        if (args.length != 2){
            System.err.println("Argumentos insuficientes: REGISTRYHOST REGISTRYPORT");
            System.exit(1);
        }

        try {
            int lidos;
	    byte[] b= new byte[256];
        
            //obter o id do cliente
            System.out.println("ID de Utilizador:");
            lidos= System.in.read(b);
            String id= new String(b,0,lidos-1);
            
            //inicializar um objecto da interface AcessoBDInterface para aceder a' base de dados de forma remota
            AcessoBDInterface bd = (AcessoBDInterface) java.rmi.Naming.lookup("rmi://"+args[0] + ":"
                + args[1] + "/pedido");
            
            
            Cliente cl = new Cliente(bd,id);
            
            //adicionar utilizador
            bd.adUtilizador(cl.idCliente);
            
            //iniciar a toca de dados
            cl.menu(bd, cl);
            
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println("Problema no formato dos argumentos");
            System.out.println(e);
        }
    }
    
    
    public static void menu(AcessoBDInterface bd, Cliente cl)throws RemoteException, IOException{
        
	    try {
		while (true) {  // ciclo: pede ao utilizador...
                    
                    //se for maior que 0 significa que a aplicacao cliente crashou
                    if(cl.ficheiro.length() > 0)
                    {
                        System.err.println("\n[ATENCAO] aplicacao cliente crashou\n");
                        
                        BufferedReader br = new BufferedReader(new FileReader(cl.ficheiro)); 
                        String st; 
                        
                        //escrever na consola os ultimos dados escritos pelo utilizador antes de crashar
                        System.err.println("Item(s) que iria(m) ser enviado(s) ao servidor:");
                        while ((st = br.readLine()) != null) {
                          
                            System.err.println(st); 
                        } 
                        System.err.println("\nSera necessario digitar de novo a operacao e os dados.");
                        
                        //criar outro objecto BufferedWriter
                        cl.reFicheiro();
                    }
                    
                    //mostrar as operacoes disponiveis ao utilizador
                    Scanner sc = new Scanner(System.in);
		    System.out.print("\n--- MENU --- \n"+
                                        "registar - registar a existencia de um produto numa loja\n"+
                                        "consultar - ver quais produtos sao necessidades\n"+
                                        "reporte - receber notificação da existencia do produto desejado\n"+
                                        "sair\n> ");
		    
                    
		    String op = sc.nextLine();
		    if (op.equals("registar"))  
			registar(bd,cl); 
                    
		    else if (op.equals("consultar"))
			consultar(bd);
                
                    else if (op.equals("reporte"))
                        reporte(bd, cl);
                   
                    else if (op.equals("sair"))
                        System.exit(0);
                    
		    else 
			System.out.println("opcao invalida");
                    
                    //criar outro BufferedWriter
                    cl.reFicheiro();
		}
                
	    }
	    catch (Exception e) {
		e.printStackTrace();
                cl.writer.close();
	    }
            
            cl.writer.close();
                 
    }
    
    //registar na bd a localizacao de um produto
    public static void registar(AcessoBDInterface bd, Cliente cl) throws IOException, RemoteException{
        
        Scanner sc = new Scanner(System.in);
        cl.writer.write("registar\n");
        
	System.out.println("produto: ");
        String produto = sc.nextLine();
        cl.writer.write(produto+"\n");
        
        System.out.println("loja: ");
        String loja = sc.nextLine();
        cl.writer.write(loja+"\n");
        
        PedidoDeRegisto p = new PedidoDeRegisto(produto, loja);
	bd.adLocalProduto((PedidoDeRegisto) p);
        
        
    }
    
    //pedir a' bd os produtos considerados necessarios
    public static void consultar(AcessoBDInterface bd) throws RemoteException{
        ArrayList<PedidoDeConsulta> resposta = bd.consulta();
        
        for(PedidoDeConsulta pedido: resposta){
            System.out.println(pedido.getElemento());
        }
        
    }
    
    //utilizador diz que necessita de um certo produto, primeiro e' averiguada na bd se existe a localizacao desse produto
    //caso contrario. e' devolvido o id da necessidade e inicializada um objecto da classe notificacao
    public static void reporte(AcessoBDInterface bd, Cliente cl) throws IOException, RemoteException{
        Scanner sc = new Scanner(System.in);
        cl.writer.write("reporte\n");
        
        System.out.println("produto: ");
        String produto = sc.nextLine();
        cl.writer.write(produto+"\n");
        
        ArrayList<PedidoDeConsulta> resposta = bd.verificarExistencia(produto);
        
        if(resposta.isEmpty())
        {
            int id = bd.adNecessidades(cl.idCliente,produto);
            System.out.println("\nCodigo de registo de necessidade: "+ id);
            
            Notificacao notificar = new Notificacao(produto,bd,cl);
            //a thread fica a trabalhar no background ate' ser introduzido na bd a localizacao do produto necessario, quando for encontrado a thread termina
            notificar.start();
        }
        else
        {
            //caso sejam encontradas localizacoes do produto seram escritas na consola
            System.out.println("\nPode encontrar o produto, "+produto+" na(s) loja(s):");
            for(PedidoDeConsulta pedido: resposta)
            {
                System.out.println(pedido.getElemento());
            }
        }
    }
}
