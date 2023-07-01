 

import java.util.ArrayList;

/**
 * Representa o objeto de um local entrega/recolha
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class LoadSite {
    private ArrayList<Product> products;

    /**
     * Construtor do LoadSite
     */
    public LoadSite() {
        products = new ArrayList<>();
    }

    /**
     * Retorna os produtos
     * @return ArrayList de produtos
     */
    public ArrayList<Product> getProducts() { return products; }

    /**
     * Verifica se contém produtos
     * @return Verdadeiro se contém produtos
     */
    public boolean hasProducts() {
        return products.size() > 0 ? true : false;
    }

    /**
     * Remove e devolve o produto removido, caso um exista
     * @return Produto, caso um exista
     */
    public Product pickProduct() {
        if(!hasProducts()) return null;
        return products.remove(0);
    }

    /**
     * Tenta receber um produto
     * @param product produto a entregar
     * @return Verdadeiro caso o produto tenha sido recebido
     */
    public boolean deliverProduct(Product product) {
        if(product == null) return false;
        return products.add(product);
    }
}
