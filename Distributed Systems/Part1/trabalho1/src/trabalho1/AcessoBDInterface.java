package trabalho1;

import java.util.ArrayList;

public interface AcessoBDInterface extends java.rmi.Remote{
    
    //adiciona a' bd um utilizador novo,verificando antes de já existe um utilizador registado com o idCliente passado
    public void adUtilizador(String idCliente) throws java.rmi.RemoteException;
    
    //adiciona a'bd a loja onde o produto pode ser adicionado
    public void adLocalProduto(PedidoDeRegisto p) throws java.rmi.RemoteException;

    //consulta os items necessitados por outros utilizadores sem divulgar a identidade dos mesmos
    public ArrayList<PedidoDeConsulta> consulta() throws java.rmi.RemoteException;

    //adiciona a' bd um produto que e'considerado necessario pelo utilizador, guardando o id do mesmo. devolve um id unico que representa a necessidade
    public int adNecessidades(String id, String produto) throws java.rmi.RemoteException;
    
    //verifica se o produto passado existe na table dos produtos localizados. se for encontrado em mais do que uma loja devolve um arraylist com objectos de PedidoDeConsulta com o nome da loja
    public ArrayList<PedidoDeConsulta> verificarExistencia(String produto) throws java.rmi.RemoteException;

    //usado pela classe notificacao, para determinar se os produtos adicionados pelo cliente como necessidades ja'foram encontrados 
    public ArrayList<PedidoDeReporte> notificarStock(String idCliente) throws java.rmi.RemoteException;
   

}
