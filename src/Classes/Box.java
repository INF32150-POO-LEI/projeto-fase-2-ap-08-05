package Classes;

/**
 * Representa uma caixa
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Box extends Storage {
    private static int COUNT = 0;
    private int id;

    /**
     * Construtor da Box
     */
    public Box() {
        super();
        id = COUNT++;
    }

    @Override
    public boolean addProduct(Product product) {
        if (product == null || products.contains(product) || products.size() >= 1) return false;
        if (product.getType() != ProductType.BOOK &&
            product.getType() != ProductType.ACCESSORY &&
            product.getType() != ProductType.SMALL_TOY &&
            product.getType() != ProductType.SMALL_ELECTRONIC) return false;
        return products.add(product);
    }

    @Override
    public String toString() {
        return "BX" + id;
    }
}
