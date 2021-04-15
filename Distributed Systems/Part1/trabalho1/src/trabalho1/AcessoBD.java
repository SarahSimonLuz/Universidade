package trabalho1;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.rmi.server.*;


public class AcessoBD extends UnicastRemoteObject implements AcessoBDInterface,java.io.Serializable{
    PostgresConectar pc;
    Statement stmt;
  
    public AcessoBD() throws java.rmi.RemoteException, Exception
    {
        //pc = new PostgresConectar("localhost","bdApp", "server","password");
        pc = new PostgresConectar();
        
        pc.connect();
        stmt = pc.getStatement();
    }
    
    @Override
    public void adUtilizador(String idCliente) throws java.rmi.RemoteException {
        try { 
            
            ResultSet rs = stmt.executeQuery("SELECT id FROM utilizador WHERE id = '"+idCliente+"'");
            
            if (!rs.next())
            {
                stmt.executeUpdate("INSERT INTO utilizador VALUES('"+idCliente+"')");
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro a adicionar utilizador ...");
       }
        
    }

    @Override
    public void adLocalProduto(PedidoDeRegisto p) throws java.rmi.RemoteException {
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM localproduto WHERE (loja = '"+p.getLoja()+"' AND produto = '"+p.getProduto()+"');");
            
            if (!rs.next())
            {
                stmt.executeUpdate("INSERT INTO localproduto VALUES('"+p.getLoja()+"','"+p.getProduto()+"')");
            }
            rs.close();
            System.err.println(p.getProduto()+ " adicionado");
 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro a adicionar produto...");
        }
    }

    @Override
    public ArrayList<PedidoDeConsulta> consulta() throws java.rmi.RemoteException {
        
        ArrayList<PedidoDeConsulta> tmp = new ArrayList<>();
        
        try {
            ResultSet rs = stmt.executeQuery("SELECT produto FROM necessidades;"); 
            
            while (rs.next()) {
                String produto = rs.getString("produto");
                tmp.add(new PedidoDeConsulta(produto));
                
            }
            rs.close(); 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
        }
        
        return tmp;
    }

    @Override
    public int adNecessidades(String id,String produto) throws java.rmi.RemoteException {
        
       int id_necessidade = 0;
       
        try 
        { 
            //verifica se o mesmo utilizador nao repete a necessidade
            ResultSet rs = stmt.executeQuery("SELECT idnecessidade FROM necessidades WHERE( id = '"+id+"' AND produto = '"+produto+"');");
            
            if (!rs.next())
            {
                stmt.executeUpdate("INSERT INTO necessidades VALUES('"+id+"','"+produto+"')");
            }
            
            rs = stmt.executeQuery("SELECT idnecessidade FROM necessidades WHERE (id = '"+id+"' AND produto = '"+produto+"');");
            
            while (rs.next()){
                id_necessidade = rs.getInt("idnecessidade");
            }
            
            rs.close(); 
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problema a inserir necessidade...");
       }
        return id_necessidade; 
    }
    
    @Override
    public ArrayList<PedidoDeConsulta> verificarExistencia(String produto) throws java.rmi.RemoteException{
        
        ArrayList<PedidoDeConsulta> tmp = new ArrayList<>();
        
        try {
            ResultSet rs = stmt.executeQuery("SELECT loja FROM localproduto WHERE produto = '"+produto+"'"); 
            
           
            while (rs.next()) {
                String loja = rs.getString("loja");
                tmp.add(new PedidoDeConsulta(loja));
     
            }
            rs.close(); 
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
        }
        
        return tmp;
    }
    
    @Override
    public ArrayList<PedidoDeReporte> notificarStock(String idCliente) throws java.rmi.RemoteException {
        
        ArrayList<PedidoDeReporte> tmp = new ArrayList<>();
        String produto = null;
        
        try {
            
            ResultSet rs = stmt.executeQuery("SELECT necessidades.produto,loja FROM necessidades, localproduto WHERE necessidades.produto = localproduto.produto AND id = '"+ idCliente+"'"); 
            
            while (rs.next()) {
                produto = rs.getString("produto");
                String loja = rs.getString("loja");
                tmp.add(new PedidoDeReporte(produto,loja));
            }
            rs.close(); 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
        }
        
        try
        {
             stmt.executeUpdate("DELETE FROM necessidades WHERE produto = '"+produto+"' AND id = '"+idCliente+"'");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
        }
        
        
        return tmp;
    }
    
}
