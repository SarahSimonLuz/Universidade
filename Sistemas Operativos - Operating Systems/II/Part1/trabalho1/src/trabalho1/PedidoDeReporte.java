package trabalho1;

//classe para os pedidos de reporte para quando a localizacao de um produto é adicionado a' bd
public class PedidoDeReporte extends Pedido implements java.io.Serializable{
    public String produto;
    public String loja;
 
    public PedidoDeReporte(String produto)
    {
        this.produto = produto;
    }
    
    public PedidoDeReporte(String produto, String loja)
    {
        this.produto = produto;
        this.loja = loja;
    }
 
    public String getProduto()
    {
        return produto;
    }
   
    public void setLoja(String nome)
    {
        loja = nome;
    }    
   
    public String getLoja()
    {
        return loja;
    }
}
