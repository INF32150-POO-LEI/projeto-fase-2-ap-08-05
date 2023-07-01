import com.sun.javafx.charts.Legend;

import java.io.BufferedReader;
import java.io.FileReader;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * Classe principal do programa
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Inicia a aplicação
     * @param args argumentos da aplicação
     */
    public static void main(String[] args) {
        launch(args);
    }

    static HashMap<Integer, ArrayList<String>> actions;

    static Grid grid;
    static LoadSiteInfo pickup;
    static LoadSiteInfo delivery;
    static Warehouse warehouse;

    static ArrayList<Tickable> tickables;

    static int tickCount;

    static int WAREHOUSE_X, WAREHOUSE_Y;

    static GridPane gridPane;

    static FloatProperty rackWeight;
    static Label weightLabel; 
    static BarChart<String, Number> chart;
    static ObservableList<String> pickupProducts;
    static ObservableList<String> deliveryProducts;
    static ObservableList<String> rackProducts;
    private static final int TILE_SIZE = 20; //Tamanho da tile no JavaFX

    /**
     * Setup simulation
     * @param actions Ações a executar
     */
    public static void setup() {
        actions = new HashMap<>();
        FileReader fileReader;
        BufferedReader buffer;

        try {
            fileReader = new FileReader("data.csv");
            buffer = new BufferedReader(fileReader);
            int count = 1;
            String line = "";
            while (line != null) {
                line = buffer.readLine();
                if(line == null) continue;
                String[] lineArgs = line.split(";");
                if(lineArgs.length < 2) throw new RuntimeException("Bad file format at line " + count);
                int tick = Integer.parseInt(lineArgs[0]);

                String finalLine = "";
                for(int i = 1; i < lineArgs.length; i++) {
                    finalLine += lineArgs[i];
                    if(i < lineArgs.length-1) finalLine += ";";
                }

                if(!actions.containsKey(tick)) actions.put(tick, new ArrayList<>());
                actions.get(tick).add(finalLine);

                count++;
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        tickCount = 0;
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

        //if(grid == null || pickup == null || delivery == null || warehouse == null) throw new RuntimeException("Bem goofy, é preciso fazer alguma coisa na simulação né?");

        WAREHOUSE_X = grid.getWallArea().maximumXY.x + 2;
        WAREHOUSE_Y = grid.getWallArea().maximumXY.y + 2;
    }

    /**
     * Simula 1 passo da simulação
     */
    public static void tick() {
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
    public static void runLine(String line) {
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
                if (lineArgs.length != 5) throw new RuntimeException("Invalid arguments length in line: " + line);
                if (grid == null) throw new RuntimeException("Grid not defined, invalid definition in line: " + line);
                if (warehouse == null) throw new RuntimeException("Warehouse not defined, invalid definition in line: " + line);
                Vector rackPosition = new Vector(
                        Integer.parseInt(lineArgs[1]),
                        Integer.parseInt(lineArgs[2]));
                int size = Integer.parseInt(lineArgs[3]);
                Direction direction = Direction.getDirection(lineArgs[4]);
                Rack rack = new Rack(grid, rackPosition, size, direction);
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


    /**
     * Atualizar a grelha do javafx
     */
    public static void updateGridUI() {
        gridPane.getChildren().clear();
        ArrayList<Rack> racks = new ArrayList<>();
        for (int row = 0; row < WAREHOUSE_X; row++) {
            for (int column = 0; column < WAREHOUSE_Y; column++) {
                StackPane stack = new StackPane();
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                Text text = new Text("");
                stack.getChildren().addAll(tile, text);  
                Vector pos = new Vector(row-1, column-1);
                Positionable positionable = grid.getPositionable(pos);

                boolean rackLoadingSpot = false;
                for(Direction direction : Direction.values()) {
                    Vector adjacentPosition = Vector.getAdjacent(pos, direction);
                    Positionable adjacentPositionable = grid.getPositionable(adjacentPosition);
                    if(!(adjacentPositionable instanceof Rack)) continue;
                    Rack rack = (Rack)adjacentPositionable;
                    if(Vector.getAdjacent(adjacentPosition, rack.getLoadDirection()).equals(pos)) {
                        rackLoadingSpot = true;
                        break;
                    }
                }

                tile.setStroke(Color.BLACK);
                
                if (grid.getWallArea().isWithin(pos) && !grid.getUsableArea().isWithin(pos)) {
                    if (pickup.position.isWithin(pos)) {
                        tile.setFill(Color.GREEN);
                    }
                    else if (delivery.position.isWithin(pos)) {
                        tile.setFill(Color.ORANGE);
                    }
                    else {
                        tile.setFill(Color.GRAY);
                    }
                }
                else if (positionable != null){
                    if (positionable instanceof Rack) {
                        tile.setFill(Color.BLUE);
                        if(!racks.contains((Rack)positionable)) {
                            racks.add((Rack)positionable);
                            text.setText("" + ((Rack)positionable).getProductCount());
                            text.setFill(Color.WHITE);
                        }
                    }
                    else if (positionable instanceof AutomaticGuidedCart) {
                        String str = ((AutomaticGuidedCart)positionable).isCarrying() ? "Y" : "N";
                        text.setText(str);
                        tile.setFill(Color.RED);
                    }
                    else if (positionable instanceof UnitLoadCarrier) {
                        String str = ((UnitLoadCarrier)positionable).isCarrying() ? "Y" : "N";
                        text.setText(str);
                        tile.setFill(Color.PINK);
                    }
                    else if (positionable instanceof TowingVehicle) {
                        String str = ((TowingVehicle)positionable).isCarrying() ? "Y" : "N";
                        text.setText(str);
                        tile.setFill(Color.MAGENTA);
                    }
                    else if (positionable instanceof Trolley) {
                        tile.setFill(Color.YELLOW);
                    }
                }
                else if (rackLoadingSpot) {
                    tile.setFill(Color.CYAN);
                }
                else {
                    tile.setFill(Color.WHITE);
                }
                
                if(text.getText().isEmpty()) gridPane.add(tile, row, WAREHOUSE_Y - column);
                else gridPane.add(stack, row, WAREHOUSE_Y - column);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        setup();
        gridPane = new GridPane();
        updateGridUI();

        createChart();

        ListView<String> pickupListView = new ListView<>(pickupProducts);
        ListView<String> deliveryListView = new ListView<>(deliveryProducts);
        ListView<String> rackListView = new ListView<>(rackProducts);

        rackWeight = new SimpleFloatProperty(0.0f);
        weightLabel = new Label();
        weightLabel.textProperty().bind(Bindings.format("Peso total armazenado: %.2f kg", rackWeight));

        Label pickupLabel = new Label("Lista de produtos para recolha");
        Label deliveryLabel = new Label("Lista de produtos entregues");
        Label rackLabel = new Label("Lista de produtos armazenados");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(pickupLabel, pickupListView, deliveryLabel, deliveryListView, rackLabel, rackListView, weightLabel);
        
        BorderPane finalRoot = new BorderPane();
        finalRoot.setLeft(gridPane);
        finalRoot.setRight(vBox);
        finalRoot.setBottom(chart);
        Scene scene = new Scene(finalRoot, WAREHOUSE_X * TILE_SIZE + 400,
                WAREHOUSE_Y * TILE_SIZE + 400 + 200);
        primaryStage.setTitle("Simulation");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        
        Thread t = new Thread(() -> {
            long lastTimeMillis = System.currentTimeMillis();
            int millisecondsElapsed = 0;

            while (true) {
                long currentTimeMillis = System.currentTimeMillis();
                millisecondsElapsed += (currentTimeMillis - lastTimeMillis);

                while (millisecondsElapsed > 500) {
                    //System.out.println("\n[ Called tick at: " + currentTimeMillis + " ]");
                    tick();
                    millisecondsElapsed -= 500;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateGridUI();
                            updateChart();
                            updateWeight();
                        }
                    });
                }

                lastTimeMillis = currentTimeMillis;
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void updateWeight() {
        float total = 0;
        ArrayList<Rack> racks = warehouse.getRacks();
        for(Rack rack : racks) {
            total += rack.getPallets().stream().map(x -> x.getWeight()).reduce(0.0f, (runningSum, value) -> runningSum + value);
            total += rack.getStorages().stream().map(x -> x.getWeight()).reduce(0.0f, (runningSum, value) -> runningSum + value);
        }
        
        rackWeight.setValue(total);
    }

    private void createChart() {
        pickupProducts = FXCollections.observableArrayList();
        deliveryProducts = FXCollections.observableArrayList();
        rackProducts = FXCollections.observableArrayList();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Distribuição de produtos");
        chart.setLegendVisible(true);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Produtos a recolher");
        series1.getData().add(new XYChart.Data<>("Quantidade", pickupProducts.size()));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Produtos entregues");
        series2.getData().add(new XYChart.Data<>("Quantidade", deliveryProducts.size()));

        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Produtos armazenados");
        series3.getData().add(new XYChart.Data<>("Quantidade", rackProducts.size()));

        chart.getData().addAll(series1, series2, series3);

        series1.getData().get(0).getNode().setStyle("-fx-bar-fill: #00db33;");
        series2.getData().get(0).getNode().setStyle("-fx-bar-fill: #fca41e;");
        series3.getData().get(0).getNode().setStyle("-fx-bar-fill: #00aeff;");

        Legend legend = (Legend)chart.lookup(".chart-legend");
        Legend.LegendItem li1=new Legend.LegendItem("Produtos a recolher", new Rectangle(10,10,Color.web("#00db33")));
        Legend.LegendItem li2=new Legend.LegendItem("Produtos entregues", new Rectangle(10,10,Color.web("#fca41e")));
        Legend.LegendItem li3=new Legend.LegendItem("Produtos armazenados", new Rectangle(10,10,Color.web("#00aeff")));
        legend.getItems().setAll(li1,li2,li3);
    }

    private void updateChart() {
        pickupProducts.clear();
        pickupProducts.addAll(FXCollections.observableArrayList(pickup.loadSite.getProducts().stream().map(x->x.getName()).toList()));
        deliveryProducts.clear();
        deliveryProducts.addAll(FXCollections.observableArrayList(delivery.loadSite.getProducts().stream().map(x->x.getName()).toList()));
        rackProducts.clear();
        warehouse.getRacks().forEach(
            w -> { 
                w.getPallets().forEach(
                    x -> x.getCardboardBoxes().forEach(
                        y -> y.getProducts().forEach(
                            z -> rackProducts.add(
                                z.getName()))));
                w.getStorages().forEach(
                    x -> x.getProducts().forEach(
                            y -> rackProducts.add(
                                y.getName())));
            }
        );

        XYChart.Series<String, Number> series1 = chart.getData().get(0);
        series1.getData().get(0).setYValue(pickupProducts.size());

        XYChart.Series<String, Number> series2 = chart.getData().get(1);
        series2.getData().get(0).setYValue(deliveryProducts.size());

        XYChart.Series<String, Number> series3 = chart.getData().get(2);
        series3.getData().get(0).setYValue(rackProducts.size());
    }
}
