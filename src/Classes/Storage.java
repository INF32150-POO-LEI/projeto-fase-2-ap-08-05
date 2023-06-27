package Classes;

import java.util.ArrayList;

/**
 * Representa um contentor genérico de produtos
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public abstract class Storage {
    /**
     * Storages a guardar
     */
    protected ArrayList<Product> products;

    /**
     * Construtor de Storage
     */
    public Storage() {
        products = new ArrayList<>();
    }

    /**
     * Retorna os produtos nesta embalagem
     * @return ArrayList de produtos nesta embalagem
     */
    public ArrayList<Product> getProducts() { return products; }

    /**
     * Obtém o produto no índice fornecido caso exista
     * @param index índice do produto
     * @return Produto no índice fornecido caso exista
     */
    public Product getProduct(int index) {
        if (index < 0 || index >= products.size()) return null;
        return products.get(index);
    }

    /**
     * Tenta adicionar um produto
     * @param product produto a adicionar
     * @return Verdadeiro caso bem sucedido
     */
    public abstract boolean addProduct(Product product);

    /**
     * Tenta remover um produto fornecido o produto a remover
     * @param product produto a remover
     * @return Verdadeiro caso bem sucedido
     */
    public boolean removeProduct(Product product) {
        if (!products.contains(product)) return false;
        return products.remove(product);
    }

    /**
     * Retorna o peso total da embalagem
     * @return Peso total da embalagem
     */
    public float getWeight() {
        float total = 0;
        for (Product product : products) { total += product.getWeight(); }
        return total;
    }
}
