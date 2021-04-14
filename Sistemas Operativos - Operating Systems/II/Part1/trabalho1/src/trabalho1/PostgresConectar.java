package trabalho1;

import java.sql.DriverManager;
import java.sql.Statement;

public class PostgresConectar {
    
    java.sql.Connection con = null;
    java.sql.Statement stmt = null;
     
    private String PG_HOST;
    private String PG_DB;
    private String USER;
    private String PWD;
    
    
    public PostgresConectar() throws Exception {
        
        //buscar os valores a um ficheiro
        ValoresBD prop = new ValoresBD();
        PG_HOST = prop.getProperties("host");
        PG_DB = prop.getProperties("bd");
        USER = prop.getProperties("user");
        PWD = prop.getProperties("pw");
        
        
    }
    
     public PostgresConectar(String host, String db, String user, String pw) throws Exception 
    {
        PG_HOST=host;
        PG_DB= db;
        USER=user;
        PWD= pw;
        
    }
    
     //conectar com bd
    public void connect() throws Exception {
        
        try {
            Class.forName("org.postgresql.Driver");
            // url = "jdbc:postgresql://host:port/database",
            con = DriverManager.getConnection("jdbc:postgresql://" + PG_HOST + ":5432/" + PG_DB,
                    USER,
                    PWD);

            stmt = con.createStatement();
            
            if (stmt != null) {
                System.out.println("Ligado à Base de Dados");
            } else {
                System.out.println("Erro ao ligar à Base de Dados");
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }

    public void disconnect() {    // importante: fechar a ligacao 'a BD
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return stmt;
    }
}
