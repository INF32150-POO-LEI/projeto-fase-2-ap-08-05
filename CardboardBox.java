 

/**
 * Representa uma caixa de cartão
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class CardboardBox extends Storage {
    private static int COUNT = 0;
    private int id;

    /**
     * Construtor da CardboardBox
     */
    public CardboardBox() {
        super();
        id = COUNT++;
    }

    @Override
    public boolean addProduct(Product product) {
        if (product == null ||
            products.contains(product) ||
            products.size() >= 10 || (products.size() == 1 &&
                (products.get(0).getType() == ProductType.LARGE_TOY ||
                products.get(0).getType() == ProductType.LARGE_ELECTRONIC))) return false;
        return products.add(product);
    }

    @Override
    public String toString() {
        return "CBX" + id;
    }
}
