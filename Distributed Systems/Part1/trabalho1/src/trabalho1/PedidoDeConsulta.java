package trabalho1;

//pedido de consulta de todos os items necessitados
import java.util.ArrayList;

public class PedidoDeConsulta extends Pedido implements java.io.Serializable{
    public String elemento;
    
    public PedidoDeConsulta(String elemento) 
    {
        this.elemento = elemento;
    }

    
    public String getElemento()
    {
        return elemento;
    }
    
}
