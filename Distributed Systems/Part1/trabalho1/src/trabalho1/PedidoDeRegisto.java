package trabalho1;

//classe para os pedidos de registo da localizacao de produtos 
public class PedidoDeRegisto extends Pedido implements java.io.Serializable
{
    private String produto;
    private String loja;
    
    public PedidoDeRegisto(String produto, String loja) {
        this.produto = produto;
        this.loja = loja;
    }
    
    public String getProduto() {
        return produto;
    }

    public String getLoja() {
        return loja;
    }

}

