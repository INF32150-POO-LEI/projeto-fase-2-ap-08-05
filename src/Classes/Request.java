package Classes;

/**
 * Representa um pedido de transporte a ser honrado pelos veículos de transporte
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Request {
    /**
     * Fonte
     */
    public Object source;
    /**
     * Destino
     */
    public Object destination;
    /**
     * Produto
     */
    public Product product;

    /**
     * Construtor de Request
     * @param source Fonte
     * @param destination Destino
     * @param product Produto
     */
    public Request(Object source, Object destination, Product product) {
        this.source = source;
        this.destination = destination;
        this.product = product;
    }

    @Override
    public String toString() {
        return "[ origem: " + source + " | destino: " + destination + " | produto: " + product + " ]";
    }
}
