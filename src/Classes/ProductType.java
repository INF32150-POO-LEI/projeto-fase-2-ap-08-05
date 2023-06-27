package Classes;

/**
 * Representa os tipos de produtos
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public enum ProductType {
    /** Roupa */
    CLOTHING,
    /** Acessório */
    ACCESSORY,
    /** Brinquedo Pequeno */
    SMALL_TOY,
    /** Brinquedo Grande */
    LARGE_TOY,
    /** Eletrónico Pequeno */
    SMALL_ELECTRONIC,
    /** Eletrónico Grande */
    LARGE_ELECTRONIC,
    /** Livro */
    BOOK;

    /**
     * Retorna um tipo de produto fornecido uma string do nome
     * @param string String equivalente ao tipo
     * @return Tipo de produto fornecida o nome
     */
    public static ProductType getProductType(String string) {

        try {
            return ProductType.values()[Integer.parseInt(string)];
        }
        catch (Exception e) {
            return ProductType.valueOf(string.toUpperCase());
        }
    }
}
