package Classes;

import java.util.ArrayList;

/**
 * Representa uma prateleira
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Rack extends Positionable {
    private ArrayList<Storage> storages;
    private ArrayList<Pallet> pallets;

    /**
     * Construtor da Rack
     * @param grid Grelha a que pertence
     * @param position Posição desejada
     */
    public Rack(Grid grid, Vector position) {
        super(grid, position);
        storages = new ArrayList<>();
        pallets = new ArrayList<>();
        grid.getWarehouse().registerRack(this);
    }

    /**
     * Retorna as embalagens contidas nesta prateleira
     * @return ArrayList de embalagens
     */
    public ArrayList<Storage> getStorages() {
        return storages;
   }

    /**
     * Retorna as paletes contidas nesta prateleira
     * @return ArrayList de paletes
     */
    public ArrayList<Pallet> getPallets() {
        return pallets;
    }

    /**
     * Tenta adicionar uma embalagem
     * @param storage Storage a adicionar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean addStorage(Storage storage) {
        if(storage == null) return false;
        if(storages.contains(storage)) return false;
        return storages.add(storage);
    }

    /**
     * Tenta remover uma embalagem
     * @param storage Storage a remover
     * @return Verdadeiro caso bem sucedido
     */
    public boolean removeStorage(Storage storage) {
        if(storage == null) return false;
        if(!storages.contains(storage)) return false;
        return storages.remove(storage);
    }

    /**
     * Tenta adicionar uma palete
     * @param pallet Pallet a adicionar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean addPallet(Pallet pallet) {
        if(pallet == null) return false;
        if(pallets.contains(pallet)) return false;
        return pallets.add(pallet);
    }

    /**
     * Tenta remover uma palete
     * @param pallet Pallet a remover
     * @return Verdadeiro caso bem sucedido
     */
    public boolean removePallet(Pallet pallet) {
        if(pallet == null) return false;
        if(!pallets.contains(pallet)) return false;
        return pallets.remove(pallet);
    }

    /**
     * Retorna a posição para acessar esta prateleira
     * @return Vetor da posição
     */
    public Vector getLoadPosition() {
        return Vector.getAdjacent(getPosition(), Direction.DOWN);
    }


    private Product pickProduct() {
        for (int i = 0; i < storages.size(); i++) {
            Storage storage = storages.get(i);
            ArrayList<Product> products = storage.getProducts();
            if(products.size() > 0) {
                System.out.println("Produto " + products.get(0) + " removido da prateleira na célula " + getPosition());
                return products.remove(0);
            }
        }

        for (int i = 0; i < pallets.size(); i++) {
            ArrayList<CardboardBox> cardboardBoxes = pallets.get(i).getCardboardBoxes();
            for (int k = 0; k < cardboardBoxes.size(); k++) {
                CardboardBox cardboardBox = cardboardBoxes.get(k);
                ArrayList<Product> products = cardboardBox.getProducts();
                if(products.size() > 0) {
                    System.out.println("Produto " + products.get(0) + " removido da prateleira na célula " + getPosition());
                    return products.remove(0);
                }
            }
        }

        return null;
    }


    private Product pickProduct(Product product) {
        for (int i = 0; i < storages.size(); i++) {
            Storage storage = storages.get(i);
            ArrayList<Product> products = storage.getProducts();
            for(int k = 0; k < products.size(); k++) {
                Product newProduct = products.get(k);
                if(newProduct.isSameModel(product)) {
                    System.out.println("Produto " + products.get(k) + " removido da prateleira na célula " + getPosition());
                    return products.remove(k);
                }
            }
        }

        for (int i = 0; i < pallets.size(); i++) {
            ArrayList<CardboardBox> cardboardBoxes = pallets.get(i).getCardboardBoxes();
            for (int k = 0; k < cardboardBoxes.size(); k++) {
                CardboardBox cardboardBox = cardboardBoxes.get(k);
                ArrayList<Product> products = cardboardBox.getProducts();
                for(int m = 0; m < products.size(); m++) {
                    Product newProduct = products.get(m);
                    if(newProduct.isSameModel(product)) {
                        System.out.println("Produto " + products.get(m) + " removido da prateleira na célula " + getPosition());
                        return products.remove(m);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Verifica se contém produtos
     * @return Verdadeiro, caso existam produtos
     */
    public boolean hasProducts() {
        for (int i = 0; i < storages.size(); i++) {
            Storage storage = storages.get(i);
            ArrayList<Product> products = storage.getProducts();
            if(products.size() > 0) {
                return true;
            }
        }

        for (int i = 0; i < pallets.size(); i++) {
            ArrayList<CardboardBox> cardboardBoxes = pallets.get(i).getCardboardBoxes();
            for (int k = 0; k < cardboardBoxes.size(); k++) {
                CardboardBox cardboardBox = cardboardBoxes.get(k);
                ArrayList<Product> products = cardboardBox.getProducts();
                if(products.size() > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retorna e remove um produto fornecido um modelo, ou qualquer produto caso null seja fornecido
     * @param product Modelo do produto a obter
     * @return Produto, caso exista um do mesmo modelo do fornecido ou o primeiro produto encontrado caso null seja fornecido
     */
    public Product getProduct(Product product) {
        if(!hasProducts()) return null;
        if(product == null) {
            return pickProduct();
        }
        else {
            return pickProduct(product);
        }
    }

    /**
     * Tenta adicionar um produto
     * @param product produto a adicionar
     * @return Verdadeiro, caso bem sucedido
     */
    public boolean addProduct(Product product) {
        if(product == null) {
            return false;
        }

        System.out.println("Produto " + product + " adicionado à prateleira na célula " + getPosition());

        CardboardBox cardboardBox = new CardboardBox();
        cardboardBox.addProduct(product);
        return storages.add(cardboardBox);
    }

    @Override
    public String toString() {
        return "[ prateleira: " + getPosition() + " ]";
    }
}
