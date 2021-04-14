package trabalho1;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Notificacao extends Thread implements Runnable{
    
    String produto;
    Cliente cl;
    AcessoBDInterface bd;
    
    private volatile boolean running = true;

    public Notificacao(String produto, AcessoBDInterface bd, Cliente cl)
    {
        this.produto = produto;
        this.bd = bd;
        this.cl = cl;
    }

    public void run()
    {
        while (running)
        {
            try
            {
                ArrayList<PedidoDeReporte> resposta = bd.notificarStock(cl.idCliente);
                
                if(!resposta.isEmpty())
                {
                    for(PedidoDeReporte pedido: resposta){
                        System.out.println("\n[ALERTA] O produto, "+ pedido.getProduto() + ", foi encontrado na loja, "+ pedido.getLoja()+"\n>");
                     }      
                    
                    terminar();
                }
                
                Thread.sleep(200);
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
                running = false;
            } catch (RemoteException ex) {
                Logger.getLogger(Notificacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void terminar()
    {
        running = false;
    }
    
}
