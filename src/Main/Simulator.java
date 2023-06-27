package Main;

import Classes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Representa uma simulação
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Simulator {
    private HashMap<Integer, ArrayList<String>> actions;

    Grid grid;
    LoadSiteInfo pickup;
    LoadSiteInfo delivery;
    Warehouse warehouse;

    ArrayList<Tickable> tickables;

    int tickCount;

    /**
     * Construtor de Simulator
     * @param actions Ações a executar
     */
    public Simulator(HashMap<Integer, ArrayList<String>> actions) {
        tickCount = 0;
        this.actions = actions;
        tickables = new ArrayList<>();

        System.out.println("----- Executing now tick: 0/setup -----");

        try {
            ArrayList<Integer> keys = new ArrayList<>(actions.keySet());
            Collections.sort(keys);
            System.out.println("Comandos detetados para os seguintes ticks no ficheiro CSV: " + keys);

            if(keys.contains(0)) {
                for (String line : actions.get(0)) {
                    System.out.println("Tick: " + tickCount + " | A executar agora: " + line);
                    runLine(line);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        tickCount++;
    }

    /**
     * Simula 1 passo da simulação
     */
    public void tick() {
        System.out.println("\n----- Executing now tick: " + tickCount + " -----");
        if(actions.keySet().contains(tickCount)) {
            for (String line : actions.get(tickCount)) {
                System.out.println("Tick: " + tickCount + " | A executar agora: " + line);
                runLine(line);
            }
        }

        for(Tickable tickable : tickables) {
            tickable.tick();
        }

        tickCount++;
    }

    /**
     * Executa um comando do ficheiro data.csv
     * @param line Linha a executar
     */
    public void runLine(String line) {
        String[] lineArgs = line.split(";");
        switch (lineArgs[0].toLowerCase()) {
            case "grid":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid != null) throw new RuntimeException("Grid already defined, invalid definition in line: " + line);
                grid = new Grid(Integer.parseInt(lineArgs[1]), Integer.parseInt(lineArgs[2]));
                System.out.println("\tGrelha criada com dimensões: " + (Integer.parseInt(lineArgs[1]) + 1) + " x " + (Integer.parseInt(lineArgs[2]) + 1));
                break;
            case "warehouse":
                if (lineArgs.length != 1) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);
                if (pickup == null) throw new RuntimeException("Pickup not defined, invalid definition in line: " + line);
                if (delivery == null) throw new RuntimeException("Delivery not defined, invalid definition in line: " + line);
                if (warehouse != null) throw new RuntimeException("Warehouse already defined, invalid definition in line: " + line);
                warehouse = new Warehouse(grid, pickup, delivery);
                grid.setWarehouse(warehouse);
                System.out.println("\tArmazém criado.");
                break;
            case "rack":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);
                if (warehouse == null) throw new RuntimeException("Warehouse not defined, invalid definition in line: " + line);
                Vector rackPosition = new Vector(
                        Integer.parseInt(lineArgs[1]),
                        Integer.parseInt(lineArgs[2]));
                Rack rack = new Rack(
                        grid, rackPosition);
                System.out.println("\tRack criada na posição: " + rackPosition);
                break;
            case "pickup":
                if (lineArgs.length != 9) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Area pickupPositionArea = new Area(new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2])),
                        new Vector(Integer.parseInt(lineArgs[3]),Integer.parseInt(lineArgs[4])));
                Area pickupLoadArea = new Area(new Vector(Integer.parseInt(lineArgs[5]),Integer.parseInt(lineArgs[6])),
                        new Vector(Integer.parseInt(lineArgs[7]),Integer.parseInt(lineArgs[8])));
                pickup = new LoadSiteInfo(pickupPositionArea,pickupLoadArea);

                System.out.println("\tLocal de recolha criado na área: " + pickupPositionArea.minimumXY + " a " + pickupPositionArea.maximumXY + " e com área de carga: " + pickupLoadArea.minimumXY + " a " + pickupLoadArea.maximumXY);
                break;
            case "delivery":
                if (lineArgs.length != 9) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Area deliveryPositionArea = new Area(new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2])),
                        new Vector(Integer.parseInt(lineArgs[3]),Integer.parseInt(lineArgs[4])));
                Area deliveryLoadArea = new Area(new Vector(Integer.parseInt(lineArgs[5]),Integer.parseInt(lineArgs[6])),
                        new Vector(Integer.parseInt(lineArgs[7]),Integer.parseInt(lineArgs[8])));
                delivery = new LoadSiteInfo(deliveryPositionArea,deliveryLoadArea);

                System.out.println("\tLocal de entrega criado na área: " + deliveryPositionArea.minimumXY + " a " + deliveryPositionArea.maximumXY + " e com área de carga: " + deliveryLoadArea.minimumXY + " a " + deliveryLoadArea.maximumXY);
                break;
            case "ulc":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Vector ulcPosition = new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2]));
                tickables.add(new UnitLoadCarrier(grid, ulcPosition));

                System.out.println("\tULC criado na posição: " + ulcPosition);
                break;
            case "agc":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Vector agcPosition = new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2]));
                tickables.add(new AutomaticGuidedCart(grid, agcPosition));

                System.out.println("\tAGC criado na posição: " + agcPosition);
                break;
            case "tow":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Vector towPosition = new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2]));
                tickables.add(new TowingVehicle(grid, towPosition));

                System.out.println("\tReboque criado na posição: " + towPosition);
                break;
            case "trolley":
                if (lineArgs.length != 3) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Vector trolleyPosition = new Vector(Integer.parseInt(lineArgs[1]),Integer.parseInt(lineArgs[2]));
                new Trolley(grid, trolleyPosition);

                System.out.println("\tCarrinho de transporte criado na posição: " + trolleyPosition);
                break;
            case "product":
                if (lineArgs.length != 4) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (pickup == null) throw new RuntimeException("Pickup not defined, invalid definition in line: " + line);

                ProductType productType = ProductType.getProductType(lineArgs[3]);
                if (productType == null) throw new RuntimeException("Product type invalid, invalid definition in line: " + line);

                Product product = new Product(lineArgs[1], Float.parseFloat(lineArgs[2]), productType);
                pickup.loadSite.deliverProduct(product);

                System.out.println("\tProduto criado na área de recolha com as especificações: " + product.getId() + " " + product.getName() + " " + product.getWeight() + " " + product.getType());
                break;
            case "attach":
                if (lineArgs.length != 5) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);

                Vector a = new Vector(Integer.parseInt(lineArgs[1]), Integer.parseInt(lineArgs[2]));
                Vector b = new Vector(Integer.parseInt(lineArgs[3]), Integer.parseInt(lineArgs[4]));

                Positionable positionableA = grid.getPositionable(a);
                Positionable positionableB = grid.getPositionable(b);

                if(!(positionableA instanceof TowingVehicle)) throw new RuntimeException("Objeto na posição: " + a + " não é um reboque.");
                if(!(positionableB instanceof Trolley)) throw new RuntimeException("Objeto na posição: " + b + " não é um carrinho de transporte.");

                TowingVehicle towingVehicle = (TowingVehicle) positionableA;
                Trolley trolley = (Trolley) positionableB;

                boolean success = towingVehicle.attachTrolley(trolley);

                if(success) {
                    System.out.println("\tReboque na posição: " + a + " está agora a rebocar o carrinho de transporte na posição" + b);
                }
                else {
                    System.out.println("\tNão foi possível conectar o reboque na posição: " + a + " e o carrinho de transporte na posição" + b);
                }
                break;
            case "request":
                if (lineArgs.length != 8) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);
                if (warehouse == null) throw new RuntimeException("Warehouse not defined, invalid definition in line: " + line);
                if (pickup == null) throw new RuntimeException("Pickup not defined, invalid definition in line: " + line);
                if (delivery == null) throw new RuntimeException("Pickup not defined, invalid definition in line: " + line);

                ProductType productType2 = ProductType.getProductType(lineArgs[3]);
                if (productType2 == null) throw new RuntimeException("Product type invalid, invalid definition in line: " + line);
                Product product2 = new Product(lineArgs[1], Float.parseFloat(lineArgs[2]), productType2);

                Vector sourcePosition = new Vector(Integer.parseInt(lineArgs[4]), Integer.parseInt(lineArgs[5]));
                Vector destinationPosition = new Vector(Integer.parseInt(lineArgs[6]), Integer.parseInt(lineArgs[7]));

                Object source = null;
                if (warehouse.getPickupArea().position.isWithin(sourcePosition)) {
                    source = warehouse.getPickupArea();
                }
                else if (warehouse.getDeliveryArea().position.isWithin(sourcePosition)) {
                    source = warehouse.getDeliveryArea();
                }
                else {
                    source = grid.getPositionable(sourcePosition);
                }
                Object destination = null;
                if (warehouse.getPickupArea().position.isWithin(destinationPosition)) {
                    destination = warehouse.getPickupArea();
                }
                else if (warehouse.getDeliveryArea().position.isWithin(destinationPosition)) {
                    destination = warehouse.getDeliveryArea();
                }
                else {
                    destination = grid.getPositionable(destinationPosition);
                }

                if (source == destination) throw new RuntimeException("Source and destination are same, invalid definition in line: " + line);

                if(!(source instanceof Rack || source instanceof LoadSiteInfo) || !(destination instanceof Rack || destination instanceof LoadSiteInfo)) {
                    throw new RuntimeException("Source or destination are invalid types, invalid definition in line: " + line);
                }

                Request request = new Request(source, destination, product2);
                warehouse.registerRequest(request);

                System.out.println("\tPedido prioritário adicionado ao sistema da warehouse.");
                break;
        }
    }
}
