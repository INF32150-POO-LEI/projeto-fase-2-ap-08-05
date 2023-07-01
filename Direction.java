 

/**
 * Representa as direções possíveis de movimento
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public enum Direction {
    /** Esquerda */
    LEFT,
    /** Direita */
    RIGHT,
    /** Cima */
    UP,
    /** Baixo */
    DOWN;

    /**
     * Retorna uma direção fornecida uma string do nome
     * @param string String equivalente à direção
     * @return Direção equivalente
     */
    public static Direction getDirection(String string) {

        try {
            return Direction.values()[Integer.parseInt(string)];
        }
        catch (Exception e) {
            return Direction.valueOf(string.toUpperCase());
        }
    }
}
