 

/**
 * Representa um saco
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Bag extends Storage {
    private static int COUNT = 0;
    private int id;

    /**
     * Construtor da Bag
     */
    public Bag() {
        super();
        id = COUNT++;
    }

    @Override
    public boolean addProduct(Product product) {
        if (product == null || products.contains(product) || products.size() >= 1) return false;
        if (product.getType() != ProductType.CLOTHING &&
            product.getType() != ProductType.ACCESSORY &&
            product.getType() != ProductType.SMALL_TOY &&
            product.getType() != ProductType.SMALL_ELECTRONIC) return false;
        return products.add(product);
    }

    @Override
    public String toString() {
        return "BG" + id;
    }
}
