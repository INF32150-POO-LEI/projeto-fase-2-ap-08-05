 

import java.util.ArrayList;

/**
 * Representa um veículo genérico
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public abstract class Vehicle extends Positionable {
    /**
     * Contagem total
     */
    protected static int COUNT;

    /**
     * ID
     */
    protected int id;
    /**
     * Caminho
     */
    protected ArrayList<Vector> path;
    /**
     * Indice to caminho
     */
    protected int pathIndex;
    /**
     * Direção do movimento
     */
    protected boolean forward;
    /**
     * Direção do carro
     */
    protected Direction direction;

    /**
     * Construtor do Vehicle
     * @param grid Grelha a que pertence
     * @param position Posição inicial
     */
    public Vehicle(Grid grid, Vector position) {
        super(grid, position);
        id = COUNT++;
        path = new ArrayList<>();
        pathIndex = -1;
        forward = true;
        direction = Direction.UP;
    }

    /**
     * Tenta mover o veículo na direção fornecida
     * @param direction direção a mover
     * @return 1 caso o veículo tenha movido, caso contrário, retorna 0
     */
    public int move(Direction direction) {
        if(direction == null) return 0;
        Vector position = getPosition();
        Vector newPosition = Vector.getAdjacent(position, direction);
        return moveToPosition(newPosition);
    }

    /**
     * Tenta mover o veículo para a posição fornecida
     * @param position posição de destino
     * @return 1 caso o veículo tenha movido, caso contrário, retorna 0
     */
    protected int moveToPosition(Vector position) {
        if(position == null) return 0;
        Vector oldPosition = getPosition();
        boolean succeeded = setPosition(position);
        Vector newPosition = getPosition();
        if(succeeded) {
            direction = Vector.getDirection(oldPosition, newPosition);
            System.out.println("Carro de id " + id + " andou da célula " + oldPosition + " para a célula " + newPosition);
        }

        return succeeded ? 1 : 0;
    }

    private ArrayList<Vector> getLidarOccupiedPositions() {
        ArrayList<Vector> occupiedPositions = new ArrayList<>();
        Vector position = getPosition();
        switch (direction) {
            case UP:
                for (int i = position.y+1; i < position.y+11; i++) {
                    for (int k = position.x-6; k < position.x+6; k++) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.isWithinAngle(position, newPosition, Direction.UP, 15f) &&
                                Vector.getDistance(position, newPosition) < 10 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case DOWN:
                for (int i = position.y-1; i > position.y-11; i--) {
                    for (int k = position.x+6; k > position.x-6; k--) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.isWithinAngle(position, newPosition, Direction.DOWN, 15f) &&
                                Vector.getDistance(position, newPosition) < 10 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case LEFT:
                for (int i = position.x-1; i > position.x-11; i--) {
                    for (int k = position.y-6; k < position.y+6; k++) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.isWithinAngle(position, newPosition, Direction.LEFT, 15f) &&
                                Vector.getDistance(position, newPosition) < 10 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case RIGHT:
                for (int i = position.x+1; i < position.x+11; i++) {
                    for (int k = position.y+6; k > position.y-6; k--) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.isWithinAngle(position, newPosition, Direction.RIGHT, 15f) &&
                                Vector.getDistance(position, newPosition) < 10 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
        }
        return occupiedPositions;
    }

    private ArrayList<Vector> getSonicSensorOccupiedPositions() {
        ArrayList<Vector> occupiedPositions = new ArrayList<>();
        Vector position = getPosition();
        switch (direction) {
            case UP:
                for (int i = position.y; i < position.y+4; i++) {
                    for (int k = position.x-4; k < position.x+4; k++) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.getDistance(position, newPosition) < 4 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case DOWN:
                for (int i = position.y; i > position.y-4; i--) {
                    for (int k = position.x+4; k > position.x-4; k--) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.getDistance(position, newPosition) < 4 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case LEFT:
                for (int i = position.x; i > position.x-4; i--) {
                    for (int k = position.y-4; k < position.y+4; k++) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.getDistance(position, newPosition) < 4 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
            case RIGHT:
                for (int i = position.x; i < position.x+4; i++) {
                    for (int k = position.y+4; k > position.y-4; k--) {
                        Vector newPosition = new Vector(k,i);
                        if (Vector.getDistance(position, newPosition) < 4 &&
                                grid.isValidUsable(newPosition) &&
                                grid.isOccupied(newPosition)) occupiedPositions.add(newPosition);
                    }
                }
                break;
        }
        return occupiedPositions;
    }

    /**
     * Obtém as posições ocupadas na grelha que este veículo tem conhecimento
     * @return ArrayList de posições ocupadas
     */
    public ArrayList<Vector> getVisibleOccupiedPositions(){
        ArrayList<Vector> occupiedPositions = new ArrayList<>();
        occupiedPositions.addAll(getLidarOccupiedPositions());
        occupiedPositions.addAll(getSonicSensorOccupiedPositions());
        for(Direction direction1 : Direction.values()) {
            Vector adjacentPosition1 = Vector.getAdjacent(getPosition(), direction1);
            if(grid.isOccupied(adjacentPosition1) && !occupiedPositions.contains(adjacentPosition1)){
                occupiedPositions.add(adjacentPosition1);
            }
            for(Direction direction2 : Direction.values()) {
                Vector adjacentPosition2 = Vector.getAdjacent(adjacentPosition1, direction2);
                if(grid.isOccupied(adjacentPosition2) && !occupiedPositions.contains(adjacentPosition2)){
                    occupiedPositions.add(adjacentPosition2);
                }
            }
        }
        return occupiedPositions;
    }

    private boolean isWithinLidarVision(Vector checkPosition) {
        if(checkPosition == null) return false;
        Vector position = getPosition();
        switch (direction) {
            case UP:
                return Vector.isWithinAngle(position, checkPosition, Direction.UP, 15f) &&
                        Vector.getDistance(position, checkPosition) < 10;
            case DOWN:
                return Vector.isWithinAngle(position, checkPosition, Direction.DOWN, 15f) &&
                        Vector.getDistance(position, checkPosition) < 10;
            case LEFT:
                return Vector.isWithinAngle(position, checkPosition, Direction.LEFT, 15f) &&
                        Vector.getDistance(position, checkPosition) < 10;
            case RIGHT:
                return Vector.isWithinAngle(position, checkPosition, Direction.RIGHT, 15f) &&
                        Vector.getDistance(position, checkPosition) < 10;
        }
        return false;
    }

    private boolean isWithinSonicSensorVision(Vector checkPosition) {
        if(checkPosition == null) return false;
        Vector position = getPosition();
        switch (direction) {
            case UP:
                return Vector.isBetween(
                        new Vector(position.x-4,position.y),
                        new Vector(position.x+4,position.y+4),
                        checkPosition) &&
                        Vector.getDistance(position, checkPosition) < 4;
            case DOWN:
                return Vector.isBetween(
                        new Vector(position.x-4,position.y-4),
                        new Vector(position.x+4,position.y),
                        checkPosition) &&
                        Vector.getDistance(position, checkPosition) < 4;
            case LEFT:
                return Vector.isBetween(
                        new Vector(position.x-4,position.y-4),
                        new Vector(position.x,position.y+4),
                        checkPosition) &&
                        Vector.getDistance(position, checkPosition) < 4;
            case RIGHT:
                return Vector.isBetween(
                        new Vector(position.x,position.y-4),
                        new Vector(position.x+4,position.y+4),
                        checkPosition) &&
                        Vector.getDistance(position, checkPosition) < 4;
        }
        return false;
    }

    /**
     * Verifica se a posição fornecida está dentro do raio de deteção dos sensores
     * @param position posição a testar
     * @return Verdadeiro caso a posição fornecida esteja dentro do raio de deteção dos sensores
     */
    public boolean isWithinSensorVision(Vector position) {
        if(position == null) return false;
        return isWithinLidarVision(position) || isWithinSonicSensorVision(position);
    }

    private boolean isWithinCameraVision(Vector checkPosition) {
        if(checkPosition == null) return false;
        Vector position = getPosition();
        switch (direction) {
            case UP:
                return Vector.isWithinAngle(position, checkPosition, Direction.UP, 45f) &&
                        Vector.getDistance(position, checkPosition) < 15;
            case DOWN:
                return Vector.isWithinAngle(position, checkPosition, Direction.DOWN, 45f) &&
                        Vector.getDistance(position, checkPosition) < 15;
            case LEFT:
                return Vector.isWithinAngle(position, checkPosition, Direction.LEFT, 45f) &&
                        Vector.getDistance(position, checkPosition) < 15;
            case RIGHT:
                return Vector.isWithinAngle(position, checkPosition, Direction.RIGHT, 45f) &&
                        Vector.getDistance(position, checkPosition) < 15;
        }
        return false;
    }

    private Positionable getObject(Vector position) {
        if (position == null) return null;
        if (!isWithinSensorVision(position)) return null;
        if (!isWithinCameraVision(position)) return null;
        if (!grid.isOccupied(position)) return null;

        Positionable object = grid.getPositionable(position);

        return object;
    }

    private Class getObjectType(Vector position) {
        if(position == null) return null;

        Positionable object = getObject(position);
        if(object == null) return null;

        return object.getClass();
    }
}
