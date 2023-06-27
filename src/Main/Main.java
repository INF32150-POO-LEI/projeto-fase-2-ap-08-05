package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe principal do programa
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Main {

    /**
     * Inicia a aplicação
     * @param args argumentos da aplicação
     */
    public static void main(String[] args) {
        HashMap<Integer, ArrayList<String>> actions = new HashMap<>();

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

        Simulator simulator = new Simulator(actions);

        long lastTimeMillis = System.currentTimeMillis();
        int millisecondsElapsed = 0;

        while (true) {
            long currentTimeMillis = System.currentTimeMillis();
            millisecondsElapsed += (currentTimeMillis - lastTimeMillis);

            while (millisecondsElapsed > 500) {
                //System.out.println("\n[ Called tick at: " + currentTimeMillis + " ]");
                simulator.tick();
                millisecondsElapsed -= 500;
            }

            lastTimeMillis = currentTimeMillis;
        }

        /*grid = new Grid(19,15);
        System.out.println("Grid created.");

        pickup = new LoadSiteInfo(
                new LoadSite(grid,new Vector(-1,0)),
                new Area(new Vector(-1,4), new Vector(-1,11)),
                new Area(new Vector(0,4), new Vector(0,11))
        );

        delivery = new LoadSiteInfo(
                new LoadSite(grid,new Vector(-1,0)),
                new Area(new Vector(20,4), new Vector(20,11)),
                new Area(new Vector(19,4), new Vector(19,11))
        );

        warehouse = new Warehouse(grid, pickup, delivery);

        grid.setWarehouse(warehouse);

        Rack rack1 = new Rack(grid, new Vector(1,0));
        Rack rack2 = new Rack(grid, new Vector(1,1));
        Rack rack3 = new Rack(grid, new Vector(1,2));
        Rack rack4 = new Rack(grid, new Vector(0,4));
        Rack rack5 = new Rack(grid, new Vector(1,4));
        Rack rack6 = new Rack(grid, new Vector(3,3));
        Rack rack7 = new Rack(grid, new Vector(2,4));

        long startTime = System.currentTimeMillis();

        ArrayList<Vector> path = grid.getPath(
                new Vector(0,0),
                new Vector(19,15));

        long endTime = System.currentTimeMillis();

        System.out.println("Tempo do pathfinding: " + (endTime-startTime) + "ms");

        System.out.println("Caminho completo: " + path);
        if(path != null)  System.out.println("Próxima posição: " + path.get(1));
        */
    }
}
