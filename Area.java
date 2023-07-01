 

/**
 * Representa uma área
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Area {
    /**
     * Coordenadas do canto inferior esquerdo
     */
    public Vector minimumXY;
    /**
     * Coordenadas do canto superior direita
     */
    public Vector maximumXY;

    /**
     * Construtor da Area
     * @param a posição inferior esquerda
     * @param b posição superior direita
     */
    public Area(Vector a, Vector b) {
        if (a == null) a = new Vector();
        if (b == null) b = new Vector();

        minimumXY = new Vector();
        maximumXY = new Vector();

        if (a.x <= b.x) {
            minimumXY.x = a.x;
            maximumXY.x = b.x;
        }
        else {
            minimumXY.x = b.x;
            maximumXY.x = a.x;
        }

        if (a.y <= b.y) {
            minimumXY.y = a.y;
            maximumXY.y = b.y;
        }
        else {
            minimumXY.y = b.y;
            maximumXY.y = a.y;
        }
    }

    /**
     * Testa se o vetor fornecido está dentro da área
     * @param a posição a testar
     * @return Verdadeiro se o vetor estiver dentro da área
     */
    public boolean isWithin(Vector a){
        return Vector.isBetween(minimumXY, maximumXY, a);
    }

    @Override
    public String toString() {
        return "[ " + minimumXY + ", " + maximumXY + " ]";
    }
}
