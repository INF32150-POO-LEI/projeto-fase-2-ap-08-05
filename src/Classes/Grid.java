package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grelha onde estarão os outros objetos
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Grid {
    private Area usableArea;
    private Area wallArea;

    private HashMap<Vector, Positionable> grid;
    private HashMap<Positionable, ArrayList<Vector>> reversedGrid;

    private Warehouse warehouse;

    /**
     *
     * @param maximumX X Máximo
     * @param maximumY Y Máximo
     */
    public Grid(int maximumX, int maximumY) {
        Vector minimumXY = new Vector(0,0);
        Vector maximumXY = new Vector();
        if(maximumX >= 0) maximumXY.x = maximumX;
        if(maximumY >= 0) maximumXY.y = maximumY;

        usableArea = new Area(minimumXY, maximumXY);
        Vector wallMinimumXY = new Vector(minimumXY.x-1,minimumXY.y-1);
        Vector wallMaximumXY = new Vector(maximumXY.x+1, maximumXY.y+1);
        wallArea = new Area(wallMinimumXY, wallMaximumXY);

        grid = new HashMap<>();
        reversedGrid = new HashMap<>();

        warehouse = null;
    }

    /**
     * Define o armazém a que pertence, apenas permite que seja executado uma vez
     * @param warehouse Warehouse a definir
     * @return Verdadeiro se o armazém foi definido
     */
    public boolean setWarehouse(Warehouse warehouse) {
        if(warehouse == null || this.warehouse != null) return false;
        this.warehouse = warehouse;
        return true;
    }

    /**
     * Retorna o armazém a que pertence
     * @return Armazém a que pertence
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Verifica se a posição fornecida está dentro da área válida do armazém
     * @param position Posição a testar
     * @return Verdadeiro se a posição é válida dentro do armazém
     */
    public boolean isValidUsable(Vector position) {
        return usableArea.isWithin(position);
    }

    /**
     * Verifica se a posição fornecida está ocupada
     * @param position Posição a testar
     * @return Verdadeiro se a posição está ocupada
     */
    public boolean isOccupied(Vector position) {
        return grid.get(position) != null;
    }

    /**
     * Retorna o objeto que está na posição fornecida
     * @param position Posição a obter
     * @return Objeto que está na posição fornecida, caso exista, caso contrário retorna null
     */
    public Positionable getPositionable(Vector position) {
        return grid.get(position);
    }

    /**
     * Tenta adicionar um objeto à grelha na posição fornecida
     * @param positionable Positionable a adicionar
     * @param position Posição a preencher
     * @return Verdadeiro se o objeto foi adicionado à grelha
     */
    public boolean addPositionable(Positionable positionable, Vector position) {
        if (position == null || positionable == null) return false;
        if (!Vector.isBetween(usableArea.minimumXY, usableArea.maximumXY, position)) return false;
        if (grid.containsKey(position)) return false;

        if (reversedGrid.containsKey(positionable)) {
            if (positionable instanceof Rack) {
                if(reversedGrid.get(positionable).size() == 2) return false;
                if(!Vector.isAdjacent(reversedGrid.get(positionable).get(0), position)) return false;
                grid.put(position, positionable);
                reversedGrid.get(positionable).add(position);
                return true;
            }
            else return false;
        }
        else {
            grid.put(position, positionable);
            ArrayList<Vector> newPositions = new ArrayList<>();
            newPositions.add(position);
            reversedGrid.put(positionable, newPositions);
            return true;
        }
    }

    /**
     * Verifica se o objeto fornecido existe na grelha
     * @param positionable Positionable a testar
     * @return Verdadeiro se o objeto existe na grelha
     */
    public boolean containsPositionable(Positionable positionable) {
        return reversedGrid.containsKey(positionable);
    }

    /**
     * Retorna o vetor da posição do objeto fornecido caso este exista
     * @param positionable Positionable a obter
     * @return Vetor de posição do objeto, caso não exista, retorna null
     */
    public Vector getPosition(Positionable positionable) {
        ArrayList<Vector> list = reversedGrid.get(positionable);
        if(list == null) return null;
        return list.get(0);
    }

    /**
     * Tenta remover da grelha o objeto fornecido
     * @param positionable Positionable a remover
     * @return Verdadeiro se o objeto existia e foi removido
     */
    public boolean removePositionable(Positionable positionable) {
        if (!reversedGrid.containsKey(positionable)) return false;
        ArrayList<Vector> list = reversedGrid.get(positionable);
        for (Vector vector : list) {
            grid.remove(vector);
        }
        reversedGrid.remove(positionable);
        return true;
    }

    /**
     * Tenta mover um objeto de posição
     * @param positionable Positionable a mover
     * @param position Posição nova
     * @return Verdadeiro se o objeto existia na grelha, a posição fornecida estava livre e foi movido com sucesso
     */
    public boolean movePositionable(Positionable positionable, Vector position) {
        if (position == null || positionable == null) return false;
        if (!Vector.isBetween(usableArea.minimumXY, usableArea.maximumXY, position)) return false;
        if (!reversedGrid.containsKey(positionable)) return false;
        if (grid.containsKey(position)) return false;
        if (reversedGrid.get(positionable).get(0).equals(position)) return false;
        if (positionable instanceof Rack) return false;

        removePositionable(positionable);
        addPositionable(positionable, position);
        reversedGrid.get(positionable).clear();
        reversedGrid.get(positionable).add(position);

        return true;
    }

    /**
     * Tenta obter um caminho do ponto inicial até ao final
     * @param startPosition Posição de partida
     * @param endPosition Posição de chegada
     * @return ArrayList de Vetor, caso exista um caminho válido, caso contrário retorna null
     */
    public ArrayList<Vector> getPath(Vector startPosition, Vector endPosition) {
        if (startPosition == null || endPosition == null || startPosition == endPosition) return null;
        if (!isValidUsable(startPosition) || !isValidUsable(endPosition)) return null;

        HashMap<Vector, Integer> distanceGrid = new HashMap<>();

        for (int i = usableArea.minimumXY.x; i <= usableArea.maximumXY.x; i++) {
            for (int k = usableArea.minimumXY.y; k <= usableArea.maximumXY.y; k++) {
                Vector position = new Vector(i,k);
                if (isOccupied(position)) distanceGrid.put(position,-1);
                else distanceGrid.put(position,-2);
            }
        }

        LinkedList<Vector> explorationQueue = new LinkedList<>();
        explorationQueue.add(endPosition);
        LinkedList<Integer> explorationQueueValues = new LinkedList<>();
        explorationQueueValues.add(0);

        distanceGrid.put(endPosition, 0);
        boolean pathFound = false;
        while (explorationQueue.size() > 0 && !pathFound) {
            Vector currentPosition = explorationQueue.getFirst();
            int currentDistance = explorationQueueValues.getFirst();
            for(Direction direction : Direction.values()) {
                Vector adjacentPosition = Vector.getAdjacent(currentPosition, direction);
                int adjacentDistance = currentDistance+1;
                if(distanceGrid.containsKey(adjacentPosition) &&
                        (distanceGrid.get(adjacentPosition) == -2 ||
                                (distanceGrid.get(adjacentPosition) == -1 && adjacentPosition.equals(startPosition)))) {
                    distanceGrid.put(adjacentPosition, adjacentDistance);
                    explorationQueue.add(adjacentPosition);
                    explorationQueueValues.add(adjacentDistance);
                }
                if(explorationQueue.getLast().equals(startPosition)) pathFound = true;
            }
            explorationQueue.remove();
            explorationQueueValues.remove();
        }

        if(distanceGrid.get(startPosition) == -2) return null;

        LinkedList<Vector> path = new LinkedList<>();
        path.add(startPosition);

        while (!path.getLast().equals(endPosition)) {
            Vector currentPosition = path.getLast();
            int currentDistance = distanceGrid.get(currentPosition);
            boolean nextPathFound = false;
            for (Direction direction : Direction.values()) {
                Vector adjacentPosition = Vector.getAdjacent(currentPosition, direction);
                if(!distanceGrid.containsKey(adjacentPosition) || distanceGrid.get(adjacentPosition) < 0) continue;
                int adjacentDistance = distanceGrid.get(adjacentPosition);
                if (adjacentDistance < currentDistance) {
                    path.add(adjacentPosition);
                    nextPathFound = true;
                    break;
                }
            }

            if(!nextPathFound){
                return null;
            }
        }

        return new ArrayList<>(path);
    }

    /**
     * Tenta obter um caminho do ponto inicial até ao final fornecida informação de obstáculos
     * @param startPosition Posição de partida
     * @param endPosition Posição de chegada
     * @param occupiedPositions Posições ocupadas
     * @return ArrayList de Vetor, caso exista um caminho válido, caso contrário retorna null
     */
    public ArrayList<Vector> getPathWithSensorInfo(Vector startPosition, Vector endPosition, ArrayList<Vector> occupiedPositions) {
        if (startPosition == null || endPosition == null || startPosition == endPosition) return null;
        if (!isValidUsable(startPosition) || !isValidUsable(endPosition)) return null;

        HashMap<Vector, Integer> distanceGrid = new HashMap<>();

        for (int i = usableArea.minimumXY.x; i <= usableArea.maximumXY.x; i++) {
            for (int k = usableArea.minimumXY.y; k <= usableArea.maximumXY.y; k++) {
                Vector position = new Vector(i,k);
                if (occupiedPositions.contains((position))) distanceGrid.put(position,-1);
                else distanceGrid.put(position,-2);
            }
        }

        LinkedList<Vector> explorationQueue = new LinkedList<>();
        explorationQueue.add(endPosition);
        LinkedList<Integer> explorationQueueValues = new LinkedList<>();
        explorationQueueValues.add(0);

        distanceGrid.put(endPosition, 0);
        boolean pathFound = false;
        while (explorationQueue.size() > 0 && !pathFound) {
            Vector currentPosition = explorationQueue.getFirst();
            int currentDistance = explorationQueueValues.getFirst();
            for(Direction direction : Direction.values()) {
                Vector adjacentPosition = Vector.getAdjacent(currentPosition, direction);
                int adjacentDistance = currentDistance+1;
                if(distanceGrid.containsKey(adjacentPosition) &&
                        (distanceGrid.get(adjacentPosition) == -2 ||
                                (distanceGrid.get(adjacentPosition) == -1 && adjacentPosition.equals(startPosition)))) {
                    distanceGrid.put(adjacentPosition, adjacentDistance);
                    explorationQueue.add(adjacentPosition);
                    explorationQueueValues.add(adjacentDistance);
                }
                if(explorationQueue.getLast().equals(startPosition)) pathFound = true;
            }
            explorationQueue.remove();
            explorationQueueValues.remove();
        }

        if(distanceGrid.get(startPosition) == -2) return null;

        LinkedList<Vector> path = new LinkedList<>();
        path.add(startPosition);

        while (!path.getLast().equals(endPosition)) {
            Vector currentPosition = path.getLast();
            int currentDistance = distanceGrid.get(currentPosition);
            boolean nextPathFound = false;
            for (Direction direction : Direction.values()) {
                Vector adjacentPosition = Vector.getAdjacent(currentPosition, direction);
                if(!distanceGrid.containsKey(adjacentPosition) || distanceGrid.get(adjacentPosition) < 0) continue;
                int adjacentDistance = distanceGrid.get(adjacentPosition);
                if (adjacentDistance < currentDistance) {
                    path.add(adjacentPosition);
                    nextPathFound = true;
                    break;
                }
            }

            if(!nextPathFound){
                return null;
            }
        }

        return new ArrayList<>(path);
    }
}
