import java.util.ArrayList;

/**
 * Representa um reboque
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class TowingVehicle extends Vehicle implements Tickable {
    private Trolley trolley;

    private Vector destination;
    private boolean loadingIntent;
    private Request request;

    /**
     * Construtor da TowingVehicle
     * @param grid Grelha a que pertence
     * @param position Posição inicial
     */
    public TowingVehicle(Grid grid, Vector position) {
        super(grid, position);
        trolley = null;
        destination = null;
        loadingIntent = true;
        request = null;
    }

    /**
     * Obter informação se o carro está a transportar mercadoria
     * @return Verdadeiro se está a transportar
     */
    public boolean isCarrying() {
        return request != null && request.product != null && !loadingIntent;
    }
    
    /**
     * Tenta anexar um carrinho de transporte a este reboque
     * @param trolley Atrelado a anexar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean attachTrolley(Trolley trolley) {
        if(this.trolley == trolley) return false;
        if(trolley == null) return false;
        Vector thisPosition = grid.getPosition(this);
        Vector trolleyPosition = grid.getPosition(trolley);
        if (!Vector.isAdjacent(thisPosition, trolleyPosition)) return false;
        this.trolley = trolley;
        System.out.println("Carrinho na célula " + trolley.getPosition() + " está agora a ser rebocado pelo reboque de id " + id + " na célula " + getPosition());
        return true;
    }

    /**
     * Tenta desanexar um carrinho de transporte deste reboque
     * @return Verdadeiro caso bem sucedido
     */
    public boolean detachTrolley() {
        if(trolley == null) return false;
        trolley = null;
        return true;
    }

    @Override
    public int move(Direction direction) {
        if(direction == null) return 0;
        Vector position = getPosition();
        Vector newPosition = Vector.getAdjacent(position, direction);
        int moveCount = moveToPosition(newPosition);

        if (moveCount == 0) {
            return 0;
        }
        if (trolley == null) return 1;

        System.out.println("Carrinho rebocado da célula " + trolley.getPosition() + " para a célula " + position);
        trolley.setPosition(position);
        return 1;
    }

    @Override
    public void tick() {
        if (request == null) {
            System.out.println("Carro de id " + id + " sem um request, a pedir um agora.");
            destination = null;
            loadingIntent = true;
            request = grid.getWarehouse().hasProductToGet();
            if (request == null) {
                if(getPosition().y == 0) return;
                Vector freeDock = null;
                boolean invalidSpot = false;
                for(int i = 0; i < grid.getUsableArea().maximumXY.x && freeDock == null; i++) {
                    Vector dockPosition = new Vector(i, 0);
                    invalidSpot = grid.isOccupied(Vector.getAdjacent(dockPosition, Direction.RIGHT)) || grid.isOccupied(Vector.getAdjacent(dockPosition, Direction.LEFT));
                    if(invalidSpot) continue;
                    if(!grid.isOccupied(dockPosition)) freeDock = dockPosition;
                }
                if(freeDock == null || freeDock.equals(getPosition())) return;
                path = grid.getPathWithSensorInfo(getPosition(), freeDock, getVisibleOccupiedPositions());
                pathIndex = 0;
                forward = true;
                if(path == null) return;
                Direction newDirection = Vector.getDirection(path.get(0), path.get(1));
                move(newDirection);
                return;
            }
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
