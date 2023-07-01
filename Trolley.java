 

import java.util.ArrayList;

/**
 * Representa um carrinho de transporte
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Trolley extends Positionable implements CargoTransporter<Storage> {

    private static final float CAPACITY = 200f;
    private ArrayList<Storage> storages;

    /**
     * Constructor do Trolley
     * @param grid Grelha a que pertence
     * @param position Posição inicial
     */
    public Trolley(Grid grid, Vector position) {
        super(grid, position);
        storages = new ArrayList<>();
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
}
