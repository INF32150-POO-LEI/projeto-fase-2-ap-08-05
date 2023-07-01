 

import java.util.ArrayList;
import java.util.Random;

/**
 * Representa a parte física de um local de entrega/recolha
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class LoadSiteInfo {
    /**
     * Local de interação
     */
    public LoadSite loadSite;
    /**
     * Area Ocupada
     */
    public Area position;
    /**
     * Area de Cargas
     */
    public Area loadArea;

    /**
     * Construtor de LoadSiteInfo
     * @param position area ocupada
     * @param loadArea area de Cargas
     */
    public LoadSiteInfo(Area position, Area loadArea) {
        this.loadSite = new LoadSite();
        this.position = position;
        this.loadArea = loadArea;
    }

    /**
     * Retorna uma das posições de carga
     * @return Vetor da posição de carga
     */
    public Vector getLoadPosition() {
        Random random = new Random();
        ArrayList<Vector> positions = new ArrayList<>();
        for(int i = loadArea.minimumXY.x; i <= loadArea.maximumXY.x; i++) {
            for(int k = loadArea.minimumXY.y; k <= loadArea.maximumXY.y; k++) {
                positions.add(new Vector(i,k));
            }
        }
        return positions.get(random.nextInt(positions.size()));
    }

    /**
     * Retorna um produto, fornecido o modelo
     * @param product produto a obter
     * @return Produto, caso exista um do mesmo modelo
     */
    public Product getProduct(Product product) {
        if(!loadSite.hasProducts()) return null;
        if(product == null) {
            return loadSite.pickProduct();
        }
        else {
            ArrayList<Product> products = loadSite.getProducts();
            for (int i = 0; i < products.size(); i++) {
                Product newProduct = products.get(i);
                if(newProduct.isSameModel(product)) {
                    products.remove(i);
                    return newProduct;
                }
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "[ local: " + loadArea + " ]";
    }
}
