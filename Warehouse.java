 

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa um armazém
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Warehouse {
    private Grid grid;
    private LoadSiteInfo pickupArea;
    private LoadSiteInfo deliveryArea;

    private ArrayList<Rack> racks;

    private LinkedList<Request> requests;

    /**
     * Construtor da Warehouse
     * @param grid Grelha que contém
     * @param pickupArea Local de Recolha
     * @param deliveryArea Local de Entrega
     */
    public Warehouse(Grid grid, LoadSiteInfo pickupArea, LoadSiteInfo deliveryArea) {
        this.grid = grid;
        this.pickupArea = pickupArea;
        this.deliveryArea = deliveryArea;
        this.racks = new ArrayList<>();
        this.requests = new LinkedList<>();
    }

    /**
     * Retorna a grelha deste armazém
     * @return Grelha do armazém
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Retorna o local de recolha deste armazém
     * @return Local de recolha do armazém
     */
    public LoadSiteInfo getPickupArea() {
        return pickupArea;
    }

    /**
     * Retorna o local de entrega deste armazém
     * @return Local de entrega do armazém
     */
    public LoadSiteInfo getDeliveryArea() {
        return deliveryArea;
    }

    /**
     * Obter a lista de todas as prateleiras
     * @return lista das prateleiras
     */
    public ArrayList<Rack> getRacks() {
        return racks;
    }

    /**
     * Tenta registar um novo pedido de transporte de items
     * @param request Pedido a registar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean registerRequest(Request request) {
        if(request == null) return false;
        return requests.add(request);
    }

    /**
     * Tenta registar uma prateleira
     * @param rack Prateleira a registar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean registerRack(Rack rack) {
        if(rack == null) return false;
        if(racks.contains(rack)) return false;
        racks.add(rack);
        return true;
    }

    /**
     * Verifica se existem pedidos para os veículos realizarem
     * @return Pedido a ser realizado, caso não exista retorna null
     */
    public Request hasProductToGet() {
        if(requests.size() > 0) {
            return requests.remove();
        }
        else {
            if(pickupArea.loadSite.hasProducts()) {
                Random random = new Random();
                Rack rack = null;
                rack = racks.get(random.nextInt(racks.size()));

                Request request = new Request(pickupArea, rack, null);
                return request;
            }
        }
        return null;
    }
}
