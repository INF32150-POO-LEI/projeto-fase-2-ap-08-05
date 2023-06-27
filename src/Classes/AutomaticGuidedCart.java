package Classes;

import java.util.ArrayList;

/**
 * Representa um carrinho guiado automaticamente
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class AutomaticGuidedCart extends Vehicle implements CargoTransporter<Storage>, Tickable {
    /**
     * Capacidade de carga
     */
    protected static final float CAPACITY = 100f;
    /**
     * Carga a ser transportada
     */
    protected ArrayList<Storage> storages;

    /**
     * Destino
     */
    protected Vector destination;
    /**
     * Intenção de carga ou descarga
     */
    protected boolean loadingIntent;
    /**
     * Pedido atual
     */
    protected Request request;

    /**
     * Construtor de AutomaticGuidedCart
     * @param grid grelha a que pertence
     * @param position posição inicial
     */
    public AutomaticGuidedCart(Grid grid, Vector position) {
        super(grid, position);
        storages = new ArrayList<>();
        destination = null;
        loadingIntent = true;
        request = null;
    }

    @Override
    public float getCapacity() {
        return CAPACITY;
    }

    @Override
    public float getObjectsWeight() {
        float total = 0;
        for(Storage storage : storages) total += storage.getWeight();
        return total;
    }

    @Override
    public ArrayList<Storage> getObjects() {
        return storages;
    }

    @Override
    public Storage getObject(int index) {
        if(index < 0 || index >= storages.size()) return null;
        return storages.get(index);
    }

    @Override
    public boolean addObject(Storage object) {
        if(object == null) return false;
        if(!(object instanceof Bag || object instanceof Box)) return false;
        if(getObjectsWeight() + object.getWeight() > CAPACITY) return false;
        return storages.add(object);
    }

    @Override
    public boolean removeObject(Storage object) {
        if(!storages.contains(object)) return false;
        return storages.remove(object);
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
            if (pathIndex >= path.size()-1) return;

            Direction newDirection = Vector.getDirection(path.get(pathIndex), path.get(pathIndex+1));
            int moveCount = move(newDirection);
            if (moveCount > 0) pathIndex++;
            else path = null;
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
