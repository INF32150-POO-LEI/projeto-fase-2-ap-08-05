 

/**
 * Representa um vetor/posição
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Vector {
    /**
     * Componentes do vetor
     */
    public int x, y;

    /**
     * Construtor do Vector padrão
     */
    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Construtor do Vector com valores
     * @param x componente X
     * @param y componente Y
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construtor do Vector para copiar outro vetor
     * @param vector vetor a copiar
     */
    public Vector(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    /**
     * Verifica se o ponto c está contido dentro de um quadrado delimitado pelos pontos a e b
     * @param a ponto A
     * @param b ponto B
     * @param c ponto C, ponto a testar
     * @return Verdadeiro caso c esteja contido entre os pontos b e c
     */
    public static boolean isBetween(Vector a, Vector b, Vector c) {
        if (a == null || b == null || c == null) return false;
        Vector minimumVector = new Vector();
        Vector maximumVector = new Vector();

        if (a.x <= b.x) {
            minimumVector.x = a.x;
            maximumVector.x = b.x;
        }
        else {
            minimumVector.x = b.x;
            maximumVector.x = a.x;
        }

        if (a.y <= b.y) {
            minimumVector.y = a.y;
            maximumVector.y = b.y;
        }
        else {
            minimumVector.y = b.y;
            maximumVector.y = a.y;
        }

        return (c.x >= minimumVector.x && c.x <= maximumVector.x && c.y >= minimumVector.y && c.y <= maximumVector.y);
    }

    /**
     * Verifica se o ponto c está contido no perímetro de um quadrado delimitado pelos pontos a e b
     * @param a ponto A
     * @param b ponto B
     * @param c ponto C, ponto a testar
     * @return Verdadeiro caso c esteja contido no perímetro dos pontos b e c
     */
    public static boolean isPerimeter(Vector a, Vector b, Vector c) {
        if (a == null || b == null || c == null) return false;
        Vector lowerLeft = new Vector();
        Vector upperRight = new Vector();

        if (a.x <= b.x) {
            lowerLeft.x = a.x;
            upperRight.x = b.x;
        }
        else {
            lowerLeft.x = b.x;
            upperRight.x = a.x;
        }

        if (a.y <= b.y) {
            lowerLeft.y = a.y;
            upperRight.y = b.y;
        }
        else {
            lowerLeft.y = b.y;
            upperRight.y = a.y;
        }

        Vector upperLeft = new Vector(lowerLeft.x, upperRight.y);
        Vector lowerRight = new Vector(upperRight.x, lowerLeft.y);

        return isBetween(lowerLeft, upperLeft, c) ||
                isBetween(upperLeft, upperRight, c) ||
                isBetween(upperRight, lowerRight, c) ||
                isBetween(lowerRight, lowerLeft, c);
    }

    /**
     * Verifica se os pontos c e d estão contido em bordas opostas do perímetro de um quadrado delimitado pelos pontos a e b
     * @param a ponto A
     * @param b ponto B
     * @param c ponto C, ponto a testar
     * @param d ponto D, ponto a testar
     * @return Verdadeiro caso c e d estejam contidos em bordas opostas entre os pontos b e c
     */
    public static boolean areOpposingInPerimeter(Vector a, Vector b, Vector c, Vector d) {
        if (a == null || b == null || c == null) return false;
        Vector lowerLeft = new Vector();
        Vector upperRight = new Vector();

        if (a.x <= b.x) {
            lowerLeft.x = a.x;
            upperRight.x = b.x;
        }
        else {
            lowerLeft.x = b.x;
            upperRight.x = a.x;
        }

        if (a.y <= b.y) {
            lowerLeft.y = a.y;
            upperRight.y = b.y;
        }
        else {
            lowerLeft.y = b.y;
            upperRight.y = a.y;
        }

        Vector upperLeft = new Vector(lowerLeft.x, upperRight.y);
        Vector lowerRight = new Vector(upperRight.x, lowerLeft.y);

        return (isBetween(lowerLeft, upperLeft, c) && isBetween(lowerRight, upperRight, d)) ||
                (isBetween(lowerLeft, upperLeft, d) && isBetween(lowerRight, upperRight, c)) ||
                (isBetween(lowerLeft, lowerRight, c) && isBetween(upperLeft, upperRight, d)) ||
                (isBetween(lowerLeft, lowerRight, d) && isBetween(upperLeft, upperRight, c));
    }

    /**
     * Verifica se os pontos a e b são adjacentes
     * @param a ponto A
     * @param b ponto B
     * @return Verdadeiro caso os pontos a e b sejam adjacentes
     */
    public static boolean isAdjacent(Vector a, Vector b) {
        if (a == null || b == null || a.equals(b)) return false;
        return (a.x == b.x && (a.y - 1 == b.y || a.y + 1 == b.y)) ||
                (a.y == b.y && (a.x - 1 == b.x || a.x + 1 == b.x));
    }

    /**
     * Obtém o ponto adjacente fornecida uma direção
     * @param a ponto A
     * @param direction Direção a utilizar
     * @return Vetor da posição na direção fornecida caso válida, caso contrário retorna null
     */
    public static Vector getAdjacent(Vector a, Direction direction) {
        if (a == null || direction == null) return null;

        Vector directionVector = new Vector();
        switch (direction) {
            case LEFT:
                directionVector = new Vector(-1,0);
                break;
            case RIGHT:
                directionVector = new Vector(1,0);
                break;
            case UP:
                directionVector = new Vector(0,1);
                break;
            case DOWN:
                directionVector = new Vector(0,-1);
                break;
        }
        return new Vector(a.x + directionVector.x, a.y + directionVector.y);
    }

    /**
     * Obtém o ponto adjacente fornecida uma direção
     * @param a ponto A
     * @param b ponto B
     * @return Vetor da posição adjacente na direção fornecida caso válida, caso contrário retorna null
     */
    public static float getDistance(Vector a, Vector b) {
        if (a == null || b == null ) return -1;
        return (float)Math.sqrt(Math.pow((b.x-a.x),2)+Math.pow((b.y-a.y),2));
    }

    /**
     * Verifica se o ponto b está dentro do cone de direção fornecida e com o ângulo fornecido
     * @param a ponto A, ponto de origem
     * @param b ponto B, ponto a testar
     * @param direction direção que o ponto A tá a apontar
     * @param angle ângulo a testar
     * @return Verdadeiro caso o ponto b esteja dentro do cone de direção fornecida e com o ângulo fornecido
     */
    public static boolean isWithinAngle(Vector a, Vector b, Direction direction, float angle) {
        if (a == null || b == null || direction == null || angle <= 0f) return false;
        if(angle >= 90f) angle = 89.99f;

        float vectorTangent = 0f;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            vectorTangent = getTangent(a, b);
        }
        else {
            vectorTangent = getTangent(b, a);
        }

        float angleTangent = (float)Math.tan((Math.PI/180f)*angle);

        return vectorTangent >= (1f/angleTangent);
    }

    private static float getTangent(Vector a, Vector b) {
        if(a == null || b == null) return 0;
        return Math.abs((float)b.y - (float)a.y) / ((float)b.x - (float)a.x);
    }

    /**
     * Obtém a direção que aponta do ponto a para o b, caso estes dois sejam adjacentes
     * @param a ponto A
     * @param b ponto B
     * @return Direção do ponto b em relação ao ponto a, caso não sejam adjacentes retorna null
     */
    public static Direction getDirection(Vector a, Vector b) {
        if(!isAdjacent(a,b)) return null;
        for(Direction direction : Direction.values()) {
            Vector adjacent = getAdjacent(a, direction);
            if(adjacent.equals(b)) return direction;
        }
        return null;
    }

    /**
     * Verifica se este vetor e o fornecido são iguais
     * @param other vetor a comparar
     * @return Verdadeiro caso este e o fornecido sejam iguais
     */
    public boolean equals(Vector other) {
        return (this.x == other.x && this.y == other.y);
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(!(object instanceof Vector)) return false;
        return this.equals((Vector)object);
    }

    @Override
    public int hashCode() {
        return (40000 * x) + y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
