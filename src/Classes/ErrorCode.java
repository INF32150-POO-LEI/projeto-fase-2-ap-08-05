package Classes;

/**
 * Representa o código de erro
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public enum ErrorCode {
    //Generic
    /** Genérico */
    GENERIC,

    //Product
    /** Nome de produto nulo */
    PRODUCT_NAME_IS_NULL,
    /** Nome de produto inválido */
    PRODUCT_NAME_IS_INVALID,
    /** Peso de produto inválido */
    PRODUCT_WEIGHT_IS_INVALID,
    /** Tipo de produto nulo */
    PRODUCT_TYPE_IS_NULL,

    //Positionable
    /** Grelha de positionable nula */
    POSITIONABLE_GRID_IS_NULL,
    /** Posição de positionable nula */
    POSITIONABLE_POSITION_IS_NULL,
    /** Posição de positionable indisponível */
    POSITIONABLE_POSITION_IS_UNAVAILABLE;

    @Override
    public String toString() {
        switch (this) {
            case PRODUCT_NAME_IS_NULL:
                return "Product name cannot be null.";
            case PRODUCT_NAME_IS_INVALID:
                return "Product name is invalid.";
            case PRODUCT_WEIGHT_IS_INVALID:
                return "Product weight is invalid.";
            case PRODUCT_TYPE_IS_NULL:
                return "Product type cannot be null.";
            case POSITIONABLE_GRID_IS_NULL:
                return "Provided grid is null.";
            case POSITIONABLE_POSITION_IS_NULL:
                return "Provided position is null.";
            case POSITIONABLE_POSITION_IS_UNAVAILABLE:
                return "Provided position is invalid or unavailable.";
            default:
                return "Generic error.";
        }

    }
}
