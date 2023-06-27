package Classes;

/**
 * Representa um produto
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Product {
    private static int COUNT = 0;

    private int id;
    private String name;
    private float weight;
    private ProductType type;

    /**
     * Construtor de Product
     * @param name nome do produto
     * @param weight peso do produto
     * @param type tipo do produto
     */
    public Product(String name, float weight, ProductType type) {
        id = COUNT++;

        try {
            this.name = validateName(name);
            this.weight = validateWeight(weight);
            this.type = validateType(type);
        }
        catch (DistributionCenterException e) {System.out.println(e); }
    }

    private static String validateName(String name) {
        if (name == null) throw new DistributionCenterException(ErrorCode.PRODUCT_NAME_IS_NULL);
        else if (name.isEmpty()) throw new DistributionCenterException(ErrorCode.PRODUCT_NAME_IS_INVALID);
        else return name;
    }

    private static float validateWeight(float weight) {
        if (weight < 0) throw new DistributionCenterException(ErrorCode.PRODUCT_WEIGHT_IS_INVALID);
        else return weight;
    }

    private static ProductType validateType(ProductType type) {
        if (type == null) throw new DistributionCenterException(ErrorCode.PRODUCT_TYPE_IS_NULL);
        else return type;
    }

    /**
     * Retorna a id deste produto
     * @return ID deste produto
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome deste produto
     * @return Nome deste produto
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o peso deste produto
     * @return Peso deste produto
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Retorna o tipo deste produto
     * @return Tipo deste produto
     */
    public ProductType getType() {
        return type;
    }

    /**
     * Verifica se este produto e o fornecido são do mesmo modelo
     * @param product produto a testar
     * @return Verdadeiro se este produto e o fornecido são o mesmo modelo
     */
    public boolean isSameModel(Product product) {
        return this.name.equals(product.name) && this.weight == product.weight && this.type == product.type;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if(this == object) return true;
        if(!(object instanceof Product)) return false;
        Product product = (Product) object;
        return this.id == product.id;
    }

    @Override
    public String toString() {
        return "[ id: " + id + " | nome: " + name + " | peso: " + weight + " | tipo: " + type + " ]";
    }
}
