package Classes;

import java.util.ArrayList;

/**
 * Representa um transportador de carga unitária
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class UnitLoadCarrier extends Vehicle implements CargoTransporter<CardboardBox>, Tickable  {
    private Pallet pallet;
    private int speed;

    private Vector destination;
    private boolean loadingIntent;
    private Request request;

    /**
     * Constructor de UnitLoadCarrier
     * @param grid Grelha a que pertence
     * @param position Posição inicial
     */
    public UnitLoadCarrier(Grid grid, Vector position) {
        super(grid, position);
        pallet = new Pallet();
        speed = 0;
        destination = null;
        loadingIntent = true;
        request = null;
    }

    @Override
    public float getCapacity() {
        return -1;
    }

    @Override
    public float getObjectsWeight() {
        return pallet.getWeight();
    }

    @Override
    public ArrayList<CardboardBox> getObjects() { return pallet.getCardboardBoxes(); }

    @Override
    public CardboardBox getObject(int index) {
        return pallet.getCardboardBox(index);
    }

    @Override
    public boolean addObject(CardboardBox object) {
        return pallet.addBox(object);
    }

    @Override
    public boolean removeObject(CardboardBox object) {
        return pallet.removeBox(object);
    }

    @Override
    public void tick() {
        if (request == null) {
            System.out.println("Carro de id " + id + " sem um request, a pedir um agora.");
            destination = null;
            loadingIntent = true;
            request = grid.getWarehouse().hasProductToGet();
            if (request == null) return;
            if (request.source instanceof LoadSiteInfo) {
                destination = ((LoadSiteInfo)request.source).getLoadPosition();
            }
            else if (request.source instanceof Rack) {
                destination = ((Rack)request.source).getLoadPosition();
            }

            if (destination == null) {
                request = null;
                return;
            }
            loadingIntent = true;

            System.out.println("Carro de id " + id + " obteve pedido: " + request);

            path = grid.getPathWithSensorInfo(getPosition(), destination, getVisibleOccupiedPositions());
            pathIndex = 0;
            forward = true;
        }

        if (path == null || pathIndex < 0 || pathIndex >= path.size()-1 || grid.isOccupied(path.get(pathIndex+1))) {
            System.out.println("Carro de id " + id + " a recalcular caminho.");
            path = grid.getPathWithSensorInfo(getPosition(), destination, getVisibleOccupiedPositions());
            pathIndex = 0;
            forward = true;
        }

        if (!getPosition().equals(destination)) {
            if (path == null) return;
            if(pathIndex >= path.size()-1) {
                path = null;
                return;
            }

            if(speed < 0) {
                speed = 1;
            }
            else if(speed < 3) {
                speed++;
            }

            Direction lastDirection = Vector.getDirection(path.get(pathIndex), path.get(pathIndex+1));
            for(int i = 0; i < speed && pathIndex < path.size()-1; i++) {
                Direction newDirection = Vector.getDirection(path.get(pathIndex), path.get(pathIndex+1));
                if(lastDirection != newDirection) {
                    break;
                }
                int moveCount = move(newDirection);
                if (moveCount > 0) pathIndex++;
                else {
                    path = null;
                    break;
                }
            }
        }

        if (getPosition().equals(destination)) {
            if (loadingIntent) {
                Product product = null;
                if (request.source instanceof LoadSiteInfo) {
                    product = ((LoadSiteInfo)request.source).getProduct(request.product);
                }
                else if (request.source instanceof Rack) {
                    product = ((Rack)request.source).getProduct(request.product);
                }

                if (product == null) {
                    System.out.println("Carro de id " + id + " cancelou o pedido " + request + " uma vez que é agora inválido.");
                    request = null;
                    destination = null;
                    return;
                }

                request.product = product;

                if (request.source instanceof LoadSiteInfo) {
                    System.out.println("Carro de id " + id + " carregou produto do local de recolha");
                }
                else if (request.source instanceof Rack) {
                    System.out.println("Carro de id " + id + " carregou produto da prateleira da célula " + ((Rack)request.source).getPosition());
                }

                if (request.destination instanceof LoadSiteInfo) {
                    destination = ((LoadSiteInfo)request.destination).getLoadPosition();
                }
                else if (request.destination instanceof Rack) {
                    destination = ((Rack)request.destination).getLoadPosition();
                }



                path = null;
                loadingIntent = false;
            }
            else {
                if (request.destination instanceof LoadSiteInfo) {
                    ((LoadSiteInfo)request.destination).loadSite.deliverProduct(request.product);
                    System.out.println("Carro de id " + id + " entregou produto ao local de entrega.");
                }
                else if (request.destination instanceof Rack) {
                    ((Rack)request.destination).addProduct(request.product);
                    System.out.println("Carro de id " + id + " entregou produto à prateleira da célula " + ((Rack)request.destination).getPosition());
                }

                request = null;
                path = null;
                loadingIntent = true;
            }
        }
    }
}
